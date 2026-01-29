package app.gameengine;

import java.util.HashMap;
import java.util.Map;

import app.Configuration;
import app.Settings;
import app.display.common.ui.PauseMenu;
import app.display.common.ui.UICollection;
import app.display.common.ui.UIElement;
import app.display.common.ui.UIHealthBar;
import app.display.common.ui.UILabel;
import app.gameengine.model.gameobjects.Player;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.statistics.Scoreboard;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 * Represents any game that can be played.
 * <p>
 * A {@code Game} object has the highest level of control over the game itself,
 * and manages the player, levels, and UI. It is responsible for managing the
 * levels themselves, including passing updates to the relevant levels.
 * <p>
 * Note that by default, a {@code Game} object does not contain any levels, or
 * any mechanism for storing and accessing levels, and specific subclasses must
 * implement this functionality.
 * 
 * @see Level
 * @see Player
 * @see UICollection
 */
public abstract class Game {

    private String iconPath = "default.png";
    private Player player;
    protected Level currentLevel;
    protected UICollection UI;
    protected Scoreboard scoreboard;
    protected UIElement pauseMenu = new PauseMenu(this);

    private boolean shouldAdvanceLevel;
    private String shouldChangeLevel = "";

    /**
     * Create a new game, with a {@link Player} as the player.
     */
    public Game() {
        this(new Player(0.0, 0.0, 100));
    }

    /**
     * Create a new game with the given player.
     * 
     * @param player the player of the game
     */
    public Game(Player player) {
        this.player = player;
    }

    /**
     * Performs any necessary actions to initialize the game. This can include
     * loading/adding levels or other setup that cannot be performed at
     * instantiation. This should also include initializing ui for the game, if
     * necessary.
     */
    public void init() {
        this.UI = new UICollection(new HashMap<>(Map.of("UIHealthBar", new UIHealthBar(), "UILabel", new UILabel())));
    }

    /**
     * Adds the given level to the game. How this level is added is up to the
     * subclass, and it is valid for subclasses to ignore this method entirely.
     * 
     * @param level the level to add
     */
    public void addLevel(Level level) {

    }

    /**
     * Advances the game to the next level, if applicable. How this is done is up to
     * the subclass, and it is valid for subclasses to ignore this method entirely.
     * <p>
     * As opposed to {@link #changeLevel(String)}, this method does not allow a
     * specific level to be chosen, and instead relies on the internal structure of
     * the levels to determine what the "next" level is.
     */
    public void advanceLevel() {

    }

    /**
     * Notifies the game that it should advance to the next level, without
     * immediately advancing. The advance will occur in the next update. This is
     * useful for objects or actions that advance level during a physics update, so
     * that an advance can occur without disrupting the active update.
     * <p>
     * If both this and {@link #markChangeLevel(String)} are called, whichever was
     * called most recently will take effect.
     */
    public void markAdvanceLevel() {
        this.shouldAdvanceLevel = true;
        this.shouldChangeLevel = "";
    }

    /**
     * Changes the current level to one with the given name, if applicable. How this
     * is done is up to the subclass, and it is valid for subclasses to ignore this
     * method entirely. Note that if there are multiple levels with the same name,
     * which one is chosen is up to the subclass.
     * <p>
     * As opposed to {@link #advanceLevel()}, this method allows a specific level to
     * be chosen, and does not require a concept of a single "next" level.
     * 
     * @param name the name of the level to change to
     */
    public void changeLevel(String name) {

    }

    /**
     * Notifies the game that it should change to the level with the given name,
     * without immediately changing. The change will occur in the next update. This
     * is useful for objects or actions that change level during a physics update,
     * so that a change can occur without disrupting the active update.
     * <p>
     * If both this and {@link #markAdvanceLevel()} are called, whichever was
     * called most recently will take effect.
     */
    public void markChangeLevel(String name) {
        this.shouldChangeLevel = name;
        this.shouldAdvanceLevel = false;
    }

    /**
     * Access the {@code Player} object associated with this game.
     * 
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set the {@code Player} object associated with this game.
     * 
     * @param player the player to be set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Get the name of this game. By default, simply returns the name of the class.
     * If the name of the game is different, this method should be overridden.
     * 
     * @return the game name
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns a path within the icons directory to the icon for this game.
     * 
     * @return the path of the icon
     */
    public String getIconPath() {
        return iconPath;
    }

