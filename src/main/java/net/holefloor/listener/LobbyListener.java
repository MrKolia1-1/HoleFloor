package net.holefloor.listener;

import net.holefloor.HoleFloor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class LobbyListener implements Listener {
    @EventHandler
    private void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();

        HoleFloor.getInstance().manager.arenas.forEach(arena -> {
            if (arena.properties.lobby.getWorld().equals(world)) {
                event.setCancelled(true);
            }
        });
    }
    @EventHandler
    private void onEntityDamageEvent(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();

        HoleFloor.getInstance().manager.arenas.forEach(arena -> {
            if (arena.properties.lobby.getWorld().equals(world)) {
                event.setCancelled(true);
            }
        });
    }
}
