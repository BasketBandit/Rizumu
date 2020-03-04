package com.basketbandit.rizumu.audio;

import com.basketbandit.rizumu.resource.Sound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    private static final Logger log = LoggerFactory.getLogger(AudioPlayer.class);
    private String path;
    private Clip clip;
    private long currentFrame;
    private String status = "stopped";
    private FloatControl gainControl;
    private float gain = 0.0f;

    public AudioPlayer(float gain) {
        this.gain = gain;
    }

    public void hotChangeTrack(String inPath) {
        stop();
        changeTrack(inPath);
        play();
    }

    public void changeTrack(String inPath) {
        try {
            path = inPath;
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path).getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gain);
            log.info("track changed: " + path + ", level: " + clip.getLevel() + ", length: " + clip.getMicrosecondLength()/1000000 + " seconds");
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    public void play() {
        clip.start();
        status = "playing";
    }

    public void play(String identifier) {
        try {
            Clip audioClip = AudioSystem.getClip(); // get clip
            audioClip.open(Sound.getAudioInputStream(identifier)); // open clip
            ((FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(gain); // set gain
            audioClip.addLineListener(event -> {
                if(event.getType().equals(LineEvent.Type.STOP)) {
                    event.getLine().close(); // close clip on end
                }
            });
            audioClip.start(); // start clip
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
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

    /**
     * Position of currently playing clip as a percentage.
     * @return {@link Integer}
     */
    public double getClipPosition() {
        return clip.getMicrosecondPosition() != 0 ? (((clip.getMicrosecondPosition()+.0) / (clip.getMicrosecondLength()+.0)) * 100) : 0.0;
    }

    /**
     * Utility Functions
     */
    public static int getTrackLength(String p) {
        try(AudioInputStream in = AudioSystem.getAudioInputStream(new File(p).getAbsoluteFile())) {
            Clip c = AudioSystem.getClip();
            c.open(in);
            return (int) (c.getMicrosecondLength() / 1000000);
        } catch(Exception ex) {
            return 0;
        }
    }
}