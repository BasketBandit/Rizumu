package com.basketbandit.rizumu.beatmap.core;

import com.basketbandit.rizumu.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.awt.*;
import java.awt.event.KeyEvent;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "key_num",
        "note_type",
        "note_length"
})
public class Note extends Rectangle {
    @JsonProperty("time")
    private int time;
    @JsonProperty("key_num")
    private int keyNum;
    @JsonProperty("note_type") // single, single_long
    private String noteType;
    @JsonProperty("note_length")
    private int noteLength;

    private boolean hit;
    private boolean held;
    private int key;
    private Color color;

    public Note() {
        super(0, -26, 50, 25);
    }

    public void initNote(int keyNum, int keyCount) {
        int notePosition = (Configuration.getContentWidth()/2) - (50*keyCount/2);

        switch(keyNum) {
            case 0:
                this.x = notePosition;
                this.key = KeyEvent.VK_Q;
                this.color = Color.GREEN;
                break;
            case 1:
                this.x = notePosition+50;
                this.key = KeyEvent.VK_W;
                this.color = Color.RED;
                break;
            case 2:
                this.x = notePosition+100;
                this.key = KeyEvent.VK_E;
                this.color = Color.YELLOW;
                break;
            case 3:
                this.x = notePosition+150;
                this.key = KeyEvent.VK_R;
                this.color = Color.BLUE;
                break;
            case 4:
                this.x = notePosition+200;
                this.key = KeyEvent.VK_T;
                this.color = Color.ORANGE;
                break;
            case 5:
                this.x = notePosition+250;
                this.key = KeyEvent.VK_Y;
                this.color = Color.PINK;
                break;
            case 6:
                this.x = notePosition+300;
                this.key = KeyEvent.VK_U;
                this.color = Color.MAGENTA;
                break;
        }

        if(noteType.equals("single_long")) {
            // The calculation of note length is based on time (not sure if that's correct!) (will need to be modified for half second long notes!)
            // note length / 1000 to get number of seconds, times that by the speed multiplier to get the total number of note lengths are needed (-1 to account for a single note size)
            this.y = -25*((noteLength/1000)* Configuration.getSpeedMultiplier()-1);
            this.height = 25*((noteLength/1000)* Configuration.getSpeedMultiplier()-1);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setHit() {
        this.hit = true;
    }

    public boolean hit() { return hit; }

    public void setHeld() {
        this.held = true;
    }

    public boolean wasHeld() {
        return held;
    }

    public int getKey() {
        return key;
    }

    @JsonProperty("time")
    public int getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(int time) {
        this.time = time;
    }

    @JsonProperty("key_num")
    public int getKeyNum() {
        return keyNum;
    }

    @JsonProperty("note_type")
    public String getNoteType() {
        return noteType;
    }

    @JsonProperty("note_length")
    public int getNoteLength() {
        return noteLength;
    }
}