    /**
     * Set the path within the icons directory of the icon for this game.
     * 
     * @param iconPath the path of the icon
     */
    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    /**
     * Returns the {@code UICollection} associated with this game, which holds the
     * main UI elements.
     * 
     * @return the UICollection for this game
     */
    public UICollection getUICollection() {
        return this.UI;
    }

    /**
     * Returns the level that is currently being played within the game.
     * 
     * @return the current level
     */
    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    /**
     * Performs any actions necessary to begin playing a level. Most of this
     * behavior is deferred to the specific level class.
     * 
     * @param level the level to load
     */
    public void loadLevel(Level level) {
        if (this.currentLevel != null) {
            this.currentLevel.setLastPlayerLocation(player.getLocation().getX(), player.getLocation().getY());
        }
        this.currentLevel = level;
        this.currentLevel.load();
    }

    /**
     * Resets the currently loaded level to its initial state. Most of this behavior
     * is deferred to the specific level class.
     */
    public void resetCurrentLevel() {
        this.currentLevel.reset();
    }

    /**
     * Update the entire game according to the amount of time that has elapsed since
     * the last frame. This includes updating the UI and the current level, and
     * resetting the current level if the player is destroyed.
     * <p>
     * Note that updates are not passed to the UI or level if the game is paused.
     * 
     * @param dt the time elapsed since the last update, in seconds
     */
    public void update(double dt) {
        if (Settings.paused()) {
            return;
        }
        if (this.shouldAdvanceLevel) {
            this.shouldAdvanceLevel = false;
            this.advanceLevel();
        } else if (!this.shouldChangeLevel.equals("")) {
            this.changeLevel(this.shouldChangeLevel);
            this.shouldChangeLevel = "";
        }
        this.currentLevel.update(dt);
        this.UI.update(dt, this.currentLevel);
        if (this.player.isDestroyed()) {
            this.resetCurrentLevel();
        }
    }

    /**
     * Reset the entire game, including all levels in the game. By default, this
     * method does nothing, as there is no default storage for levels within the
     * game.
     */
    public void resetGame() {
    }

    /**
     * Return the scoreboard associated with this game.
     * 
     * @return the scoreboard
     */
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    /**
     * Pause the game, and perform whatever UI updates are necessary to indicate
     * this.
     */
    public void pause() {
        Settings.setPaused(true);
        if (this.UI != null) {
            this.UI.removeElement("pause");
            this.UI.addElement("pause", this.pauseMenu);
            this.pauseMenu.update(0, currentLevel);
        }
    }

    /**
     * Unpause the game, and perform whatever UI updates are necessary to indicate
     * this.
     */
    public void unpause() {
        Settings.setPaused(false);
        if (this.UI != null) {
            this.UI.removeElement("pause");
        }
    }

    /**
     * Create the main {@link javafx.scene.Parent} to be housed within the
     * {@linke javafx.scene.Scene} of the window. This can be used to modify the
     * position of the UI and other menus relative to the game itself.
     * 
     * @param bgGroup  the group which contains all background objects
     * @param fgGround the group which contains all foreground objects
     * @return the root node of the game
     */
    public Parent createRootNode(Group bgGroup, Group fgGround) {
        return new StackPane(bgGroup, fgGround, this.getUICollection().getRenderableUI());
    }

    /**
     * The full width of the game window, in pixels, including any UI elements.
     * 
     * @return the width of the game window
     */
    public double getWindowWidth() {
        return currentLevel.getViewWidth() * Configuration.SCALE_FACTOR;
    }

    /**
     * The full height of the game window, in pixels, including any UI elements.
     * 
     * @return the height of the game window
     */
    public double getWindowHeight() {
        return currentLevel.getViewHeight() * Configuration.SCALE_FACTOR;
    }

    /**
     * Return the world space location of a given screen space location within the
     * root scene of the window.
     * 
     * @param rootSceneLocation a screen space location within the root scene
     * @return the world space location of the input
     */
    public Vector2D screenSpaceToWorldSpace(Vector2D rootSceneLocation) {
        return Vector2D.div(rootSceneLocation, Configuration.SCALE_FACTOR);
    }

}
