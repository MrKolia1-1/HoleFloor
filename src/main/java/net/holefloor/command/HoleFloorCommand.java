package net.holefloor.command;

import net.holefloor.HoleFloor;
import net.holefloor.arena.properties.ArenaState;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class HoleFloorCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(HoleFloor.getInstance().locale.getString("command.help"));
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "help" : {
                sender.sendMessage(HoleFloor.getInstance().locale.getString("command.help"));
                return true;
            }
            case "create" : {
                if (args.length == 1 || args.length == 2 || args.length == 3 || args.length == 4 || args.length > 5) {
                    sender.sendMessage("§b○ §f/holefloor create <name> <min> <max> <lobby-world>");
                    return true;
                }
                if (args.length == 5) {
                    String name = args[1];
                    Integer min;
                    Integer max;
                    try {
                        min = Integer.valueOf(args[2]);
                        max = Integer.valueOf(args[3]);
                    } catch (NumberFormatException numberFormatException) {
                        sender.sendMessage(HoleFloor.getInstance().locale.getString("command.invalid"));
                        return true;
                    }
                    sender.sendMessage(HoleFloor.getInstance().locale.getString("command.created")
                            .replace("%name%", name)
                            .replace("%min%", String.valueOf(min))
                            .replace("%max%", String.valueOf(max))
                    );
                    HoleFloor.getInstance().manager.create(name, min, max, new WorldCreator(args[4]).createWorld().getSpawnLocation());
                    return true;
                }
                return true;
            }
            case "delete" : {
                if (args.length == 1) {
                    sender.sendMessage("§b○ §f/holefloor delete <name>");
                    return true;
                }
                if (args.length == 2) {
                    HoleFloor.getInstance().manager.arenas.forEach(arena -> {
                        if (arena.properties.id.equalsIgnoreCase(args[1])) {
                            sender.sendMessage(HoleFloor.getInstance().locale.getString("command.deleted")
                                    .replace("%name%", args[1])
                            );
                            arena.properties.citizens.players.forEach(player -> {
                                player.teleport(arena.properties.lobby);
                            });
                            try {
                                HoleFloor.getInstance().manager.stop(arena);
                            } catch (Exception ignored) {}
                        }
                    });
                    return true;
                }
            }
            case "join" : {
                if (args.length == 1) {
                    sender.sendMessage("§b○ §f/holefloor join <name> [player]");
                    return true;
                }
                if (args.length == 2) {
                    HoleFloor.getInstance().manager.arenas.forEach(arena -> {
                        if (arena.properties.id.equalsIgnoreCase(args[1])) {
                            HoleFloor.getInstance().manager.connect(arena, (Player) sender);
                        }
                        else {
                            sender.sendMessage("§b○ §f/holefloor join <name> [player]");
                        }
                    });
                    return true;
                }
                if (args.length == 3) {
                    HoleFloor.getInstance().manager.arenas.forEach(arena -> {
                        if (arena.properties.id.equalsIgnoreCase(args[1])) {
                            HoleFloor.getInstance().manager.connect(arena, Bukkit.getPlayer(args[2]));
                        }
                        else {
                            sender.sendMessage("§b○ §f/holefloor join <name> [player]");
                        }
                    });
                    return true;
                }
                return true;
            }
            case "leave" : {
                if (args.length == 1) {
                    sender.sendMessage("§b○ §f/holefloor leave <name> [player]");
                    return true;
                }
                if (args.length == 2) {
                    HoleFloor.getInstance().manager.arenas.forEach(arena -> {
                        if (arena.properties.id.equalsIgnoreCase(args[1])) {
                            HoleFloor.getInstance().manager.disconnect(arena, (Player) sender);
                        }
                        else {
                            sender.sendMessage("§b○ §f/holefloor leave <name> [player]");
                        }
                    });
                    return true;
                }
                if (args.length == 3) {
                    HoleFloor.getInstance().manager.arenas.forEach(arena -> {
                        if (arena.properties.id.equalsIgnoreCase(args[1])) {
                            HoleFloor.getInstance().manager.disconnect(arena, Bukkit.getPlayer(args[2]));
                        }
                        else {
                            sender.sendMessage("§b○ §f/holefloor leave <name> [player]");
                        }
                    });
                    return true;
                }
                return true;
            }
        }

        sender.sendMessage(HoleFloor.getInstance().locale.getString("command.unknown"));
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
