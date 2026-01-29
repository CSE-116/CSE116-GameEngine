package app.gameengine;

import java.util.ArrayList;
import java.util.HashMap;

import app.Settings;
import app.display.common.Background;
import app.display.common.controller.BasicMouseControls;
import app.display.common.controller.KeyboardControls;
import app.display.common.controller.MouseControls;
import app.display.common.effects.Effect;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.GameObject;
import app.gameengine.model.gameobjects.Player;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.PhysicsEngine;
import app.gameengine.model.physics.Vector2D;

/**
 * Represents a single level within a game.
 * <p>
 * Second only to {@link Game} in terms of control over game logic, and
 * generally is most reponsible for the majority of gameplay. This class
 * manages storage and manipulation of {@link GameObject}s,
 * {@link KeyboardControls}, {@link MouseControls}, and a {@link PhysicsEngine}.
 * 
 * @see Game
 * @see PhysicsEngine
 * @see KeyboardControls
 * @see MouseControls
 */
public abstract class Level {

    protected KeyboardControls keyboardControls;
    protected MouseControls mouseControls;
    protected Game game;
    protected PhysicsEngine physicsEngine;

    protected int width;
    protected int height;
    protected ArrayList<StaticGameObject> staticObjects = new ArrayList<>();
    protected ArrayList<DynamicGameObject> dynamicObjects = new ArrayList<>();
    private ArrayList<StaticGameObject> originalStaticObjects = new ArrayList<>();
    private ArrayList<DynamicGameObject> originalDynamicObjects = new ArrayList<>();
    private ArrayList<StaticGameObject> staticObjectsToAdd = new ArrayList<>();
    private ArrayList<DynamicGameObject> dynamicObjectsToAdd = new ArrayList<>();

    protected boolean isLoaded;
    protected Vector2D playerStartLocation = new Vector2D(1.0, 1.0);
    private Vector2D lastPlayerLocation;

    protected Background background = new Background();
    private String levelName;
    private HashMap<Effect, Vector2D> activeEffects = new HashMap<>();
    protected double playtime;
    protected double score;

    /**
     * Constructs a level associated with the given game and with the given physics
     * engine, width, height, and name.
     * 
     * @param game          the game this level is a part of
     * @param physicsEngine the physics engine to be used
     * @param width         the width of the level, in tiles
     * @param height        the height of the level, in tiles
     * @param name          the name of the level
     */
    public Level(Game game, PhysicsEngine physicsEngine, int width, int height, String name) {
        this.game = game;
        this.levelName = name;
        this.physicsEngine = physicsEngine;
        this.width = width;
        this.height = height;
        this.isLoaded = false;
        this.mouseControls = new BasicMouseControls(game);
    }

    /**
     * Returns the player associated with this level.
     * 
     * @return the player in this level
     */
    public Player getPlayer() {
        return this.game.getPlayer();
    }

    /**
     * Returns the keyboard controls used by this level.
     * 
     * @return the keyboard controls
     */
    public KeyboardControls getKeyboardControls() {
        return this.keyboardControls;
    }

    /**
     * Sets the keyboard controls used by this level.
     * 
     * @param controls the keyboard controls
     */
    public void setKeyboardControls(KeyboardControls controls) {
        this.keyboardControls = controls;
    }

    /**
     * Returns the mouse controls used by this level.
     * 
     * @return the mouse controls
     */
    public MouseControls getMouseControls() {
        return this.mouseControls;
    }

    /**
     * Sets the mouse controls used by this level.
     * 
     * @param controls the keyboard controls
     */
    public void setMouseControls(MouseControls controls) {
        this.mouseControls = controls;
    }

    /**
     * Returns the physics engine used by this level
     * 
     * @return the physics engine
     */
    public PhysicsEngine getPhysicsEngine() {
        return physicsEngine;
    }

    /**
     * Returns the name of the level.
     * 
     * @return the name of the level
     */
    public String getName() {
        return this.levelName;
    }

    /**
     * Sets the name of the level.
     * 
     * @param levelName the name of the level
     */
    public void setName(String levelName) {
        this.levelName = levelName;
    }

    /**
     * Returns this level's class name.
     * 
     * @return the name of this level's class
     */
    public String getLevelType() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns whether this level has been loaded. This is used by game engine
     * internals, and should not be used otherwise.
     * 
     * @return {@code true} if the level has been loaded, and {@code false}
     *         otherwise
     */
    public boolean isLoaded() {
        return this.isLoaded;
    }

    /**
     * Set whether this level has been loaded. This is used by game engine
     * internals, and should not be used otherwise.
     */
    public void setLoaded() {
        this.isLoaded = true;
    }

