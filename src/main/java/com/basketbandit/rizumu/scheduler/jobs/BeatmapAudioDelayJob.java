package com.basketbandit.rizumu.scheduler.jobs;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.scheduler.Job;
import com.basketbandit.rizumu.scheduler.tasks.BeatmapAudioDelayTask;
import com.basketbandit.rizumu.stage.scene.TrackScene;

import java.util.concurrent.TimeUnit;

public class BeatmapAudioDelayJob extends Job {
    private final BeatmapAudioDelayTask beatMapAudioDelayTask;

    public BeatmapAudioDelayJob(TrackScene scene) {
        super(Configuration.getTrackStartDelay() + scene.getTrack().getStartDelay(), 0, TimeUnit.MILLISECONDS);
        this.beatMapAudioDelayTask = new BeatmapAudioDelayTask(scene);
    }

    @Override
    public void run() {
        handleTask(beatMapAudioDelayTask);
    }
}
