package com.basketbandit.rizumu.resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Image {
    private static HashMap<String, BufferedImage> images = new HashMap<>();

    public Image() {
        try {
            images.put("logo", ImageIO.read(getClass().getResourceAsStream("/assets/image/logo.png")));
            images.put("note", ImageIO.read(getClass().getResourceAsStream("/assets/image/note.png")));
            images.put("noteb", ImageIO.read(getClass().getResourceAsStream("/assets/image/noteb.png")));
        } catch(Exception ex) {
            //
        }
    }

    public static BufferedImage getBufferedImage(String identifier) {
        return images.get(identifier);
    }
}
