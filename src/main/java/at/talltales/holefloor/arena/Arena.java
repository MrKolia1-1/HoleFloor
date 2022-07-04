package at.talltales.holefloor.arena;

import at.talltales.holefloor.arena.runnable.ArenaTimerLifetime;
import at.talltales.holefloor.arena.runnable.ArenaTimerRespawn;
import at.talltales.holefloor.arena.runnable.ArenaTimerStart;
import at.talltales.holefloor.arena.runnable.ArenaTimerWait;
import at.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
}
