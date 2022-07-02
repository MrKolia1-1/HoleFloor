package me.talltales.holefloor.arena.holefloor.arena;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import me.talltales.holefloor.arena.holefloor.arena.runnables.lifetime.HoleFloorArenaLifetimeRespawn;
import me.talltales.holefloor.arena.holefloor.arena.runnables.lifetime.HoleFloorArenaLifetimeRunnable;
import me.talltales.holefloor.arena.holefloor.arena.runnables.timer.HoleFloorArenaTimerRunnable;
import me.talltales.holefloor.arena.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class HoleFloorManager {
    private final List<HoleFloorArena> arenas = new ArrayList<>();

    public HoleFloorArena createNewArena() {
        HoleFloorArena arena = new HoleFloorArena();
        arena.uuid = UUID.randomUUID();
        arena.state = HoleFloorArenaState.WAITING;
        arena.players = new ArrayList<>();
        arena.type = HoleFloorArenaType.values()[0];
        arena.world = new WorldCreator(arena.uuid.toString()).generator(new ChunkGenerator() {
            @Override
            public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
                super.generateNoise(worldInfo, random, chunkX, chunkZ, chunkData);
            }
        }).createWorld();
        arena.world.setSpawnLocation(14, 112, 21);

        arena.timerRunnable = new HoleFloorArenaTimerRunnable(arena);
        arena.lifetimeRunnable = new HoleFloorArenaLifetimeRunnable(arena);
        arena.lifetimeRespawn = new HoleFloorArenaLifetimeRespawn(arena);

        arena.timerRunnable.run();
        this.loadStructure(arena, arena.type);
        this.arenas.add(arena);
        return arena;
    }

    public void loadStructure(HoleFloorArena arena, HoleFloorArenaType type) {
        StructureBlockLibApi.INSTANCE
                .loadStructure(HoleFloor.getInstance())
                .at(new Location(Bukkit.getWorld(arena.uuid.toString()), 0, 100, 0))
                .loadFromPath(Paths.get(HoleFloor.getInstance().getDataFolder() + "/structures/" + type.name().toLowerCase() + ".nbt"));
    }

    public void deleteArena() {
        this.arenas.clear();
    }

    public void connectPlayer(HoleFloorArena arena, Player player) {
        arena.players.add(player);
        player.teleport(arena.world.getSpawnLocation());
    }

    public void disconnectPlayer(HoleFloorArena arena, Player player) {
        arena.players.remove(player);
    }

    public List<HoleFloorArena> getArenas() {
        return arenas;
    }
}
