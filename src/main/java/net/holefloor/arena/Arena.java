package net.holefloor.arena;

import net.holefloor.HoleFloor;
import net.holefloor.arena.listener.ArenaListener;
import net.holefloor.arena.powerup.ArenaPowerBooster;
import net.holefloor.arena.properties.ArenaProperties;
import net.holefloor.arena.timer.ArenaScheduler;

public final class Arena {
    public HoleFloor instance;
    public ArenaProperties properties;
    public ArenaScheduler scheduler;
    public ArenaListener listener;
    public ArenaPowerBooster booster;
}
