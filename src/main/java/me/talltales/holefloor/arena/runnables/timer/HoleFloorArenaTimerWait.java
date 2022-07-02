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

public final class HoleFloorArenaTimerWait {
    private BossBar bossBar;
    private final Set<Integer> ids;
    private final HoleFloorArena arena;

    public HoleFloorArenaTimerWait(HoleFloorArena arena) {
        this.arena = arena;
        this.ids = new HashSet<>();
    }

    public void run() {
        this.bossBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_6);
        AtomicInteger i = new AtomicInteger();
        ids.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(HoleFloor.getInstance(), () -> {
            this.arena.players.forEach(this.bossBar::addPlayer);

            AtomicReference<String> title = new AtomicReference<>(HoleFloor.getInstance().getLocale().getString("arena.timer.wait"));
            title.set(title.get().replace("{0}", String.valueOf(this.arena.players.size())));
            title.set(title.get().replace("{1}", "5"));

            if (i.get()  == 3) {
                this.bossBar.setTitle(title.get());
                i.set(0);
            }
            else {
                i.getAndIncrement();
                this.bossBar.setTitle(title + ".".repeat(i.get()));
            }
        },0,20));
    }

    public void cancel() {
        this.ids.forEach(Bukkit.getScheduler()::cancelTask);
        this.bossBar.removeAll();
    }
}
