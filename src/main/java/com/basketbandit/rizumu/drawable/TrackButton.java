package com.basketbandit.rizumu.drawable;

public class TrackButton extends Button {
    private String track;
    private String beatmap;

    public TrackButton(int x, int y, int width, int height, String track, String beatmap) {
        super(x, y, width, height);
        this.track = track;
        this.beatmap = beatmap;
    }

    public String getTrack() {
        return track;
    }

    public String getBeatmap() {
        return beatmap;
    }
}
