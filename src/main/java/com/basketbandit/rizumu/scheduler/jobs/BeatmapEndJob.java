package com.basketbandit.rizumu.scheduler.jobs;

import com.basketbandit.rizumu.stage.scene.play.PlayScene;
import com.basketbandit.rizumu.scheduler.Job;
import com.basketbandit.rizumu.scheduler.tasks.BeatmapEndTask;

import java.util.concurrent.TimeUnit;

public class BeatmapEndJob extends Job {
    private final BeatmapEndTask beatmapEndTask;

    public BeatmapEndJob(PlayScene scene, long delay) {
        super( delay + 3000, 0, TimeUnit.MILLISECONDS);
        this.beatmapEndTask = new BeatmapEndTask(scene);
    }

    @Override
    public void run() {
        handleTask(beatmapEndTask);
    }
}