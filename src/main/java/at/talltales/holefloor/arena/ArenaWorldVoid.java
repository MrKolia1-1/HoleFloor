package at.talltales.holefloor.arena;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.Random;

public class ArenaWorldVoid extends ChunkGenerator {
    public ArenaWorldVoid() {
        super();
    }

    @Override
    public void generateNoise(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkGenerator.ChunkData chunkData) {
    }
}
