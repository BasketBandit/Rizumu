package com.basketbandit.rizumu.audio;

import java.util.HashMap;

public class AudioPlayerController {

    private static HashMap<String, AudioPlayer> audioPlayers = new HashMap<>();

    public AudioPlayerController() {
        addAudioPlayer("effects", new AudioPlayer());
        addAudioPlayer("menu", new AudioPlayer());
        addAudioPlayer("beatmap", new AudioPlayer());
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
