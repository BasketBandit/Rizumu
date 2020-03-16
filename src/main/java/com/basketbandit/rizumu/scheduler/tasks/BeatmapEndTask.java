package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.Task;
import com.basketbandit.rizumu.stage.scene.play.PlayScene;

public class BeatmapEndTask implements Task {
    private final PlayScene scene;

    public BeatmapEndTask(PlayScene scene) {
        this.scene = scene;
    }

    @Override
    public void run() {
        scene.getAudioPlayer().stop();
        ScheduleHandler.cancelExecution();
        Engine.setSecondaryScene(scene.getResultsMenu().init(scene.getScore()));
    }
}
