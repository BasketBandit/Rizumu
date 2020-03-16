package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.Task;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.scene.track.TrackScene;

public class BeatmapEndTask implements Task {
    private final TrackScene scene;

    public BeatmapEndTask(TrackScene scene) {
        this.scene = scene;
    }

    @Override
    public void run() {
        scene.getAudioPlayer().stop();
        ScheduleHandler.cancelExecution();
        Engine.setPrimaryScene(Engine.getStaticScene(Scenes.RESULTS).init(scene.getScore()));
    }
}
