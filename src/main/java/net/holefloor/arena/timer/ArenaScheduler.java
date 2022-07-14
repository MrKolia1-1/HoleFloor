package net.holefloor.arena.timer;

import net.holefloor.HoleFloor;
import net.holefloor.arena.Arena;
import net.holefloor.arena.properties.ArenaState;
import net.holefloor.arena.tab.HoleFloorTab;
import net.holefloor.arena.timer.lifetime.ArenaTimerLifetime;
import net.holefloor.arena.timer.lobby.ArenaTimerEnd;
import net.holefloor.arena.timer.lobby.ArenaTimerStart;
import net.holefloor.arena.timer.lobby.ArenaTimerWait;

public final class ArenaScheduler {
    private final Arena arena;
    public Integer tick;
    public Integer id;

    public ArenaTimerWait timerWait;
    public ArenaTimerStart timerStart;
    public ArenaTimerEnd timerEnd;
    public ArenaTimerLifetime timerLifetime;

    public ArenaScheduler(Arena arena) {
        this.arena = arena;
        this.tick = 0;
        this.timerWait = new ArenaTimerWait(arena);
        this.timerStart = new ArenaTimerStart(arena);
        this.timerEnd = new ArenaTimerEnd(arena);
        this.timerLifetime = new ArenaTimerLifetime(arena);
    }

    public void run() {
        if (this.arena.properties.state.equals(ArenaState.WAITING)) {
            if (this.arena.properties.citizens.players.size() >= this.arena.properties.citizens.min) {
                this.arena.properties.state = ArenaState.STARTING;
                this.timerWait.cancel();
                return;
            }
            this.timerWait.run();
        }
        if (this.arena.properties.state.equals(ArenaState.STARTING)) {
            if (this.arena.properties.citizens.players.size() < this.arena.properties.citizens.min) {
                this.arena.properties.state = ArenaState.WAITING;
                this.timerStart.cancel();
                return;
            }
            this.timerStart.run();
        }
        if (this.arena.properties.state.equals(ArenaState.PLAYING)) {
            this.timerLifetime.run();
        }

        if (this.arena.properties.state.equals(ArenaState.ENDING)) {
            this.timerEnd.run();
        }
        HoleFloor.getInstance().holeFloorTab.update();
    }

    public void cancel() {
        this.arena.instance.getServer().getScheduler().cancelTask(this.id);
    }
}
