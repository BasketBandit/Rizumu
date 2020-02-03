package com.basketbandit.rizumu;

import com.basketbandit.rizumu.scene.TickObject;

public class Ticker {
    private TickObject tickObject;

    public void setTickObject(TickObject tickObject) {
        this.tickObject = tickObject;
    }

    public void tick() {
        tickObject.tick();
    }
}
