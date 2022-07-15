package net.holefloor.arena.timer;

import net.holefloor.HoleFloor;
import net.holefloor.arena.Arena;
import net.holefloor.arena.powerup.ArenaBoosterType;
import net.holefloor.arena.properties.ArenaState;
import net.holefloor.arena.timer.lifetime.ArenaTimerLifetime;
import net.holefloor.arena.timer.lobby.ArenaTimerEnd;
import net.holefloor.arena.timer.lobby.ArenaTimerStart;
import net.holefloor.arena.timer.lobby.ArenaTimerWait;
import net.holefloor.tab.HoleFloorTab;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

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

            if (this.tick % 200 == 0) {
                if (!this.arena.booster.isSpawned) {
                    this.arena.booster.spawn(ArenaBoosterType.values()[new Random().nextInt(ArenaBoosterType.values().length)]);
                }
            }
            if (this.arena.booster.isSpawned) {
                this.arena.booster.stand.setRotation(this.arena.booster.stand.getLocation().getYaw() + 10, 0);
                List<Entity> nearbyEntites = this.arena.booster.stand.getNearbyEntities(0.5, 0.5, 0.5);
                nearbyEntites.forEach(entity -> {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        if (!this.timerLifetime.hashMap.get(player).isDead && !this.timerLifetime.hashMap.get(player).isEliminated) {
                            this.arena.booster.collect(player);
                        }
                    }
                });
            }
        }

        HoleFloor.getInstance().tab.update();
        HoleFloor.getInstance().board.update();
        if (this.arena.properties.state.equals(ArenaState.ENDING)) {
            this.timerEnd.run();
        }
    }

    public void cancel() {
        this.arena.instance.getServer().getScheduler().cancelTask(this.id);
    }
}
