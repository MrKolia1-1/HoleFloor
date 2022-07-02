package me.talltales.holefloor.arena.holefloor.arena.runnables.lifetime;

import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public final class HoleFloorArenaLifetimeMap {
    public Player player;
    public BossBar bossBar;
    public int lifetime;
    public int lives;
    public boolean isDead;
    public boolean isRespawning;
    public int respawnTime;

    public HoleFloorArenaLifetimeMap() {
        super();
    }
}
