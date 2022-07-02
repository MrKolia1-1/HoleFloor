package me.talltales.holefloor.arena.runnables.lifetime;

import me.talltales.holefloor.arena.HoleFloorArena;
import me.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
                    }
                    this.barHashMap.get(map.player).setTitle("respawn in " + map.respawnTime);
                    map.respawnTime--;
                }
            });
        }, 0, 20));
    }

    public void cancel() {
        this.ids.forEach(Bukkit.getScheduler()::cancelTask);
    }
}