package me.talltales.holefloor.listeners;

import me.talltales.holefloor.arena.HoleFloorArena;
import me.talltales.holefloor.arena.HoleFloorManager;
import me.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public final class MainListener implements Listener {

    @EventHandler
    private void on(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        HoleFloorArena arena = HoleFloor.getInstance().getManager().createNewArena();
        HoleFloor.getInstance().getManager().connectPlayer(arena, player);
    }

    @EventHandler
    private void on(PlayerJoinEvent event) {
        HoleFloor.getInstance().getManager().connectPlayer(
                HoleFloor.getInstance().getManager().getArenas().get(0), event.getPlayer());
    }
    @EventHandler
    private void on(PlayerQuitEvent event) {
        HoleFloor.getInstance().getManager().disconnectPlayer(
                HoleFloor.getInstance().getManager().getArenas().get(0), event.getPlayer());
    }
}
