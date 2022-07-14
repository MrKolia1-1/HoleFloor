package net.holefloor.arena.properties;

import org.bukkit.Location;

import java.util.List;

public enum ArenaType {
    ICE(List.of(
            new Location(null, 30, 109, 4, 50, 0),
            new Location(null, 13, 111, 24, -150, 0),
            new Location(null, 8, 111, 16, -70, 0))),

    DESERT(List.of(
            new Location(null, 3, 110, 19, -90, 0),
            new Location(null, 14, 110, 3, 0, 0),
            new Location(null, 24, 109, 16, 90, 0))),

    STRONGHOLD(List.of(
            new Location(null, 16, 113, 22, 180, 0),
            new Location(null, 15, 133, 9, 0, 0),
            new Location(null, 2, 105, 15, -90, 0))),

    END(List.of(
            new Location(null, 10, 116, 25, 150, 0),
            new Location(null, 22, 110, 10, 30, 0),
            new Location(null, 27, 114, 17, 100, 0)));

    final List<Location> spawns;
    ArenaType(List<Location> spawns) {
        this.spawns = spawns;
    }

    public List<Location> getSpawns() {
        return spawns;
    }
}
