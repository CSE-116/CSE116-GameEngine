package app.display.common;

import app.Configuration;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Handles rendering of game objects as sprites using JavaFX's
 * {@link ImageView}.
 * <p>
 * This class manages the display of a sprite from a sprite sheet, including
 * viewport selection, scaling, rotation, and reflection. It supports both
 * integer and fractional zoom levels and can reset the viewport to display the
 * correct sprite frame based on the object's state.
 * <p>
 * SpriteGraphics is typically constructed from a {@link RenderableAsSprite}
 * object and
 * automatically loads the appropriate image asset.
 *
 * @see RenderableAsSprite
 * @see SpriteLocation
 * @see AssetManager
 */
public class SpriteGraphics extends ImageView {

    public static final SpriteLocation DEFAULT_IMAGE_LOCATION = new SpriteLocation(0, 0);

    /**
     * Constructs a SpriteGraphics object for the given renderable, using the
     * default zoom level.
     *
     * @param renderableObject the object to render as a sprite
     */
    public SpriteGraphics(RenderableAsSprite renderableObject) {
        this(renderableObject, Configuration.ZOOM);
    }

    /**
     * Constructs a SpriteGraphics object for the given renderable, using the
     * specified zoom level.
     *
     * @param renderableObject the object to render as a sprite
     * @param zoom             the zoom factor to apply to the sprite
     */
    public SpriteGraphics(RenderableAsSprite renderableObject, double zoom) {
        Image image = AssetManager.getImage(renderableObject.getSpriteSheetFilename());
        this.setImage(image);
        if (image == AssetManager.DEFAULT_IMAGE || image == AssetManager.SCALED_DEFAULT_IMAGE) {
            resetSpriteViewport(renderableObject, DEFAULT_IMAGE_LOCATION);
        } else {
            resetSpriteViewport(renderableObject);
        }

        if (!Configuration.INTEGER_SCALE) {
            this.setScaleX(zoom);
            this.setScaleY(zoom);
        }
    }

    /**
     * Sets the viewport and transformations for this sprite using the specified
     * sprite location. This includes cropping the sprite sheet, applying
     * reflection, and rotation.
     *
     * @param renderableObject the object to render as a sprite
     * @param location         the sprite location
     */
    public void resetSpriteViewport(RenderableAsSprite renderableObject, SpriteLocation location) {
        int x = location.getColumn();
        int y = location.getRow();
        int fullTileWidth = renderableObject.getSpriteTileWidth();
        int fullTileHeight = renderableObject.getSpriteTileHeight();

        int tileWidth = renderableObject.getSpriteWidth();
        int tileHeight = renderableObject.getSpriteHeight();
        if (Configuration.INTEGER_SCALE) {
            tileWidth *= (int) Configuration.ZOOM;
            tileHeight *= (int) Configuration.ZOOM;
            fullTileWidth *= (int) Configuration.ZOOM;
            fullTileHeight *= (int) Configuration.ZOOM;
        }
        this.setViewport(new Rectangle2D(fullTileWidth * x, fullTileHeight * y, tileWidth, tileHeight));
        double scaleX = renderableObject.getSpriteScaleX();
        double scaleY = renderableObject.getSpriteScaleY();
        this.setScaleX(location.isReflectedHorizontally() ? -scaleX : scaleX);
        this.setScaleY(location.isReflectedVertically() ? -scaleY : scaleY);
        this.setTranslateX((scaleX - 1) * tileWidth / 2);
        this.setTranslateY((scaleY - 1) * tileHeight / 2);
        this.setRotate(location.getRotation());
    }

    /**
     * Sets the viewport and transformations for this sprite using the object's
     * current sprite location.
     *
     * @param renderableObject the object to render as a sprite
     */
    public void resetSpriteViewport(RenderableAsSprite renderableObject) {
        resetSpriteViewport(renderableObject, renderableObject.getCurrentSpriteLocation());
    }

}
