package at.talltales.holefloor.arena;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public enum ArenaType {
    ICE(new Vector(14, 111, 23), new Vector(22, 111, 13)),
    DESERT(new Vector(14, 111, 23), new Vector(22, 111, 13));

    private final Vector[] vectors;
    ArenaType(Vector... vectors) {
        this.vectors = vectors;
    }

    public Vector[] getVectors() {
        return vectors;
    }
}
