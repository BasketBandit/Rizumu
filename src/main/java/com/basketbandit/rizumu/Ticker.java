package com.basketbandit.rizumu;

import com.basketbandit.rizumu.scene.TickObject;

public class Ticker {
    private SystemConfiguration sys;
    private TickObject tickObject;

    Ticker(SystemConfiguration sys) {
        this.sys = sys;
    }

    public void setTickObject(TickObject tickObject) {
        this.tickObject = tickObject;
    }

    public void tick() {
        tickObject.tick();
    }
}
