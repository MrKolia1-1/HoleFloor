package at.talltales.holefloor.arena.runnable;

import at.talltales.holefloor.arena.Arena;
import at.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ArenaTimerRespawn extends BukkitRunnable {
    private final Arena arena;
    private final HashMap<Player, ArenaTimerRespawnMap> map;
    public ArenaTimerRespawn(Arena arena, HashMap<Player, ArenaTimerRespawnMap> map) {
        this.arena = arena;
        this.map = map;
    }

    @Override
    public void run() {
        this.map.forEach((player, map) -> {
            if (map.isDeath) {
                if (!map.bossBar.getPlayers().contains(player)) {
                    map.bossBar.addPlayer(player);
                }
                String bossbarTitle = HoleFloor.getInstance().getLocale().getString("arena.bossbar.timer.start")
                        .replace("{0}", String.valueOf(map.respawnTime)
                        .replace("{1}", String.valueOf(this.arena.lifetimeMap.get(player).lives)));
                Bukkit.getServer().broadcastMessage(String.valueOf(map.respawnTime));
                map.bossBar.setTitle(bossbarTitle);
                map.respawnTime--;
            }
        });
    }

    public static class ArenaTimerRespawnMap {
        public BossBar bossBar;
        public int respawnTime;
        public boolean isDeath;
    }
}
