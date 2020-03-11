package com.basketbandit.rizumu.audio;

import com.basketbandit.rizumu.resource.Sound;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AudioPlayer {
    private static final Logger log = LoggerFactory.getLogger(AudioPlayer.class);
    private MediaPlayer player;
    private float volume;

    public AudioPlayer(float volume) {
        this.volume = volume;
    }

    public void changeTrack(String path) {
        player = new MediaPlayer(new Media(new File(path).toURI().toString()));
        player.setVolume(volume);
        player.setOnStopped(player::dispose);
        player.setOnEndOfMedia(player::dispose);
        log.info("Track changed: " + path + ", volume: " + volume + ".");
    }

    public void changeTrack(Media media) {
        player = new MediaPlayer(media);
        player.setVolume(volume);
        player.setOnStopped(player::dispose);
        player.setOnEndOfMedia(player::dispose);
        log.info("Track changed: " + media.getSource() + ", volume: " + volume);
    }

    public void hotChangeTrack(String path) {
        stop();
        changeTrack(path);
        play();
    }

    public void play() {
        player.setOnReady(() -> player.play());
    }

    public void play(String identifier) {
        try {
            MediaPlayer tempPlayer = new MediaPlayer(Sound.getMedia(identifier));
            tempPlayer.setOnReady(() -> {
                tempPlayer.setVolume(volume);
                tempPlayer.play();
            });
            tempPlayer.setOnEndOfMedia(tempPlayer::dispose);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
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

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    /**
     * Position of currently playing media as a percentage.
     * @return {@link Integer}
     */
    public double getMediaPosition() {
        return player.getCurrentTime().toMillis() != 0 ? (player.getCurrentTime().toMillis() / player.getTotalDuration().toMillis()) * 100 : 0.0;
    }
}