package com.basketbandit.rizumu.drawable;

import com.basketbandit.rizumu.SystemConfiguration;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Note extends Rectangle {
    private boolean isBottom, isRight, hit;
    private int type;
    private int key;
    private Color color;
    private SystemConfiguration sys = new SystemConfiguration();

    public Note() {
    }

    public Note(int i) {
        super((25+50*i), 0, 50, 23);
        this.type = 1;
        setColour(i);
    }

    public Note(int i, int count) {
        super((25+50*i), 0, 50, 0); //(-46*(count-1))+10 | -35
        this.y = (-23*(count-1))-((count*sys.getSpeedMultiplier()));
        this.height = (23*count)+((count*sys.getSpeedMultiplier()));
        this.type = 2;
        setColour(i);
    }

    private void setColour(int i) {
        switch(i) {
            case 0:
                this.key = KeyEvent.VK_Q;
                this.color = Color.GREEN;
                return;
            case 1:
                this.key = KeyEvent.VK_W;
                this.color = Color.RED;
                return;
            case 2:
                this.key = KeyEvent.VK_E;
                this.color = Color.YELLOW;
                return;
            case 3:
                this.key = KeyEvent.VK_R;
                this.color = Color.BLUE;
        }
    }

    public boolean hit() { return hit; }

    public int getKey() {
        return key;
    }

    public Color getColor() {
        return color;
    }

    public int getType() {
        return type;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

}
