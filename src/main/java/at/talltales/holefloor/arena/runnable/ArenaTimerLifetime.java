package at.talltales.holefloor.arena.runnable;

import at.talltales.holefloor.arena.Arena;
import at.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ArenaTimerLifetime extends BukkitRunnable {
    private final Arena arena;
    private final HashMap<Player, ArenaLifeTimeMap> map;

    public ArenaTimerLifetime(Arena arena, HashMap<Player, ArenaLifeTimeMap> map) {
        this.arena = arena;
        this.map = map;
    }

    @Override
    public void run() {
        this.map.forEach((player, map) -> {
            if (!map.isDeath) {
                if (map.lifetime <= 0) {
                    HoleFloor.getInstance().getManager().deathPlayer(this.arena, player);
                }
                if (map.lifetime >= 70 && map.lifetime <= 100) {
                    map.bossBar.setColor(BarColor.GREEN);
                }
                if (map.lifetime > 40 && map.lifetime < 70) {
                    map.bossBar.setColor(BarColor.YELLOW);
                }
                if (map.lifetime >= 0 && map.lifetime <= 40) {
                    map.bossBar.setColor(BarColor.RED);
                }
                String bossbarTitle = HoleFloor.getInstance().getLocale().getString("arena.bossbar.timer.lifetime")
                        .replace("{0}", String.valueOf(map.lives));
                map.bossBar.setProgress(map.lifetime / 100D);
                map.bossBar.setTitle(bossbarTitle);
                map.lifetime--;
            }
        });
    }

    public static class ArenaLifeTimeMap {
        public BossBar bossBar;
        public int lives;
        public int lifetime;
        public boolean isDeath;
    }
}
