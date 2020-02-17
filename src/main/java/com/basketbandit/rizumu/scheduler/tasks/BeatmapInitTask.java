package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.beatmap.NoteLoader;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.stage.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.Task;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapAudioDelayJob;

public class BeatmapInitTask implements Task {
    private final TrackScene scene;
    private final Beatmap beatmap;

    public BeatmapInitTask(TrackScene scene) {
        this.scene = scene;
        this.beatmap = scene.getBeatmap();
    }

    @Override
    public void run() {
        new NoteLoader(scene, beatmap);
        ScheduleHandler.registerUniqueJob(new BeatmapAudioDelayJob(scene));
    }
}