    /**
     * Performs any operations necessary to load a level. This can include setup of
     * {@code GameObject}s, such as the player, and any graphics or sound effects.
     */
    public void load() {
        if (!isLoaded) {
            this.originalDynamicObjects = new ArrayList<>(this.dynamicObjects);
            this.originalStaticObjects = new ArrayList<>(this.staticObjects);
            this.originalDynamicObjects.addAll(dynamicObjectsToAdd);
            this.originalStaticObjects.addAll(staticObjectsToAdd);
        }
        // Load player
        this.playtime = 0;
        Vector2D playerLocation = lastPlayerLocation == null ? playerStartLocation : lastPlayerLocation;
        this.getPlayer().setLocation(playerLocation.getX(), playerLocation.getY());
        this.dynamicObjects.removeIf(GameObject::isPlayer);
        this.dynamicObjects.add(this.getPlayer());
        // Effects
        this.activeEffects.clear();
        this.dynamicObjects.forEach(DynamicGameObject::onSpawn);
        this.staticObjects.forEach(StaticGameObject::onSpawn);
        this.onStart();
    }

    /**
     * Resets a level to it's initial state. This can include resetting each
     * {@code GameObject}, as well as resetting which game objects are present in
     * the level.
     */
    public void reset() {
        if (!this.isLoaded) {
            return;
        }
        // Reset objects
        this.playtime = 0;
        this.dynamicObjects.clear();
        this.dynamicObjects.addAll(this.originalDynamicObjects);
        this.staticObjects.clear();
        this.staticObjects.addAll(this.originalStaticObjects);
        this.dynamicObjects.forEach(GameObject::reset);
        this.staticObjects.forEach(GameObject::reset);
        this.dynamicObjectsToAdd.clear();
        this.staticObjectsToAdd.clear();
        // Reset player
        this.getPlayer().reset();
        this.getPlayer().setLocation(playerStartLocation.getX(), playerStartLocation.getY());
        this.dynamicObjects.removeIf(GameObject::isPlayer);
        this.dynamicObjects.add(this.getPlayer());
        this.lastPlayerLocation = null;
        // Reset controls
        this.keyboardControls.reset();
        this.mouseControls.reset();
        // Effects
        this.activeEffects.clear();
        this.dynamicObjects.forEach(DynamicGameObject::onSpawn);
        this.staticObjects.forEach(StaticGameObject::onSpawn);
        this.onStart();
    }

    /**
     * Intended to be called whenever a level is started, ie. loaded or reset. This
     * method should apply any graphical or sound effects associated with this level
     * being started, or any other non-essential logic. It is separate from
     * {@link #load()} and {@link #reset()} so that inherited logic can be
     * maintained, but other effects are not.
     * <p>
     * By default, it does nothing
     */
    public void onStart() {

    }

    /**
     * Returns the {@code Background} used by this level. That background can either
     * be tiled or use one or more background images.
     * <p>
     * If not set, the background will use the default {@link Background}
     * constructor
     * 
     * @return the background of this level
     */
    public Background getBackground() {
        return this.background;
    }

    /**
     * Sets the {@code Background} used by this level. That background can either
     * be tiled or use one or more background images.
     * 
     * @param background the background to be used
     */
    public void setBackground(Background background) {
        this.background = background;
    }

    /**
     * Returns all of the {@code StaticGameObject}s currently within the level. You
     * generally should not directly modify this list, especially if that operation
     * happens during game updates (i.e. it's usually fine if it happens during
     * level setup).
     * <p>
     * If you want to add a static object, you should use
     * {@link #addStaticObject(StaticGameObject)} instead.
     * 
     * @return a list of static objects
     */
    public ArrayList<StaticGameObject> getStaticObjects() {
        return this.staticObjects;
    }

    /**
     * Adds the input static object to this level's list during the next update.
     * This is far safer than modifying the list itself, as returned by
     * {@link #getStaticObjects()}, and is generally preferred.
     * 
     * @param object the object to add
     */
    public void addStaticObject(StaticGameObject object) {
        if (!this.isLoaded) {
            this.staticObjects.add(object);
        } else {
            this.staticObjectsToAdd.add(object);
        }
    }

    /**
     * Returns all of the {@code DynamicGameObject}s currently within the level. You
     * generally should not directly modify this list, especially if that operation
     * happens during game updates (i.e. it's usually fine if it happens during
     * level setup).
     * <p>
     * If you want to add a dynamic object, you should use
     * {@link #addDynamicObject(DynamicGameObject)} instead.
     * 
     * @return a list of dynamic objects
     */
    public ArrayList<DynamicGameObject> getDynamicObjects() {
        return this.dynamicObjects;
    }

    /**
     * Adds the input dynamic object to this level's list during the next update.
     * This is far safer than modifying the list itself, as returned by
     * {@link #getDynamicObjects()}, and is generally preferred.
     * 
     * @param object the object to add
     */
    public void addDynamicObject(DynamicGameObject object) {
        if (!this.isLoaded) {
            this.dynamicObjects.add(object);
        } else {
            this.dynamicObjectsToAdd.add(object);
        }
    }

    /**
     * Returns the starting location of the {@code Player} within the level. If it
     * has not been set, it defaults to (1, 1).
     * 
     * @return the starting location of the player
     */
    public Vector2D getPlayerStartLocation() {
        return new Vector2D(this.playerStartLocation.getX(), this.playerStartLocation.getY());
    }

