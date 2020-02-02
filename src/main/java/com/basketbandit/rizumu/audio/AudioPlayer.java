package com.basketbandit.rizumu.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class AudioPlayer {
    private long currentFrame;
    private Clip clip;
    private String status;
    private String path;
    private FloatControl gainControl;
    private float gain = -10.0f;

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
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gain);
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