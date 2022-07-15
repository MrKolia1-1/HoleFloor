package net.holefloor.arena.listener;

import net.holefloor.arena.Arena;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class ArenaListener implements Listener {
    private final Arena arena;
    public ArenaListener(Arena arena) {
        this.arena = arena;
    }

    @EventHandler
    private void onEntityDamageEvent(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();

        if (this.arena.properties.world.equals(world)) {
            event.setDamage(0);
            if (entity instanceof Player) {
                entity.setVelocity(entity.getVelocity().multiply(2));
            }
        }
    }
    @EventHandler
    private void onEntityDamageEvent(FoodLevelChangeEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();

        if (this.arena.properties.world.equals(world)) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    private void onEntityDamageEvent(ExplosionPrimeEvent event) {
        Entity entity = event.getEntity();
        World world = entity.getWorld();

        if (this.arena.properties.world.equals(world)) {
            event.setRadius(0);
        }
    }
}
