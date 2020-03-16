package com.basketbandit.rizumu.scheduler.tasks;

import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.scheduler.Task;
import com.basketbandit.rizumu.stage.scene.track.TrackScene;

public class BeatmapAudioDelayTask implements Task {
    private final Track track;
    private final AudioPlayer audioPlayer;

    public BeatmapAudioDelayTask(TrackScene scene) {
        this.track = scene.getTrack();
        this.audioPlayer = scene.getAudioPlayer();
    }

    @Override
    public void run() {
        audioPlayer.load(track.getAudioFilePath());
        audioPlayer.play();
    }
}
