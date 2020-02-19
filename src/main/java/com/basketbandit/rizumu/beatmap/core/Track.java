package com.basketbandit.rizumu.beatmap.core;

import com.basketbandit.rizumu.Configuration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "artist",
        "image_filename",
        "audio_filename",
        "start_delay",
        "beatmaps"
})
public class Track {
    private String fileName;
    private File file;
    @JsonProperty("name")
    private String name;
    @JsonProperty("artist")
    private String artist;
    @JsonProperty("image_filename")
    private String imageFilename;
    private BufferedImage image;
    @JsonProperty("audio_filename")
    private String audioFilename;
    @JsonProperty("start_delay")
    private Integer startDelay;
    @JsonProperty("beatmaps")
    private List<Beatmap> beatmaps = null;

    public void setFileInfo(String fileName, File file) {
        this.fileName = fileName;
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("artist")
    public String getArtist() {
        return artist;
    }

    @JsonProperty("image_filename")
    public String getImageFilename() {
        return imageFilename;
    }

    public BufferedImage getImage() {
        try {
            if(imageFilename.isBlank() || imageFilename.isEmpty())  {
                return  null;
            }
            if(image != null) {
                return image;
            }
            return image = ImageIO.read(new File(Configuration.getBeatmapResourcePath() + imageFilename));
        } catch(Exception ex) {
            return null;
        }
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