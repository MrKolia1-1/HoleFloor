package me.talltales.holefloor.arena.holefloor.arena.runnables.lifetime;

import me.talltales.holefloor.arena.holefloor.arena.HoleFloorArena;
import me.talltales.holefloor.arena.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.util.*;

public class HoleFloorArenaLifetimeRunnable {
    private final Set<Integer> ids;
    private final HoleFloorArena arena;
    private final List<HoleFloorArenaLifetimeMap> map;

    public HoleFloorArenaLifetimeRunnable(HoleFloorArena arena) {
        this.arena = arena;
        this.map = new ArrayList<>();
        this.ids = new HashSet<>();
    }

    public void run() {
        this.arena.players.forEach(player -> {
            HoleFloorArenaLifetimeMap map = new HoleFloorArenaLifetimeMap();
            map.bossBar = Bukkit.createBossBar("LifeTime", BarColor.WHITE, BarStyle.SEGMENTED_20);
            map.lifetime = 100;
            map.player = player;
            map.bossBar.addPlayer(player);
            map.lives = 5;
            map.isDead = false;
            map.respawnTime = 10;
            map.isRespawning = false;
            this.map.add(map);
        });
        ids.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(HoleFloor.getInstance(), () -> {
            this.map.forEach(map -> {
                if (!map.isDead) {
                    if (map.lifetime > 70) {
                        map.bossBar.setColor(BarColor.GREEN);
                    }
                    if (map.lifetime > 30 && map.lifetime < 70) {
                        map.bossBar.setColor(BarColor.YELLOW);
                    }
                    if (map.lifetime < 30) {
                        map.bossBar.setColor(BarColor.RED);
                    }
                    if (map.lifetime <= 0) {
                        this.deathPlayer(map, HoleFloorArenaLifetimeDeathReason.LIFETIME);
                        return;
                    }
                    map.lifetime = map.lifetime - 1;
                    map.bossBar.setTitle(String.valueOf(map.lifetime));
                    map.bossBar.setProgress(map.lifetime / 100D);
                }
            });
        },0,5));
    }

    public void deathPlayer(HoleFloorArenaLifetimeMap map, HoleFloorArenaLifetimeDeathReason reason) {
        map.isDead = true;
        map.player.setGameMode(GameMode.SPECTATOR);
        map.lives--;
        map.bossBar.removeAll();
        map.player.sendMessage(HoleFloor.getInstance().getLocale().getString("arena.message.death"));;

        if (map.lives == 0) {

        }
    }

    public void respawnPlayer(HoleFloorArenaLifetimeMap map) {
        map.player.setGameMode(GameMode.SURVIVAL);
        map.bossBar.addPlayer(map.player);
        map.lifetime = 100;

        Location location = this.arena.type.getLocations().get(new Random().nextInt(this.arena.type.getLocations().size()));
        location.setWorld(this.arena.world);
        map.player.sendMessage(HoleFloor.getInstance().getLocale().getString("arena.message.respawn"));
        map.player.teleport(location);
    }

    public void cancel() {
        this.ids.forEach(Bukkit.getScheduler()::cancelTask);
    }

    public List<HoleFloorArenaLifetimeMap> getMap() {
        return map;
    }
}