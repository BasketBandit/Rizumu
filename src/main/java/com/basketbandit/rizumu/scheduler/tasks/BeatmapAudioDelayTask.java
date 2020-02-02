package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.SystemConfiguration;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.beatmap.Beatmap;
import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.Task;

public class BeatmapAudioDelayTask implements Task {
    private final TrackScene scene;
    private final Beatmap beatmap;
    private final AudioPlayer audioPlayer;

    public BeatmapAudioDelayTask(TrackScene scene) {
        this.scene = scene;
        this.beatmap = scene.getBeatmap();
        this.audioPlayer = scene.getAudioPlayer();
    }

    @Override
    public void run() {
        audioPlayer.changeTrack(SystemConfiguration.getBeatmapResourcePath() + beatmap.getAudioFilename() + ".wav");
        audioPlayer.loop(0);
        audioPlayer.play();
    }
}
