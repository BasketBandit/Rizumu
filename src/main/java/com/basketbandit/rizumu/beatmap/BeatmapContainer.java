package com.basketbandit.rizumu.beatmap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "beatmap"
})
public class BeatmapContainer {

    @JsonProperty("beatmap")
    private List<Beatmap> beatmaps = null;

    @JsonProperty("beatmap")
    public List<Beatmap> getBeatmaps() {
        return beatmaps;
    }

}
