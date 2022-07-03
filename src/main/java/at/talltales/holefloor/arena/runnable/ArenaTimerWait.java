package at.talltales.holefloor.arena.runnable;

import at.talltales.holefloor.arena.Arena;
import at.talltales.holefloor.arena.ArenaState;
import at.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaTimerWait extends BukkitRunnable {
    private final Arena arena;
    private final BossBar bossBar;
    private int dot;

    public ArenaTimerWait(Arena arena) {
        this.arena = arena;
        this.bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
        this.dot = 0;
    }

    @Override
    public void run() {
        if (this.arena.state.equals(ArenaState.WAITING)) {
            if (this.arena.players.size() >= 2) {
                this.arena.state = ArenaState.STARTING;

                this.arena.timerStart = new ArenaTimerStart(arena);
                this.arena.timerStart.runTaskTimer(HoleFloor.getInstance(), 0, 20);

                this.bossBar.removeAll();
                this.cancel();
                return;
            }
        }
        this.arena.players.forEach(player -> {
            if (!this.bossBar.getPlayers().contains(player)) {
                this.bossBar.addPlayer(player);
            }
            String subtitle = HoleFloor.getInstance().getLocale().getString("arena.title.timer.wait");
            player.sendTitle(" ", subtitle, 0, 40, 0);
        });

        String bossbarTitle = HoleFloor.getInstance().getLocale().getString("arena.bossbar.timer.wait")
                .replace("{0}", String.valueOf(this.arena.players.size()))
                .replace("{1}", "5");
        this.bossBar.setTitle(bossbarTitle + ".".repeat(dot));
        if (dot != 3) {
            dot++;
        }
        else {
            dot = 0;
        }
    }
}
