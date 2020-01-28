package com.basketbandit.rizumu.beatmap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "start_delay",
        "segments"
})
public class Beatmap {
    @JsonProperty("name")
    private String name;
    @JsonProperty("start_delay")
    private int startDelay;
    @JsonProperty("segments")
    private List<Segment> segments = null;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("start_delay")
    public int getStartDelay() {
        return startDelay;
    }

    @JsonProperty("segments")
    public List<Segment> getSegments() {
        return segments;
    }
}
