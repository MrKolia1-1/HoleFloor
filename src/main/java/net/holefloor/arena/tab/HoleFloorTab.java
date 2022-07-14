package net.holefloor.arena.tab;

import net.holefloor.HoleFloor;
import org.bukkit.Bukkit;

public final class HoleFloorTab {

    public HoleFloorTab() {
    }

    public void update() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            HoleFloor.getInstance().manager.arenas.forEach(arena -> {
                if (arena.properties.citizens.players.contains(player)) {
                    player.setPlayerListHeader(
                            """                                                                                     
                                                                                                                                                                    \n
                            §b§lHOLE IN §f§lTHE FLOOR
                            §r§fPlaying in arena §a#%id%
                            §r§fPlayers §a%count%
                            """
                                    .replace("%id%", arena.properties.id)
                                    .replace("%count%", String.valueOf(arena.properties.citizens.players.size()))
                    );
                }
                else {
                    player.setPlayerListHeader(
                            """                                                                                     
                                                                                                                                                                    \n
                            §b§lHOLE IN §f§lTHE FLOOR
                            §r§fIn lobby
                            
                            """
                    );
                }
            });
            player.setPlayerListFooter(
                    """
                    §ewww.spigotmc.com
                    """
            );
        });
    }
}
