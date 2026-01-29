package app.display.common;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import app.Configuration;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * Manages loading and caching of images for sprites, backgrounds, and icons.
 * <p>
 * This class provides static methods to retrieve images, ensuring each asset is
 * loaded only once and reused throughout the game. It also handles scaling
 * according to the settings in {@link Configuration} and provides default
 * images if the requested files are missing.
 * 
 * @see RenderableAsSprite
 * @see Background
 * @see Configuration
 */
public class AssetManager {

    /**
     * Prevent instantiation, as this class is intended to be static.
     */
    private AssetManager() {
    }

    // Prevents loading a sprite sheet more than once
    private static HashMap<String, Image> loadedAssets = new HashMap<>();

    private static final String ASSETS_DIRECTORY = "data/sprites/";
    private static final String BACKGROUND_DIRECTORY = "data/backgrounds/";
    private static final String ICON_DIRECTORY = "data/icons/";
    private static final String DEFAULT_IMAGE_FILENAME = "fallback/default.png";
    private static final String DEFAULT_BACKGROUND_IMAGE_FILENAME = "fallback/default.png";
    private static final String DEFAULT_ICON_IMAGE_FILENAME = "default.png";
    protected static final Image DEFAULT_IMAGE = getDefaultImage();
    protected static final Image SCALED_DEFAULT_IMAGE = scaleImage(DEFAULT_IMAGE);

    /**
     * Returns the sprite image for the given filename, loading and caching it if
     * necessary. If the image cannot be found, a default image is returned.
     *
     * @param filename the relative filename of the sprite image within the sprites
     *                 directory
     * @return the loaded {@link javafx.scene.image.Image} object
     */
    public static Image getImage(String filename) {
        if (loadedAssets.containsKey(filename)) {
            // image has already been loaded
            return loadedAssets.get(filename);
        }
        try {
            String imageFilename = ASSETS_DIRECTORY + filename;
            Image image = new Image(new FileInputStream(imageFilename), 0, 0, true, true);
            if (Configuration.INTEGER_SCALE) {
                image = scaleImage(image);
                loadedAssets.put(filename, image);
            } else {
                loadedAssets.put(filename, image);
            }

            return image;
        } catch (FileNotFoundException e) {
            System.err.println("** Invalid Sprite sheet " + filename + ". Using default image **");
            if (Configuration.INTEGER_SCALE) {
                loadedAssets.put(filename, SCALED_DEFAULT_IMAGE);
                return SCALED_DEFAULT_IMAGE;
            } else {
                loadedAssets.put(filename, DEFAULT_IMAGE);
                return DEFAULT_IMAGE;
            }
        }
    }

    /**
     * Returns the background image for the given filename, loading and caching it
     * if necessary. If the image cannot be found, a default background image is
     * returned.
     *
     * @param filename      the relative filename of the background image within the
     *                      backgrounds directory
     * @param width         the desired width of the image
     * @param height        the desired height of the image
     * @param preserveRatio whether to preserve the aspect ratio
     * @return the loaded {@link javafx.scene.image.Image} object
     */
    public static Image getBackgroundImage(String filename, double width, double height, boolean preserveRatio) {
        if (loadedAssets.containsKey(filename + width + height + preserveRatio)) {
            // image has already been loaded
            return loadedAssets.get(filename + width + height + preserveRatio);
        }
        try {
            String imageFilename = BACKGROUND_DIRECTORY + filename;
            Image image = new Image(new FileInputStream(imageFilename), width, height, preserveRatio, false);
            loadedAssets.put(filename + width + height + preserveRatio, image);
            return image;
        } catch (FileNotFoundException e) {
            System.err.println("** Invalid background image " + filename + ". Using default image **");
            Image image = getDefaultBackgroundImage(width, height, preserveRatio);
            loadedAssets.put(filename + width + height + preserveRatio, image);
            return image;
        }
    }

    /**
     * Returns the icon image for the given filename, loading and caching it if
     * necessary. If the image cannot be found, a default icon image is returned.
     *
     * @param filename the relative filename of the icon image within the icons
     *                 directory
     * @return the loaded {@link javafx.scene.image.Image} object
     */
    public static Image getIconImage(String filename) {
        if (loadedAssets.containsKey(ICON_DIRECTORY + filename)) {
            // image has already been loaded
            return loadedAssets.get(ICON_DIRECTORY + filename);
        }
        try {
            filename = ICON_DIRECTORY + filename;
            Image image = new Image(new FileInputStream(filename));
            loadedAssets.put(filename, image);
            return image;
        } catch (FileNotFoundException e) {
            System.err.println("** Invalid icon image " + filename + ". Using default image **");
            Image image = getDefaultIconImage();
            loadedAssets.put(ICON_DIRECTORY + filename, image);
            return image;
        }
    }

    /**
     * Loads and returns the default sprite image.
     *
     * @return the default {@link javafx.scene.image.Image} object
     */
    private static Image getDefaultImage() {
        String defaultImageFilename = ASSETS_DIRECTORY + DEFAULT_IMAGE_FILENAME;
        try {
            return new Image(new FileInputStream(defaultImageFilename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and returns the default background image with the specified dimensions.
     *
     * @param width         the desired width of the image
     * @param height        the desired height of the image
     * @param preserveRatio whether to preserve the aspect ratio
     * @return the default {@link javafx.scene.image.Image} object
     */
    private static Image getDefaultBackgroundImage(double width, double height, boolean preserveRatio) {
        String defaultImageFilename = BACKGROUND_DIRECTORY + DEFAULT_BACKGROUND_IMAGE_FILENAME;
        try {
            return new Image(new FileInputStream(defaultImageFilename), width, height, preserveRatio, false);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads and returns the default icon image.
     *
     * @return the default {@link javafx.scene.image.Image} object
     */
    private static Image getDefaultIconImage() {
        String defaultIconFilename = ICON_DIRECTORY + DEFAULT_ICON_IMAGE_FILENAME;
        try {
            return new Image(new FileInputStream(defaultIconFilename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Scales the given image according to the current configuration zoom setting.
     *
     * @param image the image to scale
     * @return the scaled {@link javafx.scene.image.Image} object
     */
    private static Image scaleImage(Image image) {
        BufferedImage original = SwingFXUtils.fromFXImage(image, null);
        java.awt.Image scaled = original.getScaledInstance(original.getWidth() * (int) Configuration.ZOOM,
                original.getHeight() * (int) Configuration.ZOOM, java.awt.Image.SCALE_FAST);
        BufferedImage out = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = out.createGraphics();
        g2d.drawImage(scaled, 0, 0, null);
        return SwingFXUtils.toFXImage(out, null);
    }
}
