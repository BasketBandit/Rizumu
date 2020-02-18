package com.basketbandit.rizumu.stage.object;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.drawable.CursorContainer;
import com.basketbandit.rizumu.input.MouseInput;

import java.awt.*;

public class DefaultBackgroundTickObject implements TickObject {
    private int x = 0;

    @Override
    public void tick() {
        // update mouse position every tick (used for cursor selection)
        MouseInput.updatePosition(Rizumu.getFrame().getContentPane().getLocationOnScreen(), MouseInfo.getPointerInfo().getLocation());

        // update mouse cursor to be default (loop eliminates cursor flickering associated with rapid updates)
        if(x++ > 2) {
            x = 0;
            Rizumu.getFrame().setCursor(CursorContainer.DEFAULT_CURSOR);
        }
    }
}
