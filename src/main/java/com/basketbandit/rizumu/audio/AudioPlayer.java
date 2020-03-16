package com.basketbandit.rizumu.audio;

import com.basketbandit.rizumu.media.Sound;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public final class AudioPlayer {
    private static final Logger log = LoggerFactory.getLogger(AudioPlayer.class);
    private MediaPlayer player;
    private double volume;

    public AudioPlayer(float volume) {
        this.volume = volume;
    }

    public void load(String path) {
        load(new Media(new File(path).toURI().toString()));
        log.info("Track changed: " + path + ", volume: " + volume + ".");
    }

    public void load(Media media) {
        player = new MediaPlayer(media);
        player.setVolume(volume);
        player.setOnStopped(player::dispose);
        player.setOnEndOfMedia(player::dispose);
        log.info("Track changed: " + media.getSource() + ", volume: " + volume);
    }

    public void hotLoad(String path, boolean loop) {
        stop();
        load(path);
        if(loop) {
            loop();
        }
        play();
    }

    public void play() {
        player.setOnReady(() -> player.play());
    }

    /**
     * Plays an {@link AudioClip} from {@link Sound} using a {@link String} identifier, instead of {@link Media} like regular playback.
     * This method is perfect for short audio clips since it is usable immediately, unlike Media which uses a buffer. (Shouldn't be used to play full tracks, since it loads entire track into memory!)
     * @param identifier {@link String}
     */
    public void play(String identifier) {
        if(identifier.equals("track-hit")) {
            return; // volume of clip plays causes stuttering (my assumption is due to overhead of threads starting/terminating several times a second)
        }
        Sound.getAudioClip(identifier).play(volume);
    }

    public void pause() {
        player.pause();
    }

    public void resume() {
        player.play();
    }

    public void stop() {
        player.stop();
    }

    public void loop() {
        player.setOnEndOfMedia(() -> player.seek(Duration.millis(0)));
    }

    public double getVolume() {
        return player.getVolume();
    }

    public void setVolume(double volume) {
        this.volume = volume;
        player.setVolume(volume);
    }

    /**
     * Position of currently playing media as a percentage.
     * @return {@link Integer}
     */
    public double getPosition() {
        return player.getCurrentTime().toMillis() != 0 ? (player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis()) * 100 : 0.0;
    }
}