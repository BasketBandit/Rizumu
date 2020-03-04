package com.basketbandit.rizumu.audio;

import java.util.HashMap;

public class AudioPlayerController {

    private static HashMap<String, AudioPlayer> audioPlayers = new HashMap<>();

    public AudioPlayerController() {
        addAudioPlayer("music", new AudioPlayer(-20));
        addAudioPlayer("effect", new AudioPlayer(-23));
    }

    public void addAudioPlayer(String name, AudioPlayer audioPlayer) {
        audioPlayers.put(name, audioPlayer);
    }

    public void removeAudioPlayer(String player) {
        audioPlayers.remove(player);
    }

    public static AudioPlayer getAudioPlayer(String player) {
        return audioPlayers.get(player);
    }
}
