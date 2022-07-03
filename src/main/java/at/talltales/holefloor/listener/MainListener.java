package at.talltales.holefloor.listener;

import at.talltales.holefloor.arena.Arena;
import at.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class MainListener implements Listener {
    @EventHandler
    private void on(PlayerSwapHandItemsEvent event) {
        if (event.getPlayer().isSneaking()) {
            HoleFloor.getInstance().getManager().connect(HoleFloor.getInstance().getManager().getArenas().get(0), event.getPlayer());
            return;
        }
        Arena arena = HoleFloor.getInstance().getManager().create();
    }

    @EventHandler
    private void ondf(PlayerDropItemEvent event) {
        //HoleFloor.getInstance().getManager().disconnect(HoleFloor.getInstance().getManager().getArenas().get(0), event.getPlayer());
    }
}
