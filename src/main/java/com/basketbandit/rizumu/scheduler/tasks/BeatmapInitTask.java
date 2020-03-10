package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.Task;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapAudioDelayJob;
import com.basketbandit.rizumu.stage.scene.TrackScene;

public class BeatmapInitTask implements Task {
    private final TrackScene scene;

    public BeatmapInitTask(TrackScene scene) {
        this.scene = scene;
    }

    @Override
    public void run() {
        ScheduleHandler.registerUniqueJob(new BeatmapAudioDelayJob(scene));
    }
}
