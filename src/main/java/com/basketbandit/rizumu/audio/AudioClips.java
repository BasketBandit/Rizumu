package com.basketbandit.rizumu.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AudioClips {
    private static HashMap<String, File> audioFiles = new HashMap<>();

    static {
        audioFiles.put("menu-click", new File("src/main/resources/assets/click.wav").getAbsoluteFile());
        audioFiles.put("menu-select", new File("src/main/resources/assets/select.wav").getAbsoluteFile());

        audioFiles.put("track-hit", new File("src/main/resources/assets/hit.wav").getAbsoluteFile());
        audioFiles.put("track-combobreak", new File("src/main/resources/assets/combobreak.wav").getAbsoluteFile());
    }

    public static AudioInputStream getInputStream(String identifier) throws IOException, UnsupportedAudioFileException {
        return AudioSystem.getAudioInputStream(audioFiles.get(identifier).getAbsoluteFile());
    }
}
