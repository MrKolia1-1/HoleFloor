package net.holefloor.arena.timer.lobby;

import net.holefloor.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.Objects;

public final class ArenaTimerWait {
    private final Arena arena;
    private final BossBar bossBar;
    private Integer point;

    public ArenaTimerWait(Arena arena) {
        this.arena = arena;
        this.point = 0;
        this.bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_6);
    }

    public void run() {
        this.arena.properties.citizens.players.forEach(player -> {
            if (!this.bossBar.getPlayers().contains(player)) {
                this.bossBar.addPlayer(player);
            }
        });

        this.bossBar.setTitle(Objects.requireNonNull(this.arena.instance.locale.getString("lobby.bossbar.wait"))
                .replace("%min%", String.valueOf(this.arena.properties.citizens.players.size()))
                .replace("%max%", String.valueOf(this.arena.properties.citizens.min)) + (this.point == 0 ? " " : ".".repeat(this.point)));

        if (this.arena.scheduler.tick % 20 == 0) {
            if (this.point == 3) {
                this.point = 0;
                return;
            }
            this.point++;
        }
    }

    public void cancel() {
        this.bossBar.removeAll();
        this.point = 0;
    }
}
