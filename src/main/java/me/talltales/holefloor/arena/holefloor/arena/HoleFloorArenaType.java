package me.talltales.holefloor.arena.holefloor.arena;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public enum HoleFloorArenaType {
    ICE(Material.ICE, List.of(
            new Location(null, 19, 113, 22, 155, 0),
            new Location(null,19, 113, 22, 40, 0),
            new Location(null, 4, 110, 15, -70, 0))),
    DESERT(Material.SANDSTONE_STAIRS, List.of(new Location(null, 0, 0, 0)));

    private final Material icon;
    private final List<Location> locations;
    HoleFloorArenaType(Material icon, List<Location> locations) {
        this.icon = icon;
        this.locations = locations;
    }

    public Material getIcon() {
        return icon;
    }

    public List<Location> getLocations() {
        return locations;
    }
}
