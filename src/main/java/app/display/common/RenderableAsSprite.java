package app.display.common;

import java.util.ArrayList;

import app.Configuration;
import app.gameengine.model.gameobjects.GameObject;

/**
 * Represents any object that is renderable within the game world as a sprite.
 * <p>
 * A renderable must have a sprite sheet to draw from, as well as a location
 * within that sheet to define the region to draw to the screen. The size of
 * that sprite, as well as the offset from the renderable itself, can be set.
 * <p>
 * Renderable may optionally have animations. They must be able to initialize
 * animations, freeze animations, have multiple animation states, and cycle
 * through an animation over time.
 * 
 * @see SpriteLocation
 * @see SpriteGraphics
 * @see Configuration
 * @see GameObject
 */
public interface RenderableAsSprite {

    /**
     * Access the spritesheet from which this renderable sources its sprites.
     * 
     * @return the spritesheet file name within the sprites directory
     */
    String getSpriteSheetFilename();

    /**
     * Returns the {@code SpriteLocation} which defines its current display state
     * within the sprite sheet. If animated, this should be the current frame of
     * animation.
     * 
     * @return the {@code SpriteLocation} of the current frame
     */
    SpriteLocation getCurrentSpriteLocation();

    /**
     * Returns the width of the sprite in pixels. This property describes how many
     * pixels wide the rendered sprite will draw from the sprite sheet.
     * 
     * @return width of the sprite
     */
    default int getSpriteWidth() {
        return Configuration.SPRITE_SIZE;
    }

    /**
     * Returns the height of the sprite in pixels. This property describes how many
     * pixels tall the rendered sprite will draw from the sprite sheet.
     * 
     * @return height of the sprite
     */
    default int getSpriteHeight() {
        return Configuration.SPRITE_SIZE;
    }

    /**
     * Returns the width of each sprite tile in pixels. This property describes the
     * width of each tile of the sprite sheet when calculating the position to draw
     * from, using the {@code SpriteLocation}.
     * <p>
     * By default, this will return the same value as {@link #getSpriteWidth()}.
     * 
     * @return the width of each spritesheet tile
     */
    default int getSpriteTileWidth() {
        return getSpriteWidth();
    }

    /**
     * Returns the height of each sprite tile in pixels. This property describes the
     * height of each tile of the sprite sheet when calculating the position to draw
     * from, using the {@code SpriteLocation}.
     * <p>
     * By default, this will return the same value as {@link #getSpriteHeight()}
     * 
     * @return the height of each spritesheet tile
     */
    default int getSpriteTileHeight() {
        return getSpriteHeight();
    }

    /**
     * Returns the horizontal offset of the sprite from its origin, in pixels.
     * 
     * @return the X offset
     */
    default int getSpriteOffsetX() {
        return 0;
    }

    /**
     * Returns the vertical offset of the sprite from its origin, in pixels.
     * 
     * @return the Y offset
     */
    default int getSpriteOffsetY() {
        return 0;
    }

    /**
     * Returns the value used the scale the entire sprite in the horizontal
     * dimension. A value of 1, which is the default, means no scaling is applied,
     * aside from engine-wide zoom set by {@link Configuration#ZOOM}. A larger value
     * will make the sprite appear wider, and a smaller value will make it appear
     * narrower.
     * 
     * @return the horizontal scaling value
     */
    default double getSpriteScaleX() {
        return 1;
    }

    /**
     * Returns the value used the scale the entire sprite in the vertical dimension.
     * A value of 1, which is the default, means no scaling is applied, aside from
     * engine-wide zoom set by {@link Configuration#ZOOM}. A larger value will make
     * the sprite appear taller, and a smaller value will make it appear shorter.
     * 
     * @return the vertical scaling value
     */
    default double getSpriteScaleY() {
        return 1;
    }

    /**
     * Returns the list of {@code SpriteLocation} objects representing the animation
     * frames of the object within the current animation state.
     * 
     * @return a list of animation frames
     */
    ArrayList<SpriteLocation> getAnimations();

    /**
     * Returns the time spent in the current animation state.
     * 
     * @return time in seconds in the current animation state
     */
    double getTimeInAnimationState();

    /**
     * Initializes the animation frames and states for this renderable
     */
    void initAnimations();

    /**
     * Freeze the current animation, preventing it from updating.
     */
    void freezeAnimations();

    /**
     * Sets the current animation state by name.
     * 
     * @param newState the name of the new animation state
     */
    void setAnimationState(String newState);

    /**
     * Returns the name of the current animation state.
     * 
     * @return the current animation state name
     */
    String getAnimationState();

    /**
     * Returns the duration of a single frame of animation, in seconds.
     * 
     * @return the animation duration
     */
    double getAnimationDuration();

    /**
     * Sets the duration of a single frame of animation, in seconds.
     * 
     * @param animationTime the new animation duration
     */
    void setAnimationDuration(double animationTime);

}
