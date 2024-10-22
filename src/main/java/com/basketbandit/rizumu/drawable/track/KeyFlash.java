package com.basketbandit.rizumu.drawable.track;


import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.media.Image;

public class KeyFlash {
    private java.awt.Image image = Image.getBufferedImage("hitflash-body").getScaledInstance(Configuration.getDefaultNoteWidth()+1, 150, 0); // +1 to width to plug up any 1/2 pixel gaps
    private int key;
    private float opacity = 0.1f;

    public KeyFlash(int key) {
        this.key = key;
    }

    public java.awt.Image getImage() {
        return image;
    }

    public int getKey() {
        return key;
    }

    public float getOpacity() {
        return opacity;
    }

    public void resetOpacity() {
        opacity = 1;
    }

    public void decrementOpacity() {
        opacity += (opacity > 0.1f) ? -0.1f : 0;
    }
}
