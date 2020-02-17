package com.basketbandit.rizumu.scheduler.jobs;

import com.basketbandit.rizumu.stage.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.Job;
import com.basketbandit.rizumu.scheduler.tasks.BeatmapEndTask;

import java.util.concurrent.TimeUnit;

public class BeatmapEndJob extends Job {
    private final BeatmapEndTask beatmapEndTask;

    public BeatmapEndJob(TrackScene scene, long delay) {
        super(delay + 6000, 0, TimeUnit.MILLISECONDS);
        this.beatmapEndTask = new BeatmapEndTask(scene);
    }

    @Override
    public void run() {
        handleTask(beatmapEndTask);
    }
}