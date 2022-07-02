package me.talltales.holefloor.arena;

import me.talltales.holefloor.arena.runnables.timer.HoleFloorArenaTimerRunnable;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public final class HoleFloorArena {
    public UUID uuid;
    public World world;
    public List<Player> players;
    public HoleFloorArenaType type;
    public HoleFloorArenaState state;
    public HoleFloorArenaTimerRunnable timerRunnable;

    public HoleFloorArena() {
        super();
    }

}