    /**
     * Sets the starting location of the {@code Player} within the level.
     * 
     * @param x the x location to be set
     * @param y the y location to be set
     */
    public void setPlayerStartLocation(double x, double y) {
        this.playerStartLocation.setX(x);
        this.playerStartLocation.setY(y);
    }

    /**
     * Sets the last whole-numbered tile location of the player.
     * 
     * @param x the x location
     * @param y the y location
     */
    public void setLastPlayerLocation(double x, double y) {
        this.lastPlayerLocation = new Vector2D(Math.round(x), Math.round(y));
    }

    /**
     * Returns the full width, in game tiles, of the level.
     * 
     * @return the width of the level
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the full height, in game tiles, of the level.
     * 
     * @return the height of the level
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the width, in game tiles, of the visible portion of the level. If
     * this is less than the value of {@link #getWidth()}, part of the level will
     * not be displayed, which can be useful for large levels.
     * 
     * @return the visible width of the level
     */
    public int getViewWidth() {
        return width;
    }

    /**
     * Returns the height, in game tiles, of the visible portion of the level. If
     * this is less than the value of {@link #getHeight()}, part of the level will
     * not be displayed, which can be useful for large levels.
     * 
     * @return the visible height of the level
     */
    public int getViewHeight() {
        return height;
    }

    /**
     * Performs the necessary operations for when the left mouse button is pressed.
     * By default, this simply calls the {@link #actionButtonPressed()} method.
     * 
     * @param location the location, in world space, of the click
     */
    public void handleLeftClick(Vector2D location) {
        this.actionButtonPressed();
    }

    /**
     * Performs the necessary operations for when the right mouse button is pressed.
     * By default, this simply cycles the player's inventory.
     * 
     * @param location the location, in world space, of the click
     */
    public void handleRightClick(Vector2D location) {
        this.getPlayer().cycleInventory();
    }

    /**
     * Performs the necessary operations for when the action button (space bar by
     * default), is pressed. By default, this activates the currently selected item
     * in the player's inventory
     */
    public void actionButtonPressed() {
        
    }

    /**
     * Returns a mapping of all {@link Effect}s that are currently active within the
     * level to their central point. These can be associated with the level itself
     * or any of the {@code GameObject}s within the level.
     * 
     * @return a mapping of effects to their location
     */
    public HashMap<Effect, Vector2D> getActiveEffects() {
        return this.activeEffects;
    }

    /**
     * Returns a string used to display the level within the game's UI. This is not
     * guaranteed to be used by the game's UI. By default, it displays the name of
     * the level and the name of the player's active item.
     * 
     * @return a string representing the current level
     */
    public String getUIString() {
        return "Level: " + this.getName() + " - " + "Equipped: " + this.getPlayer().getActiveItemID();
    }

    /**
     * Update the entire level according to the amount of time that has elapsed
     * since the last frame. This includes updating all objects, static and dynamic,
     * within the current level, removing destroyed objects, triggering the physics
     * engine to update the level, and updating all active graphical effects in the
     * level.
     * 
     * @param dt the time elapsed since the last update, in seconds
     */
    public void update(double dt) {
        if (!this.dynamicObjectsToAdd.isEmpty()) {
            this.dynamicObjects.addAll(this.dynamicObjectsToAdd);
            this.dynamicObjectsToAdd.clear();
        }
        if (!this.staticObjectsToAdd.isEmpty()) {
            this.staticObjects.addAll(this.staticObjectsToAdd);
            this.staticObjectsToAdd.clear();
        }
        this.playtime += dt;
        this.dynamicObjects.removeIf(GameObject::isDestroyed);
        this.staticObjects.removeIf(GameObject::isDestroyed);
        this.physicsEngine.updateLevel(dt, this);
        for (int i = 0; i < this.getDynamicObjects().size(); i++) {
            DynamicGameObject object = this.getDynamicObjects().get(i);
            object.update(dt, this);
            object.getEffects().forEach(a -> this.activeEffects.put(a, object.getLocation().copy()));
        }
        for (int i = 0; i < this.getStaticObjects().size(); i++) {
            StaticGameObject object = this.getStaticObjects().get(i);
            object.update(dt, this);
            object.getEffects().forEach(a -> this.activeEffects.put(a, object.getLocation().copy()));
        }
        this.dynamicObjects.removeIf(GameObject::isDestroyed);
        this.staticObjects.removeIf(GameObject::isDestroyed);
        if (Settings.showHitboxes()) {
            this.dynamicObjects.forEach(a -> a.showHitbox());
            this.staticObjects.forEach(a -> a.showHitbox());
        }
        this.activeEffects.forEach((k, v) -> k.update(dt));
        this.activeEffects.entrySet().removeIf(e -> e.getKey().isFinished());
    }

    /**
     * Returns the amount of time that has elapsed since the level began.
     * 
     * @return the elapsed time
     */
    public double getPlaytime() {
        return this.playtime;
    }

    /**
     * Returns the current score within the level. What this score measures depends
     * on the type of level and game.
     * 
     * @return the score
     */
    public double getScore() {
        return this.score;
    }

    /**
     * Sets the current score within the level to the given amount. What this score
     * measures depends on the type of level and game.
     * 
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

}
