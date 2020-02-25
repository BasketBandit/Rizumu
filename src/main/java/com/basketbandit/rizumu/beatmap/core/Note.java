package com.basketbandit.rizumu.beatmap.core;

import com.basketbandit.rizumu.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.awt.*;

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
    private String noteType = "";
    @JsonProperty("note_length")
    private int noteLength;
    private boolean hit;
    private boolean held;
    private int key;
    private Image[] noteImage;
    private Color color;

    public Note() {
        super(0, -Configuration.getDefaultNoteHeight(), Configuration.getDefaultNoteWidth(), Configuration.getDefaultNoteHeight());
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

    public void setKeyNum(int keyNum) {
        this.keyNum = keyNum;
    }

    @JsonProperty("note_type")
    public String getNoteType() {
        return noteType;
    }

    @JsonProperty("note_length")
    public int getNoteLength() {
        return noteLength;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setHit() {
        this.hit = true;
    }

    public boolean hit() { return hit; }

    public void setHeld() {
        this.held = true;
    }

    public boolean isHeld() {
        return held;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void initImages(Image... image) {
        noteImage = new Image[noteType.equals("single_long") ? 2 : 1];
        noteImage[0] = image[0];
        if(noteType.equals("single_long")) {
            noteImage[1] = image[1].getScaledInstance(width - 6, height, 0);
        }
    }

    public Image getHead() {
        return noteImage[0];
    }

    public Image getBody() {
        return noteImage.length > 1 ? noteImage[1] : noteImage[0];
    }
}
