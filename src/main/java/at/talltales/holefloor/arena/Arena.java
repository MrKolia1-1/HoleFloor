package at.talltales.holefloor.arena;

import at.talltales.holefloor.arena.runnable.ArenaTimerLifetime;
import at.talltales.holefloor.arena.runnable.ArenaTimerRespawn;
import at.talltales.holefloor.arena.runnable.ArenaTimerStart;
import at.talltales.holefloor.arena.runnable.ArenaTimerWait;
import at.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Arena {
    public UUID uuid;
    public World world;
    public ArenaType type;
    public ArenaState state;
    public HoleFloor instance;
    public List<Player> players;

    public ArenaTimerWait timerWait;
    public ArenaTimerStart timerStart;
    public ArenaTimerLifetime timerLifetime;
    public ArenaTimerRespawn timerRespawn;

    public HashMap<Player, ArenaTimerLifetime.ArenaLifeTimeMap> lifetimeMap;
    public HashMap<Player, ArenaTimerRespawn.ArenaTimerRespawnMap> respawnMap;

    public void start() {
        this.state = ArenaState.PLAYING;
        this.players.forEach(player -> {
            ArenaTimerLifetime.ArenaLifeTimeMap lifeTimeMap = new ArenaTimerLifetime.ArenaLifeTimeMap();
            lifeTimeMap.bossBar = Bukkit.createBossBar(" ", BarColor.WHITE, BarStyle.SEGMENTED_20);
            lifeTimeMap.bossBar.addPlayer(player);
            lifeTimeMap.lifetime = 40;
            lifeTimeMap.lives = 5;
            lifeTimeMap.isDeath = false;
            this.lifetimeMap.put(player, lifeTimeMap);

            ArenaTimerRespawn.ArenaTimerRespawnMap  respawnMap = new ArenaTimerRespawn.ArenaTimerRespawnMap();
            respawnMap.bossBar = Bukkit.createBossBar(" ", BarColor.RED, BarStyle.SEGMENTED_20);
            respawnMap.respawnTime = 10;
            respawnMap.isDeath = false;
            this.respawnMap.put(player, respawnMap);
        });
        this.timerLifetime = new ArenaTimerLifetime(this, lifetimeMap);
        this.timerLifetime.runTaskTimer(HoleFloor.getInstance(), 0, 5);

        this.timerRespawn = new ArenaTimerRespawn(this, respawnMap);
        this.timerRespawn.runTaskTimer(HoleFloor.getInstance(), 0, 20);

        this.players.forEach(player -> {
            player.teleport(type.getVectors()[new Random().nextInt(ArenaType.values().length)].toLocation(world));
        });
    }

    public void stop() {

    }

    public void restart() {

    }

    public void deathPlayer(Player player) {
        lifetimeMap.get(player).isDeath = true;
        respawnMap.get(player).isDeath = true;
    }

    public void respawnPlayer(Player player) {
    }
}
