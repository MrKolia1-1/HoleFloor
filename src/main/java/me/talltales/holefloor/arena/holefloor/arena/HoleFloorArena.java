package me.talltales.holefloor.arena.holefloor.arena;

import me.talltales.holefloor.arena.holefloor.arena.runnables.lifetime.HoleFloorArenaLifetimeRespawn;
import me.talltales.holefloor.arena.holefloor.arena.runnables.lifetime.HoleFloorArenaLifetimeRunnable;
import me.talltales.holefloor.arena.holefloor.arena.runnables.timer.HoleFloorArenaTimerRunnable;
import me.talltales.holefloor.arena.holefloor.plugin.HoleFloor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class HoleFloorArena {
    public UUID uuid;
    public World world;
    public List<Player> players;
    public HoleFloorArenaType type;
    public HoleFloorArenaState state;
    public HoleFloorArenaTimerRunnable timerRunnable;
    public HoleFloorArenaLifetimeRunnable lifetimeRunnable;
    public HoleFloorArenaLifetimeRespawn lifetimeRespawn;

    public HoleFloorArena() {
        super();
    }

    public void startGame() {
        this.state = HoleFloorArenaState.PLAYING;
        this.lifetimeRunnable.run();
        this.lifetimeRespawn.run();
        this.players.forEach(player -> {
            Location location = this.type.getLocations().get(new Random().nextInt(type.getLocations().size()));
            location.setWorld(this.world);
            player.sendMessage(HoleFloor.getInstance().getLocale().getString("arena.message.game-started"));
            player.teleport(location);
        });
    }
}