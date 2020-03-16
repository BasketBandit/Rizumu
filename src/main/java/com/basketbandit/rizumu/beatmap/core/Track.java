package com.basketbandit.rizumu.beatmap.core;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.media.Image;
import com.basketbandit.rizumu.utility.extension.AffineTransformEx;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.imageio.ImageIO;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "artist",
        "image_filename",
        "audio_filename",
        "start_delay",
        "beatmaps"
})
public class Track {
    @JsonProperty("id")
    private int id;
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

    private String filePath;
    private String fileName;
    private File file;
    private int trackLength; // milliseconds

    public Track setTrackInfo(String filePath, String fileName, File file) {
        this.filePath = filePath + "/"; // file.getParent() leaves out trailing slash
        this.fileName = fileName;
        this.file = file;
        return this;
    }

    public Track setTrackLength(int trackLength) {
        this.trackLength = trackLength;
        return this;
    }

    @JsonProperty("id")
    public int getId() {
        return id;
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
            if(imageFilename.isEmpty())  {
                return null;
            }
            if(image != null) {
                return image;
            }
            BufferedImage masterImage = Image.toCompatibleImage(ImageIO.read(new File(filePath + imageFilename)));
            image = new BufferedImage(Configuration.getWidth(), Configuration.getHeight(), BufferedImage.TYPE_INT_ARGB);
            return new AffineTransformOp(new AffineTransformEx().inlineScale((Configuration.getWidth()+.0)/(masterImage.getWidth()+.0), (Configuration.getHeight()+.0)/(masterImage.getHeight()+.0)), AffineTransformOp.TYPE_BILINEAR).filter(masterImage, image);
        } catch(Exception ex) {
            return null;
        }
    }

    @JsonProperty("audio_filename")
    public String getAudioFilename() {
        return audioFilename;
    }

    public String getAudioFilePath() {
        return filePath + audioFilename;
    }

    public int getTrackLength() {
        return trackLength;
    }

    public String getFormattedTrackLength() {
        int t = trackLength/1000;
        return ((t/60) % 60) + ":" + ((((t % 60)+"").length() < 2) ? "0" + (t % 60) : (t % 60));
    }

    @JsonProperty("start_delay")
    public Integer getStartDelay() {
        return startDelay;
    }

    @JsonProperty("beatmaps")
    public List<Beatmap> getBeatmaps() {
        return beatmaps;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }
}