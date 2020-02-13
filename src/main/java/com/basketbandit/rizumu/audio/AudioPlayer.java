package com.basketbandit.rizumu.audio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class AudioPlayer {
    private static final Logger log = LoggerFactory.getLogger(AudioPlayer.class);

    private long currentFrame;
    private Clip clip;
    private String status = "stopped";
    private String path;
    private FloatControl gainControl;
    private float gain = 0.0f;

    public AudioPlayer() {
    }

    public AudioPlayer(String path) {
        changeTrack(path);
    }

    public void changeTrack(String inPath) {
        try {
            path = inPath;
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gain);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    public void play() {
        clip.start();
        status = "playing";
        log.info("playing track: " + path + ", level: " + clip.getLevel() + ", length: " + clip.getMicrosecondLength()/1000000 + " seconds");
    }

    public void pause() {
        if(status.equals("paused") || status.equals("stopped")) {
            return;
        }
        status = "paused";
        currentFrame = clip.getMicrosecondPosition();
        clip.stop();
    }

    public void resume() {
        if(status.equals("playing") || status.equals("stopped")) {
            return;
        }
        status = "playing";
        clip.close();
        changeTrack(path);
        clip.setMicrosecondPosition(currentFrame);
        play();
    }

    public void stop() {
        if(status.equals("stopped")) {
            return;
        }
        status = "stopped";
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    public void loop(int type) {
        clip.loop(type);
    }

    public float getMasterGain() {
        return gain;
    }

    public void setMasterGain(float gain) {
        this.gain = gain;
    }

    public float getGain() {
        return gainControl.getValue();
    }

    public void setGain(float gain) {
        gainControl.setValue(gain);
    }


}