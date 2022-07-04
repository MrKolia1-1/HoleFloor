package at.talltales.holefloor.arena;

import at.talltales.holefloor.arena.runnable.ArenaTimerLifetime;
import at.talltales.holefloor.arena.runnable.ArenaTimerRespawn;
import at.talltales.holefloor.arena.runnable.ArenaTimerWait;
import at.talltales.holefloor.plugin.HoleFloor;
import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.nio.file.Paths;
import java.util.*;

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

    public void runArena(Arena arena) {
        arena.state = ArenaState.PLAYING;
        arena.players.forEach(player -> {
            ArenaTimerLifetime.ArenaLifeTimeMap lifeTimeMap = new ArenaTimerLifetime.ArenaLifeTimeMap();
            lifeTimeMap.bossBar = Bukkit.createBossBar("123 ", BarColor.WHITE, BarStyle.SEGMENTED_20);
            lifeTimeMap.bossBar.addPlayer(player);
            lifeTimeMap.lifetime = 40;
            lifeTimeMap.lives = 5;
            lifeTimeMap.isDeath = false;
            arena.lifetimeMap.put(player, lifeTimeMap);

            ArenaTimerRespawn.ArenaTimerRespawnMap  respawnMap = new ArenaTimerRespawn.ArenaTimerRespawnMap();
            respawnMap.bossBar = Bukkit.createBossBar(" ", BarColor.RED, BarStyle.SEGMENTED_20);
            respawnMap.respawnTime = 10;
            respawnMap.isDeath = false;
            arena.respawnMap.put(player, respawnMap);
        });
        arena.timerLifetime = new ArenaTimerLifetime(arena, arena.lifetimeMap);
        arena.timerLifetime.runTaskTimer(HoleFloor.getInstance(), 0, 5);

        arena.timerRespawn = new ArenaTimerRespawn(arena, arena.respawnMap);
        arena.timerRespawn.runTaskTimer(HoleFloor.getInstance(), 0, 20);

        arena.players.forEach(player -> {
            player.teleport(arena.type.getVectors()[new Random().nextInt(ArenaType.values().length)].toLocation(arena.world));
        });
    }

    public void stopArena() {

    }

    public void restartArena() {

    }

    public void deathPlayer(Arena arena, Player player) {
        arena.lifetimeMap.get(player).isDeath = true;
        arena.respawnMap.get(player).isDeath = true;
        arena.lifetimeMap.get(player).bossBar.removeAll();
        arena.lifetimeMap.get(player).lives--;
        player.setGameMode(GameMode.SPECTATOR);
    }

    public void respawnPlayer(Arena arena, Player player) {
        arena.lifetimeMap.get(player).isDeath = false;
        arena.respawnMap.get(player).isDeath = false;
        arena.respawnMap.get(player).bossBar.removeAll();
        arena.lifetimeMap.get(player).bossBar.addPlayer(player);
        arena.respawnMap.get(player).respawnTime = 10;
        arena.lifetimeMap.get(player).lifetime = 40;
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(arena.type.getVectors()[new Random().nextInt(ArenaType.values().length)].toLocation(arena.world));
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
