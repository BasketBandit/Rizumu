package com.basketbandit.rizumu.scheduler.jobs;

import com.basketbandit.rizumu.stage.scene.play.PlayScene;
import com.basketbandit.rizumu.scheduler.Job;
import com.basketbandit.rizumu.scheduler.tasks.BeatmapInitTask;

import java.util.concurrent.TimeUnit;

public class BeatmapInitJob extends Job {
    private final BeatmapInitTask beatmapInitTask;

    public BeatmapInitJob(PlayScene scene) {
        super(scene.getTrack().getStartDelay(), 0, TimeUnit.MILLISECONDS);
        this.beatmapInitTask = new BeatmapInitTask(scene);
    }

    @Override
    public void run() {
        handleTask(beatmapInitTask);
    }
}
