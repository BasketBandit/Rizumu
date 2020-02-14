package com.basketbandit.rizumu.beatmap.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "length",
        "notes"
})
public class Segment {
    @JsonProperty("name")
    private String name;
    @JsonProperty("length")
    private int length;
    @JsonProperty("notes")
    private List<Note> notes = null;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("length")
    public int getLength() {
        return length;
    }

    @JsonProperty("notes")
    public List<Note> getNotes() {
        return notes;
    }
}
