package net.holefloor.arena.powerup;

public enum ArenaBoosterType {
    SPEED(ArenaBoosterRate.COMMON),
    JUMP(ArenaBoosterRate.RARE),
    BOW(ArenaBoosterRate.RARE),
    EXPLORER(ArenaBoosterRate.UNIQUE),
    LEVITATION(ArenaBoosterRate.UNIQUE),
    INVISIBLE(ArenaBoosterRate.LEGENDARY);

    enum ArenaBoosterRate {
        COMMON,
        RARE,
        UNIQUE,
        LEGENDARY,
    }

    private final ArenaBoosterRate rate;
    ArenaBoosterType(ArenaBoosterRate rate) {
        this.rate = rate;
    }

    public ArenaBoosterRate getRate() {
        return rate;
    }
}
