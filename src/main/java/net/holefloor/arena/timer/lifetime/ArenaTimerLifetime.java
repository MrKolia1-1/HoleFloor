package net.holefloor.arena.timer.lifetime;

import net.holefloor.HoleFloor;
import net.holefloor.arena.Arena;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;

public final class ArenaTimerLifetime {
    private final Arena arena;
    public List<Player> top;
    public HashMap<Player, ArenaLifeTimeMap> hashMap;

    public ArenaTimerLifetime(Arena arena) {
        this.arena = arena;
        this.top = new ArrayList<>();
        this.hashMap = new HashMap<>();
    }

    public void run() {
        List<Player> alive = new ArrayList<>();
        this.hashMap.forEach((player, map) -> {
            if (!map.isEliminated) {
                alive.add(player);
            }
        });
        if (alive.size() == 0) {
            HoleFloor.getInstance().manager.end(arena);
            return;
        }

        this.hashMap.forEach((player, map) -> {
            if (!map.bossBar.getPlayers().contains(player)) {
                map.bossBar.addPlayer(player);
            }
            if (!map.isDead && !map.isEliminated) {
                if (map.lifetime > 500) {
                    map.lifetime = 500;
                }
                if (map.lifetime >= 0) {
                    map.bossBar.setTitle(Objects.requireNonNull(arena.instance.locale.getString("arena.bossbar.lifetime"))
                            .replace("%lives%", String.valueOf(map.lives)));
                    map.bossBar.setProgress(map.lifetime / 500D);
                    if (map.lifetime >= 300) {
                        map.bossBar.setColor(BarColor.GREEN);
                    }
                    if (map.lifetime >= 300 && map.lifetime <= 100) {
                        map.bossBar.setColor(BarColor.YELLOW);
                    }

                    if (map.lifetime <= 100) {
                        map.bossBar.setColor(BarColor.RED);
                    }
                    map.lifetime--;
                }
            }
            if (!map.isDead && !map.isEliminated) {
                if (player.getLocation().getBlockY() <= 100) {
                    player.setVelocity(player.getVelocity().multiply(-20));
                    if (map.lives <= 1) {
                        this.eliminate(player);
                    }
                    if (map.lives >= 1) {
                        this.death(player);
                    }
                }
                if (map.lifetime <= 0) {
                    if (map.lives <= 1) {
                        this.eliminate(player);
                    }
                    if (map.lives >= 1) {
                        this.death(player);
                    }
                }
            }
            if (this.arena.scheduler.tick % 20L == 0) {
                if (map.isDead && !map.isEliminated) {
                    if (map.respawnTime >= 0) {
                        map.bossBar.setTitle(Objects.requireNonNull(arena.instance.locale.getString("arena.bossbar.respawn"))
                                .replace("%seconds%", String.valueOf(map.respawnTime)));

                        if (map.respawnTime <= 3 && map.respawnTime >= 1) {
                            player.sendTitle("§b" + map.respawnTime, Objects.requireNonNull(this.arena.instance.locale.getString("arena.title.respawn")), 0, 30, 0);
                            player.playSound(player, Sound.BLOCK_AMETHYST_CLUSTER_HIT, 1, 2);
                        }
                    }
                    if (map.respawnTime == 0) {
                        player.sendTitle("§aGO", "", 0, 25, 0);
                        this.respawn(player);
                    }
                    map.respawnTime--;
                }
            }
        });
    }

    public void death(Player player) {
        ArenaLifeTimeMap map = this.hashMap.get(player);
        map.respawnTime = 15;
        map.isDead = true;
        map.lives--;

        this.arena.properties.citizens.players.forEach(consumer -> {
            consumer.sendMessage(Objects.requireNonNull(arena.instance.locale.getString("arena.message.death"))
                    .replace("%lives%", String.valueOf(map.lives))
                    .replace("%player%", consumer.getName()));
        });

        map.bossBar.setColor(BarColor.RED);
        map.bossBar.setProgress(1);

        Objects.requireNonNull(
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                .setBaseValue(map.lives == 0 ? 20 : (map.lives * 2));
        player.setHealth(map.lives * 2);
        player.playSound(player, Sound.ENTITY_PLAYER_DEATH, 1, 1);
        player.sendTitle("", Objects.requireNonNull(this.arena.instance.locale.getString("arena.title.death")), 20, 50, 10);
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void respawn(Player player) {
        ArenaLifeTimeMap map = this.hashMap.get(player);
        map.isDead = false;
        map.lifetime = 500;
        player.playSound(player, Sound.ITEM_GOAT_HORN_SOUND_1, 1, 1);
        player.setGameMode(GameMode.ADVENTURE);
        Location location = arena.properties.type.getSpawns().get(new Random().nextInt(arena.properties.type.getSpawns().size()));
        location.setWorld(arena.properties.world);
        player.teleport(location);
    }

    public void eliminate(Player player) {
        ArenaLifeTimeMap map = this.hashMap.get(player);
        map.isEliminated = true;
        map.isDead = true;
        map.lives = 0;
        map.lifetime = 0;

        map.bossBar.setTitle(Objects.requireNonNull(arena.instance.locale.getString("arena.bossbar.eliminated")));
        map.bossBar.setColor(BarColor.RED);
        map.bossBar.setProgress(1);

        this.arena.properties.citizens.players.forEach(consumer -> {
            consumer.sendMessage(Objects.requireNonNull(arena.instance.locale.getString("arena.message.eliminated"))
                    .replace("%player%", consumer.getName()));
        });
        player.playSound(player, Sound.ENTITY_ALLAY_DEATH, 1, 1);
        player.sendTitle("", Objects.requireNonNull(this.arena.instance.locale.getString("arena.title.eliminated")), 20, 50, 10);
        player.setGameMode(GameMode.SPECTATOR);
        this.top.add(player);
        map.lives--;
    }

    public void cancel() {
        this.hashMap.forEach((player, map) -> {
            map.bossBar.removeAll();
        });
    }
}