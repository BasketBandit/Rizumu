package com.basketbandit.rizumu.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Image {
    private static final Logger log = LoggerFactory.getLogger(Image.class);
    private static HashMap<String, BufferedImage> images = new HashMap<>();

    public Image() {
        try {
            images.put("no-song", ImageIO.read(getClass().getResourceAsStream("/assets/image/no-song.png")));
            images.put("logo", ImageIO.read(getClass().getResourceAsStream("/assets/image/logo.png")));
            images.put("note", ImageIO.read(getClass().getResourceAsStream("/assets/image/note.png")));
            images.put("noteb", ImageIO.read(getClass().getResourceAsStream("/assets/image/noteb.png")));
            images.put("hitflash-body", ImageIO.read(getClass().getResourceAsStream("/assets/image/hitflash-body.png")));
            images.put("settings-icon", ImageIO.read(getClass().getResourceAsStream("/assets/image/settings.png")));
            images.put("splash-background", ImageIO.read(getClass().getResourceAsStream("/assets/image/splash-background.jpg")));

            // ensures image compatibility
            for(String image: images.keySet()) {
                images.replace(image, toCompatibleImage(images.get(image)));
            }
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Image.class.getSimpleName(), ex.getMessage(), ex);
        }
    }

    public static BufferedImage getBufferedImage(String identifier) {
        return images.get(identifier);
    }

    public static BufferedImage toCompatibleImage(BufferedImage image) {
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();

        if(image.getColorModel().equals(gc.getColorModel())) {
            return image;
        }

        BufferedImage tempImage = gc.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());

        Graphics2D g = tempImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return tempImage;
    }
}
