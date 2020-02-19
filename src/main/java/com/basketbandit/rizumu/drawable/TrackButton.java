package com.basketbandit.rizumu.drawable;

import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;

public class TrackButton extends Button {
    private Track track;
    private Beatmap beatmap;

    public TrackButton(int x, int y, int width, int height, Track track, Beatmap beatmap) {
        super(x, y, width, height);
        this.track = track;
        this.beatmap = beatmap;
    }

    public Track getTrack() {
        return track;
    }

    public Beatmap getBeatmap() {
        return beatmap;
    }
}
