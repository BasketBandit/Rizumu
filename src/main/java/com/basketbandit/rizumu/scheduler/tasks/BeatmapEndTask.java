package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.scene.ResultsScene;
import com.basketbandit.rizumu.scene.Scenes;
import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.Task;

public class BeatmapEndTask implements Task {
    private final TrackScene scene;

    public BeatmapEndTask(TrackScene scene) {
        this.scene = scene;
    }

    @Override
    public void run() {
        scene.getAudioPlayer().stop();
        ScheduleHandler.cancelExecution();
        ResultsScene resultsScene = (ResultsScene) Rizumu.getStaticScene(Scenes.RESULTS);
        Rizumu.setPrimaryScene(resultsScene.initScene(scene.getStatistics()));
    }
}
