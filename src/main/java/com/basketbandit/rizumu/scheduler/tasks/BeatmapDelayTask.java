package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.beatmap.Beatmap;

import com.basketbandit.rizumu.beatmap.NoteLoader;
import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.Task;

public class BeatmapDelayTask implements Task {
    private final Beatmap beatmap;
    private final TrackScene scene;

    public BeatmapDelayTask(Beatmap beatmap, TrackScene scene) {
        this.beatmap = beatmap;
        this.scene = scene;
    }

    @Override
    public void run() {
        new NoteLoader(beatmap, scene);
    }
}
