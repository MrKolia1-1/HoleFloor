package net.holefloor.arena.timer.lobby;

import net.holefloor.arena.Arena;
import net.holefloor.arena.properties.ArenaState;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Objects;

public final class ArenaTimerEnd {
    private final Arena arena;
    private final BossBar bossBar;
    private Integer seconds;

    public ArenaTimerEnd(Arena arena) {
        this.arena = arena;
        this.seconds = 20;
        this.bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_6);
    }

    public void run() {
        this.arena.properties.citizens.players.forEach(player -> {
            if (!this.bossBar.getPlayers().contains(player)) {
                this.bossBar.addPlayer(player);
            }
        });

        this.bossBar.setTitle(Objects.requireNonNull(this.arena.instance.locale.getString("lobby.bossbar.end"))
                .replace("%seconds%", String.valueOf(this.seconds)));

        if (this.arena.scheduler.tick % 20 == 0) {
            Player top1 = this.arena.scheduler.timerLifetime.top.get(this.arena.scheduler.timerLifetime.top.size() - 1);
            Firework fw = top1.getWorld().spawn(top1.getLocation(), Firework.class);
            FireworkMeta fwm = fw.getFireworkMeta();
            fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).withColor(Color.YELLOW).trail(true).flicker(true).with(FireworkEffect.Type.BALL_LARGE).withFade(Color.LIME).build());
            fwm.setPower(1);
            fw.setFireworkMeta(fwm);
            if (this.seconds == 0) {
                this.arena.properties.state = ArenaState.WAITING;
                this.arena.properties.citizens.players.forEach(player -> {
                    player.setGameMode(GameMode.ADVENTURE);
                    player.teleport(this.arena.properties.lobby);
                });
                this.cancel();
                return;
            }
            this.seconds--;
        }
    }

    public void cancel() {
        this.bossBar.removeAll();
        this.seconds = 20;
    }
}
