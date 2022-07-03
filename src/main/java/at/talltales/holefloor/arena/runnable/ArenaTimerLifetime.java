package at.talltales.holefloor.arena.runnable;

import at.talltales.holefloor.arena.Arena;
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
                    this.arena.deathPlayer(player);
                    map.bossBar.removeAll();
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
                map.bossBar.setProgress(map.lifetime / 100D);
                map.bossBar.setTitle(String.valueOf(map.lifetime));
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
