package net.holefloor.scoreboard;

import net.holefloor.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

public final class HoleFloorBoard {
    public Scoreboard scoreboard;
    public void update() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            HoleFloor.getInstance().manager.arenas.forEach(arena -> {
                if (arena.properties.citizens.players.contains(player)) {
                    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                    final Objective objective = scoreboard.registerNewObjective("test", "dummy");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    objective.setDisplayName("§b§lHOLE IN §f§lTHE FLOOR");
                    Score score = objective.getScore(" ");
                    score.setScore(10);
                    Score score1 = objective.getScore("  ");
                    score1.setScore(9);
                    Score score2 = objective.getScore("§fArena §a#" + arena.properties.id);
                    score2.setScore(8);
                    Score score3 = objective.getScore("§fPlayers §a" + arena.properties.citizens.players.size());
                    score3.setScore(7);
                    Score score4 = objective.getScore("    ");
                    score4.setScore(6);
                    Score score5 = objective.getScore("     ");
                    score5.setScore(5);
                    Score score6 = objective.getScore("§fMap random: §6" + arena.properties.type.name().toLowerCase());
                    score6.setScore(4);
                    Score score7 = objective.getScore("       ");
                    score7.setScore(3);
                    Score score8 = objective.getScore("§ewww.spigotmc.com ");
                    score8.setScore(2);
                    player.setScoreboard(scoreboard);
                }
            });
        });
    }
}
