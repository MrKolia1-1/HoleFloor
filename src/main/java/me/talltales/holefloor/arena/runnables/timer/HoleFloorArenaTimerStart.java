package me.talltales.holefloor.arena.runnables.timer;

import me.talltales.holefloor.arena.HoleFloorArena;
import me.talltales.holefloor.plugin.HoleFloor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class HoleFloorArenaTimerStart {
    private BossBar bossBar;
    private final Set<Integer> ids;
    private final HoleFloorArena arena;

    public HoleFloorArenaTimerStart(HoleFloorArena arena) {
        this.arena = arena;
        this.ids = new HashSet<>();
    }

    public void run() {
        this.bossBar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SEGMENTED_12);
        AtomicInteger i = new AtomicInteger(15);
        ids.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(HoleFloor.getInstance(), () -> {
            this.arena.players.forEach(this.bossBar::addPlayer);

            AtomicReference<String> title = new AtomicReference<>(HoleFloor.getInstance().getLocale().getString("arena.timer.start"));
            if (i.get() == -1) {
                i.set(15);
                this.arena.startGame();
                this.cancel();
            } else {
                title.set(title.get().replace("{0}", String.valueOf(i.get())));
                this.bossBar.setTitle(title.get());
                i.getAndDecrement();
            }
        }, 0, 20));
    }

    public void cancel() {
        this.ids.forEach(Bukkit.getScheduler()::cancelTask);
        this.bossBar.removeAll();
    }
}