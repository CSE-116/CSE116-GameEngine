package app;

import app.display.common.controller.KeyboardControls;
import app.games.GameFactory;

/**
 * Static class containing properties which will not change at runtime.
 * <p>
 * The static variables defined in this class determine certain properties of
 * the game, including the game being played, the default sprite size and zoom
 * factor, whether integer scaling is enabled, and the default animation
 * duration. Unlike {@link Settings}, these variables are final and will
 * not change at runtime.
 * <p>
 * Note that most of these values are only defaults, and games/levels/objects
 * may override these values with their own in many cases.
 * 
 * @see Settings
 * @see GameFactory
 */
public class Configuration {

    /**
     * The game being played. Must be a valid option within the factory method
     * {@link GameFactory#getGame}.
     */
    public static final String GAME = "sample game";

    /**
     * Multiplier for increasing the size of the window and objects within the game.
     * If {@link #INTEGER_SCALE} is enabled, this should be a whole number for best
     * results.
     */
    public static final double ZOOM = 3.0;

    /**
     * Default sprite size, in pixels, of objects within the game. This can be
     * overridden if the object is smaller or larger in a given dimension.
     */
    public static final int SPRITE_SIZE = 16;

    /**
     * Scale factor from game space to screen space. Calculated based off of
     * {@link #SPRITE_SIZE} and {@link #ZOOM}, and only included for convenience.
     */
    public static final double SCALE_FACTOR = SPRITE_SIZE * ZOOM;

    /**
     * Scale factor specifically for text elements. By default, this has the same
     * value as {@link #ZOOM}, but it can be increased or decreased independently.
     */
    public static final double TEXT_SCALE = ZOOM;

    /**
     * Default size for text elements within the game. If a size is not specified
     * when retrieving a font, this is the value that is used.
     */
    public static final double DEFAULT_TEXT_SIZE = TEXT_SCALE * 5;

    /**
     * Whether integer scaling is applied to rendered sprites in the game. This
     * should be enabled, as the engine is pretty much untested with it disabled,
     * and tends to have graphical bugs. Disabling it does, however, allow for
     * non-integer {@code #ZOOM} levels.
     */
    public static final boolean INTEGER_SCALE = true;

    /**
     * Default duration of animation frames, in seconds. This can be overridden if
     * specific objects have different animation times.
     */
    public static final double ANIMATION_TIME = 0.1;

    /**
     * Default volume for music tracks within the game. If the volume is not
     * specified when starting music, this volume will be used.
     */
    public static final double DEFAULT_MUSIC_VOLUME = 0.5;

    /**
     * Default volume for sound effects within the game. If the volume is not
     * specified when playiny a sound effect, this volume will be used.
     */
    public static final double DEFAULT_SOUND_VOLUME = 0.5;

    /**
     * If true, enables certain controls that are useful for debugging while playing
     * the game. Primarily used/controlled by {@link KeyboardControls}.
     */
    public static final boolean DEBUG_MODE = true;

}
