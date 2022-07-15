package net.holefloor.tab;

import net.holefloor.HoleFloor;
import org.bukkit.Bukkit;

public final class HoleFloorTab {

    public void update() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            HoleFloor.getInstance().manager.arenas.forEach(arena -> {
                if (arena.properties.citizens.players.contains(player)) {
                    player.setPlayerListHeader(
                            " ".repeat(100) + "\n" +
                                    "§b§lHOLE IN §f§lTHE FLOOR\n" +
                                    "§r§fYou plaing in arena #" + arena.properties.id + "\n" +
                                    "\n" +
                                    "\n" +
                                    "\n");
                    player.setPlayerListFooter(
                            " ".repeat(100) + "\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "\n" +
                                    "§ewww.spigotmc.com");
                } else {
                    if (arena.properties.lobby.getWorld().equals(player.getWorld())) {

                        player.setPlayerListHeader(
                                " ".repeat(100) + "\n" +
                                        "§b§lHOLE IN §f§lTHE FLOOR\n" +
                                        "§r§fYou standing in lobby\n" +
                                        "§r§fTotal online " + Bukkit.getOnlinePlayers().size() + "\n" +
                                        "\n" +
                                        "\n");
                        player.setPlayerListFooter(
                                " ".repeat(100) + "\n" +
                                        "\n" +
                                        "\n" +
                                        "\n" +
                                        "\n" +
                                        "\n" +
                                        "\n" +
                                        "§ewww.spigotmc.com");
                    }
                }
            });
        });
    }
}
