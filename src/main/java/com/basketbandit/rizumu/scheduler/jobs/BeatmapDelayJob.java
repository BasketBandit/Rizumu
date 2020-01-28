package com.basketbandit.rizumu.scheduler.jobs;

import com.basketbandit.rizumu.beatmap.Beatmap;
import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.Job;
import com.basketbandit.rizumu.scheduler.tasks.BeatmapDelayTask;

import java.util.concurrent.TimeUnit;

public class BeatmapDelayJob extends Job {
    private final BeatmapDelayTask beatmapDelayTask;

    public BeatmapDelayJob(Beatmap beatmap, TrackScene scene) {
        super(beatmap.getStartDelay(), 0, TimeUnit.MILLISECONDS);
        this.beatmapDelayTask = new BeatmapDelayTask(beatmap, scene);
    }

    @Override
    public void run() {
        handleTask(beatmapDelayTask);
    }
}
