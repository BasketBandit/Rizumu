package com.basketbandit.rizumu.stage.object;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.drawable.CursorContainer;
import com.basketbandit.rizumu.input.MouseInput;

public class DefaultBackgroundTickObject implements TickObject {
    private int x, y = 0;

    @Override
    public void tick() {
        // update mouse cursor to be default (loop eliminates cursor flickering associated with rapid updates)
        if(x++ > 5) {
            x = 0;
            Rizumu.getFrame().setCursor(CursorContainer.DEFAULT_CURSOR);
        }

        // resets mouse wheel values to 0 (loop allows for smoother scrolling)
        if(y++ > 40) {
            y = 0;
            MouseInput.resetMouseWheel();
        }
    }
}
