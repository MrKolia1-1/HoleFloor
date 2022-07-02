package me.talltales.holefloor.arena.runnables.timer;

import me.talltales.holefloor.arena.HoleFloorArena;
import me.talltales.holefloor.arena.HoleFloorArenaState;
import me.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BossBar;

import java.util.HashSet;
import java.util.Set;

public final class HoleFloorArenaTimerRunnable {
    private final Set<Integer> ids;
    private final HoleFloorArena arena;
    private final HoleFloorArenaTimerWait timerWait;
    private final HoleFloorArenaTimerStart timerStart;

    public HoleFloorArenaTimerRunnable(HoleFloorArena arena) {
        this.arena = arena;
        this.ids = new HashSet<>();
        this.timerWait = new HoleFloorArenaTimerWait(this.arena);
        this.timerStart = new HoleFloorArenaTimerStart(this.arena);
        this.timerWait.run();
    }

    public void run() {
        ids.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(HoleFloor.getInstance(), () -> {
            if (this.arena.state.equals(HoleFloorArenaState.WAITING)) {
                if (this.arena.players.size() >= 2) {
                    this.arena.state = HoleFloorArenaState.STARTING;
                    this.timerWait.cancel();
                    this.timerStart.run();
                }
            }
            if (this.arena.state.equals(HoleFloorArenaState.STARTING)) {
                if (this.arena.players.size() < 2) {
                    this.arena.state = HoleFloorArenaState.WAITING;
                    this.timerStart.cancel();
                    this.timerWait.run();
                }
            }
        },0,0));
    }

    public void cancel() {
        this.ids.forEach(Bukkit.getScheduler()::cancelTask);
    }
}
