package app.display.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Represents a level background.
 * A background can be a single images, multiple layered images, or a single
 * {@value app.Configuration#SPRITE_SIZE} pixel square of a sprite tile that is
 * repeated to fill the screen.
 * <p>
 * Single or multiple images can have parallax effects applied, which
 * cause images to shift as the player moves.
 */
public class Background {

    private ArrayList<String> imageFileNames;
    private ArrayList<Double> parallaxRatios;
    private boolean usesBackgroundImage;
    private SpriteLocation groundTileSpriteLocation;

    /**
     * Constructs a background with explicit parallax ratios for each image.
     * Images should be given in back-to-front rendering order. Lists of images
     * and parallax ratios must be non-empty and of the same length.
     *
     * @param imageFileNames the list of background image file paths, from the
     *                       default background directory
     * @param parallaxRatios the corresponding parallax ratio for each image
     * @throws RuntimeException if the lists are empty or not the same length
     */
    public Background(ArrayList<String> imageFileNames, ArrayList<Double> parallaxRatios) {
        if (imageFileNames.isEmpty()) {
            throw new RuntimeException("At least one background image must be provided");
        } else if (imageFileNames.size() != parallaxRatios.size()) {
            throw new RuntimeException("Number of background images must equal number of parallax ratios");
        }
        this.imageFileNames = imageFileNames;
        this.parallaxRatios = parallaxRatios;
        this.usesBackgroundImage = true;
    }

    /**
     * Constructs a background with automatically assigned parallax ratios,
     * evenly spaced from 0.0 to 1.0, inclusive. If a single image is given,
     * the parallax ratio is set to 1.0.
     *
     * @param imageFileNames the list of background image file paths, from the
     *                       default background directory
     * @throws RuntimeException if the list is empty
     */
    public Background(ArrayList<String> imageFileNames) {
        this(imageFileNames,
                imageFileNames.size() > 1
                        ? new ArrayList<>(IntStream.range(0, imageFileNames.size())
                                .mapToDouble(a -> a / (imageFileNames.size() - 1.0)).boxed().toList())
                        : new ArrayList<>(Arrays.asList(1.0)));
    }

    /**
     * Constructs a background with a single image and the specified parallax ratio
     *
     * @param imageFileName the background image file path, from the default
     *                      background directory
     * @param parallaxRatio the parallax ratio for the image
     */
    public Background(String imageFileName, double parallaxRatio) {
        this(new ArrayList<>(Arrays.asList(imageFileName)), new ArrayList<>(Arrays.asList(parallaxRatio)));
    }

    /**
     * Constructs a background with a single image and the default parallax ratio of
     * 0.0.
     *
     * @param imageFileName the background image file path, from the default
     *                      background directory
     */
    public Background(String imageFileName) {
        this(new ArrayList<>(Arrays.asList(imageFileName)));
    }

    /**
     * Constructs a tiled background using the given sprite sheet and tile location
     * within that sprite sheet. Each tile is a square of size
     * {@value app.Configuration#SPRITE_SIZE} pixels, and the column and row
     * specifies the location of the chosen tile within the sprite sheet.
     * 
     * 
     * @param spriteSheetFileName the path to the sprite sheet image, from the
     *                            default sprite directory
     * @param column              the column of the sprite within it's sprite sheet
     * @param row                 the row of the sprite within it's sprite sheet
     */
    public Background(String spriteSheetFileName, int column, int row) {
        this.imageFileNames = new ArrayList<>(Arrays.asList(spriteSheetFileName));
        this.groundTileSpriteLocation = new SpriteLocation(column, row);
        this.usesBackgroundImage = false;
    }

    /**
     * Constructs a default tiled background using a grass sprite.
     */
    public Background() {
        this("MiniWorldSprites/Ground/Grass.png", 2, 0);
    }

    /**
     * Returns whether this background uses a background image or a tiled sprite
     * background.
     *
     * @return {@code true} if the background uses images, {@code false} if it uses
     *         tiled sprites
     */
    public boolean usesBackgroundImage() {
        return this.usesBackgroundImage;
    }

    /**
     * Returns the list of background image file names. If using a tiled sprite
     * background, it has only a single entry, which is the sprite sheet specified.
     * 
     * @return a list of image file names
     */
    public ArrayList<String> getBackgroundImageFileNames() {
        return this.imageFileNames;
    }

    /**
     * Returns the list of parallax ratios corresponding to background images
     * Only valid if {@link #usesBackgroundImage()} is true.
     * 
     * @return a list of parallax ratios
     */
    public ArrayList<Double> getParallaxRatios() {
        return this.parallaxRatios;
    }

    /**
     * Return the location of the sprite tile used for tiling the background
     * Only valid if {@link #usesBackgroundImage()} is false.
     * 
     * @return the {@code SpriteLocation} specifying the background tile within its
     *         sprite sheet
     */
    public SpriteLocation getGroundTileSpriteLocation() {
        return this.groundTileSpriteLocation;
    }

}
