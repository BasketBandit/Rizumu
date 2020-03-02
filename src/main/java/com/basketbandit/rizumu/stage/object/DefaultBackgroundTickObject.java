package com.basketbandit.rizumu.stage.object;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.utility.Cursors;

public class DefaultBackgroundTickObject implements TickObject {
    private int x = 0;

    @Override
    public void tick() {
        // update mouse cursor to be default (loop eliminates cursor flickering associated with rapid updates)
        if(x++ > 5) {
            x = 0;
            Rizumu.getFrame().setCursor(Cursors.DEFAULT_CURSOR);
        }

        for(Button button: (Rizumu.getSecondaryScene() == null) ? Rizumu.getPrimaryScene().getButtons().values() : Rizumu.getSecondaryScene().getButtons().values()) {
            if(button.isHovered()) {
                Rizumu.getFrame().setCursor(Cursors.HAND_CURSOR);
                break;
            }
        }
    }
}
