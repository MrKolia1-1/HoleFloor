package me.talltales.holefloor.arena.holefloor.arena.runnables.lifetime;

import me.talltales.holefloor.arena.holefloor.arena.HoleFloorArena;
import me.talltales.holefloor.arena.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class HoleFloorArenaLifetimeRespawn {
    private final Set<Integer> ids;
    private final HoleFloorArena arena;
    private final HashMap<Player, BossBar> barHashMap;

    public HoleFloorArenaLifetimeRespawn(HoleFloorArena arena) {
        this.arena = arena;
        this.ids = new HashSet<>();
        this.barHashMap = new HashMap<>();
    }

    public void run() {
        this.arena.players.forEach(player -> {
            this.barHashMap.put(player, Bukkit.createBossBar("", BarColor.RED, BarStyle.SEGMENTED_20));
        });
        ids.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(HoleFloor.getInstance(), () -> {
            arena.lifetimeRunnable.getMap().forEach(map -> {
                if (map.lives != 0) {
                    if (map.isDead) {
                        this.barHashMap.get(map.player).addPlayer(map.player);
                        if (!map.isRespawning) {
                            map.isRespawning = true;
                            map.respawnTime = 10;
                        }
                        if (map.respawnTime == 0) {
                            map.isRespawning = false;
                            map.isDead = false;
                            map.respawnTime = 10;
                            this.arena.lifetimeRunnable.respawnPlayer(map);
                            this.barHashMap.get(map.player).removeAll();
                        }
                        AtomicReference<String> title = new AtomicReference<>(HoleFloor.getInstance().getLocale().getString("arena.timer.respawn"));
                        title.set(title.get().replace("{0}", String.valueOf(map.respawnTime)));
                        this.barHashMap.get(map.player).setTitle(title.get());
                        map.respawnTime--;
                    }
                }
            });
        }, 0, 20));
    }

    public void cancel() {
        this.ids.forEach(Bukkit.getScheduler()::cancelTask);
    }
}