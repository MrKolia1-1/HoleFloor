package at.talltales.holefloor.arena;

import at.talltales.holefloor.arena.runnable.ArenaTimerWait;
import at.talltales.holefloor.plugin.HoleFloor;
import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class ArenaManager {
    private final List<Arena> arenas = new ArrayList<>();

    public Arena create() {
        Arena arena = new Arena();
        arena.uuid = UUID.randomUUID();
        arena.instance = HoleFloor.getInstance();
        arena.players = new ArrayList<>();
        arena.type = ArenaType.values()[0];
        arena.state = ArenaState.values()[0];
        arena.world = Bukkit.getWorld("1a24cb5b-a9bc-4fda-b966-4386993f4798");//new WorldCreator(arena.uuid.toString()).generator(new ArenaWorldVoid()).createWorld();
        arena.world.setSpawnLocation(20, 111, 13);
        arena.lifetimeMap = new HashMap<>();
        arena.respawnMap = new HashMap<>();

        arena.timerWait = new ArenaTimerWait(arena);
        arena.timerWait.runTaskTimer(HoleFloor.getInstance(), 0, 20);

        this.loadArena(arena, arena.type);
        this.arenas.add(arena);
        return arena;
    }

    public void delete(Arena arena) {
        this.arenas.remove(arena);
    }

    public void connect(Arena arena, Player player) {
        arena.players.add(player);
        player.teleport(arena.world.getSpawnLocation());
    }

    public void disconnect(Arena arena, Player player) {
        arena.players.remove(player);
    }

    public void loadArena(Arena arena, ArenaType type) {
        StructureBlockLibApi.INSTANCE
                .loadStructure(HoleFloor.getInstance())
                .at(new Location(Bukkit.getWorld("1a24cb5b-a9bc-4fda-b966-4386993f4798"), 0, 100, 0))
                .loadFromPath(Paths.get(HoleFloor.getInstance().getDataFolder() + "/structures/" + type.name().toLowerCase() + ".nbt"));
    }

    public List<Arena> getArenas() {
        return arenas;
    }
}
