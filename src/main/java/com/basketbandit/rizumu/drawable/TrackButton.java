package com.basketbandit.rizumu.drawable;

import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;

public class TrackButton extends Button {
    private int id;
    private Track track;
    private Beatmap beatmap;

    public TrackButton(int x, int y, int width, int height, int id, Track track, Beatmap beatmap) {
        super(x, y, width, height);
        this.id = id;
        this.track = track;
        this.beatmap = beatmap;
    }

    public int getId() {
        return id;
    }

    public Track getTrack() {
        return track;
    }

    public Beatmap getBeatmap() {
        return beatmap;
    }
}
