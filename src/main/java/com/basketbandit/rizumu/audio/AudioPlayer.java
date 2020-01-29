package com.basketbandit.rizumu.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class AudioPlayer {
    private long currentFrame;
    private Clip clip;
    private String status;
    private String path;

    public AudioPlayer() {
    }

    public AudioPlayer(String path) {
        changeTrack(path);
    }

    public void changeTrack(String path) {
        try {
            this.path = path;
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void play() {
        clip.start();
        status = "playing";
    }

    public void pause() {
        if(status.equals("paused")) {
            return;
        }
        status = "paused";
        this.currentFrame = this.clip.getMicrosecondPosition();
        clip.stop();
    }

    public void resume() {
        if(status.equals("playing")) {
            return;
        }
        status = "playing";
        clip.close();
        changeTrack(path);
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }

    public void stop() {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    public void loop(int type) {
        clip.loop(type);
    }
}