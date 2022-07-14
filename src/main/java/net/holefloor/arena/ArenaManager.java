package net.holefloor.arena;

import com.github.shynixn.structureblocklib.api.bukkit.StructureBlockLibApi;
import net.holefloor.HoleFloor;
import net.holefloor.arena.properties.ArenaCitizens;
import net.holefloor.arena.properties.ArenaProperties;
import net.holefloor.arena.properties.ArenaState;
import net.holefloor.arena.properties.ArenaType;
import net.holefloor.arena.tab.HoleFloorTab;
import net.holefloor.arena.timer.ArenaScheduler;
import net.holefloor.arena.timer.lifetime.ArenaLifeTimeMap;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Biome;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public final class ArenaManager {
    public List<Arena> arenas = new ArrayList<>();

    public Arena create(String id, Integer min, Integer max, Location lobby) {
        Arena arena = new Arena();
        arena.instance = HoleFloor.getInstance();
        ArenaProperties properties = new ArenaProperties();
        properties.id = id;
        properties.type = ArenaType.values()[new Random().nextInt(ArenaType.values().length)];
        properties.state = ArenaState.values()[0];
        properties.lobby = lobby;
        properties.world = new WorldCreator(properties.id).generator(new ChunkGenerator(){
            @NotNull
            @Override
            public ChunkData generateChunkData(@NotNull World world, @NotNull Random random, int x, int z, @NotNull ChunkGenerator.BiomeGrid biome) {
                biome.setBiome(x, z, Biome.PLAINS);
                return super.generateChunkData(world, random, x, z, biome);
            }
        }).createWorld();
        properties.world.setGameRule(GameRule.DO_ENTITY_DROPS, Boolean.FALSE);

        ArenaCitizens citizens = new ArenaCitizens();
        citizens.min = min;
        citizens.max = max;
        citizens.players = new ArrayList<>();
        citizens.spectators = new ArrayList<>();
        properties.citizens = citizens;
        arena.properties = properties;

        arena.scheduler = new ArenaScheduler(arena);
        arena.scheduler.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(HoleFloor.getInstance(), () -> {
            arena.scheduler.run();
            arena.scheduler.tick++;
        },1L,1L);

        this.loadMap(arena, arena.properties.type);
        this.arenas.add(arena);
        return arena;
    }

    public Arena load() {
        //data load instance
        return null;
    }

    public void launch(Arena arena) {
        arena.properties.state = ArenaState.PLAYING;
        arena.properties.citizens.players.clear();                            //        delete after
        arena.properties.citizens.players.add(Bukkit.getPlayer("lolzworker"));///////// delete after
        HashMap<Player, ArenaLifeTimeMap> hashMap = new HashMap<>();
        arena.properties.citizens.players.forEach(player -> {
            ArenaLifeTimeMap map = new ArenaLifeTimeMap();
            map.bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SEGMENTED_20);
            map.lifetime = 500;
            map.lives = 5;
            map.isDead = false;
            map.isEliminated = false;
            map.respawnTime = 10;
            hashMap.put(player, map);
            Objects.requireNonNull(
                    player.getAttribute(Attribute.GENERIC_MAX_HEALTH))
                    .setBaseValue(map.lives * 2);
            player.setHealth(map.lives * 2);

            player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1, 0);
            player.setGameMode(GameMode.ADVENTURE);

            Location location = arena.properties.type.getSpawns().get(new Random().nextInt(arena.properties.type.getSpawns().size()));
            location.setWorld(arena.properties.world);
            player.teleport(location);
        });
        arena.scheduler.timerLifetime.hashMap = hashMap;
    }

    public void end(Arena arena) {
        arena.scheduler.timerLifetime.cancel();
        arena.properties.state = ArenaState.ENDING;
    }

    public void stop(Arena arena) {
        try {
            arena.scheduler.timerWait.cancel();
            arena.scheduler.timerStart.cancel();
            arena.scheduler.timerEnd.cancel();
            arena.scheduler.timerLifetime.cancel();
        } catch (Exception ignored){}
    }

    public void connect(Arena arena, Player player) {
        arena.properties.citizens.players.add(player);
        arena.properties.citizens.players.forEach(consumer -> {
            consumer.sendMessage(Objects.requireNonNull(arena.instance.locale.getString("lobby.message.connect"))
                    .replace("%player%", player.getName()));
            consumer.playSound(consumer.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
        });
        player.teleport(arena.properties.lobby);
    }
    public void disconnect(Arena arena, Player player) {
        arena.properties.citizens.players.remove(player);
        arena.properties.citizens.players.forEach(consumer -> {
            consumer.sendMessage(Objects.requireNonNull(arena.instance.locale.getString("lobby.message.disconnect"))
                    .replace("%player%", player.getName()));
            consumer.playSound(consumer.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);
        });
    }

    public void loadMap(Arena arena, ArenaType type) {
        StructureBlockLibApi.INSTANCE
                .loadStructure(HoleFloor.getInstance())
                .at(new Location(Bukkit.getWorld(String.valueOf(arena.properties.id)), 0, 100, 0))
                .loadFromFile(new File(HoleFloor.getInstance().getDataFolder() + "/maps/" + type.name().toLowerCase() + ".nbt"));
    }
}
