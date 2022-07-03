package at.talltales.holefloor.arena.runnable;

import at.talltales.holefloor.arena.Arena;
import at.talltales.holefloor.arena.ArenaState;
import at.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.*;

public class ArenaTimerStart extends BukkitRunnable {
    private final Arena arena;
    private final BossBar bossBar;
    private int seconds;

    public ArenaTimerStart(Arena arena) {
        this.arena = arena;
        this.bossBar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SEGMENTED_10);
        this.seconds = 10;
    }

    @Override
    public void run() {
        if (this.arena.state.equals(ArenaState.STARTING)) {
            if (this.arena.players.size() < 2) {
                this.arena.state = ArenaState.WAITING;

                this.arena.timerWait = new ArenaTimerWait(arena);
                this.arena.timerWait.runTaskTimer(HoleFloor.getInstance(), 0, 20);

                this.arena.players.forEach(player -> {
                    String message = HoleFloor.getInstance().getLocale().getString("arena.message.timer.enough-players");
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1.5F);
                    player.sendMessage(message);
                });
                this.bossBar.removeAll();
                this.cancel();
                return;
            }
        }
        this.arena.players.forEach(player -> {
            if (!this.bossBar.getPlayers().contains(player)) {
                this.bossBar.addPlayer(player);
            }
        });
        this.arena.players.forEach(player -> {
            String subtitle = HoleFloor.getInstance().getLocale().getString("arena.title.timer.start").replace("{0}", String.valueOf(seconds));
            String message = HoleFloor.getInstance().getLocale().getString("arena.message.timer.start").replace("{0}", String.valueOf(seconds));
            player.sendTitle(" ", subtitle, 0, 25, 0);
            player.sendMessage(message);
            player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_CLUSTER_HIT, 1, 1.5F);
        });

        String bossbarTitle = HoleFloor.getInstance().getLocale().getString("arena.bossbar.timer.start").replace("{0}", String.valueOf(seconds));
        this.bossBar.setTitle(bossbarTitle);
        if (seconds == 0) {
            this.arena.start();
            this.bossBar.removeAll();
            this.cancel();
        }
        else {
            seconds--;
        }
    }
}