package com.basketbandit.rizumu.scheduler.jobs;

import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.Job;
import com.basketbandit.rizumu.scheduler.tasks.BeatmapAudioDelayTask;

import java.util.concurrent.TimeUnit;

public class BeatmapAudioDelayJob extends Job {
    private final BeatmapAudioDelayTask beatMapAudioDelayTask;

    public BeatmapAudioDelayJob(TrackScene scene) {
        super(3000, 0, TimeUnit.MILLISECONDS);
        this.beatMapAudioDelayTask = new BeatmapAudioDelayTask(scene);
    }

    @Override
    public void run() {
        handleTask(beatMapAudioDelayTask);
    }
}
