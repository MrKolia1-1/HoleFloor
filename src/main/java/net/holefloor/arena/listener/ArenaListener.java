package net.holefloor.arena.listener;

import net.holefloor.HoleFloor;
import net.holefloor.arena.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public final class ArenaListener implements Listener {
    @EventHandler
    private void on(PlayerSwapHandItemsEvent event) {
        Arena arena = HoleFloor.getInstance().manager.arenas.get(0);
        if (event.getPlayer().isSneaking()) {
            HoleFloor.getInstance().manager.disconnect(arena, event.getPlayer());
            return;
        }
        HoleFloor.getInstance().manager.connect(arena, event.getPlayer());
    }

    @EventHandler
    private void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (HoleFloor.getInstance().getConfig().getBoolean("bungee-cord")) {
            HoleFloor.getInstance().manager.arenas.forEach(arena -> {
                if (arena.properties.id.equals(HoleFloor.getInstance().getConfig().getString("bungee-cord-arena"))) {
                    HoleFloor.getInstance().manager.connect(arena, player);
                }
            });
        }
    }
}
