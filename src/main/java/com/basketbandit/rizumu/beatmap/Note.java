package com.basketbandit.rizumu.beatmap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.awt.*;
import java.awt.event.KeyEvent;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "time",
        "key_num",
        "note_type"
})
public class Note extends Rectangle {
    @JsonProperty("time")
    private int time;
    @JsonProperty("key_num")
    private int keyNum;

    // single, single_long
    @JsonProperty("note_type")
    private String noteType;

    private boolean hit;
    private int key;
    private Color color;

    public Note() {
        super(0, -23, 50, 23);
    }

    void setColor(int num) {
        switch(num) {
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

    public Color getColor() {
        return color;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean hit() { return hit; }

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
}
