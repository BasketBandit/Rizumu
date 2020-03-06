package com.basketbandit.rizumu.drawable.track;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.utility.Colours;

import java.awt.*;

public class AccuracyLabel extends Rectangle {
    Color color = Colours.TRANSPARENT;
    float textOpacity = 0.0f;
    String text = "";

    public AccuracyLabel() {
        super(Configuration.getDefaultBeatmapXPosition(), Configuration.getDefaultRegistrarYFromBottom() - 150, Configuration.getWidth(), 50);
    }

    public AccuracyLabel setState(String text, Color color) {
        this.text = text;
        this.color = color;
        this.textOpacity = 1.0f;
        return this;
    }

    public String getText() {
        return text;
    }

    public Color getColor() {
        return color;
    }

    public float getOpacity() {
        return textOpacity;
    }

    public void decrementOpacity() {
        textOpacity += (textOpacity > 0) ? (textOpacity < 0.075) ? -textOpacity : -0.075f : 0;
    }
}
