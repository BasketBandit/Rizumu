package com.basketbandit.rizumu.beatmap.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "keys",
        "segments"
})
public class Beatmap {
    @JsonProperty("name")
    private String name;
    @JsonProperty("keys")
    private Integer keys;
    @JsonProperty("segments")
    private List<Segment> segments = null;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("keys")
    public Integer getKeys() {
        return keys;
    }

    @JsonProperty("segments")
    public List<Segment> getSegments() {
        return segments;
    }
}
