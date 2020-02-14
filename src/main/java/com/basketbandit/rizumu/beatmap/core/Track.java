package com.basketbandit.rizumu.beatmap.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "artist",
        "audio_filename",
        "start_delay",
        "beatmaps"
})
public class Track {
    @JsonProperty("name")
    private String name;
    @JsonProperty("artist")
    private String artist;
    @JsonProperty("audio_filename")
    private String audioFilename;
    @JsonProperty("start_delay")
    private Integer startDelay;
    @JsonProperty("beatmaps")
    private List<Beatmap> beatmaps = null;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("artist")
    public String getArtist() {
        return artist;
    }

    @JsonProperty("audio_filename")
    public String getAudioFilename() {
        return audioFilename;
    }

    @JsonProperty("start_delay")
    public Integer getStartDelay() {
        return startDelay;
    }

    @JsonProperty("beatmaps")
    public List<Beatmap> getBeatmaps() {
        return beatmaps;
    }
}