package net.holefloor.arena.timer.lobby;

import net.holefloor.HoleFloor;
import net.holefloor.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.Objects;

public final class ArenaTimerStart {
    private final Arena arena;
    private final BossBar bossBar;
    private Integer seconds;

    public ArenaTimerStart(Arena arena) {
        this.arena = arena;
        this.seconds = 10;
        this.bossBar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SEGMENTED_6);
    }

    public void run() {
        this.arena.properties.citizens.players.forEach(player -> {
            if (!this.bossBar.getPlayers().contains(player)) {
                this.bossBar.addPlayer(player);
            }
        });

        this.bossBar.setTitle(Objects.requireNonNull(this.arena.instance.locale.getString("lobby.bossbar.start"))
                .replace("%seconds%", String.valueOf(this.seconds)));

        if (this.arena.scheduler.tick % 20 == 0) {
            if (this.seconds == 0) {
                HoleFloor.getInstance().manager.launch(arena);
                this.cancel();
                return;
            }
            this.arena.properties.citizens.players.forEach(player -> {
                if (this.arena.scheduler.tick % 40 == 0) {
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
                }
            });
            this.seconds--;
        }
    }

    public void cancel() {
        this.bossBar.removeAll();
        this.seconds = 10;
    }
}
