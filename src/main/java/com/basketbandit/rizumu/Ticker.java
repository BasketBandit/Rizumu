package com.basketbandit.rizumu;

import com.basketbandit.rizumu.scene.TickObject;

public class Ticker {
    private TickObject backgroundTickObject;
    private TickObject primaryTickObject;
    private TickObject secondaryTickObject;

    void setBackgroundTickObject(TickObject tickObject) {
        this.backgroundTickObject = tickObject;
    }

    void setPrimaryTickObject(TickObject tickObject) {
        this.primaryTickObject = tickObject;
    }

    void setSecondaryTickObject(TickObject tickObject) {
        this.secondaryTickObject = tickObject;
    }

    void tick() {
        if(backgroundTickObject != null) {
            backgroundTickObject.tick();
        }

        if(primaryTickObject != null) {
            primaryTickObject.tick();
        }

        if(secondaryTickObject != null) {
            secondaryTickObject.tick();
        }
    }
}
