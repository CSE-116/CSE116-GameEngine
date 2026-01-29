package app.gameengine.model.gameobjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import app.Configuration;
import app.display.common.RenderableAsSprite;
import app.display.common.SpriteLocation;
import app.display.common.effects.Effect;
import app.display.common.effects.HitboxOutline;
import app.gameengine.Level;
import app.gameengine.model.physics.Collidable;
import app.gameengine.model.physics.Hitbox;
import app.gameengine.model.physics.Vector2D;

/**
 * Represents an abstract game object in the game engine.
 * <p>
 * A {@code GameObject} defines the base functionality for all visible and
 * interactive objects in the game world. This is the base class for all
 * objects, including walls, enemies, projectiles, and the player. Notable
 * subclasses include {@code DynamicGameObject} and {@code StaticGameObject}
 * </p>
 * Contains properties like a position, a hitbox with separate position and
 * dimensions for physics and collisions, sprite properties and animations for
 * rendering, and a lifecycle that allows for destruction and revival.
 * 
 * @see DynamicGameObject
 * @see StaticGameObject
 * @see Vector2D
 * @see Collidable
 * @see RenderableAsSprite
 * @see Effect
 */
public abstract class GameObject implements RenderableAsSprite, Collidable {

    // Collision
    private Vector2D location;
    private Vector2D startingLocation;
    private Hitbox hitbox;

    private boolean destroyed = false;

    // Graphics
    protected String spriteSheetFilename = "";
    protected SpriteLocation defaultSpriteLocation = new SpriteLocation(0, 0);
    private double timeInAnimationState = 0.0;
    private String animationState = "default";
    protected HashMap<String, ArrayList<SpriteLocation>> animations = new HashMap<>();
    protected boolean freezeAnimations = false;
    protected double animationDuration = Configuration.ANIMATION_TIME;
    private double rotation;

    private ArrayList<Effect> effects = new ArrayList<>();
    protected HitboxOutline outlineEffect;

    /**
     * Create a new {@code GameObject} object at the location specified by the
     * {@code x} and {@code y} coordinates.
     * 
     * @param x the x location of the constructed object
     * @param y the y location of the constructed object
     */
    public GameObject(double x, double y) {
        this.location = new Vector2D(x, y);
        this.startingLocation = new Vector2D(x, y);
        this.hitbox = new Hitbox(this.location, new Vector2D(1, 1));
        this.outlineEffect = new HitboxOutline(this);
        this.initAnimations();
    }

    /**
     * Returns a {@code Vector2D} object representing the current location of this
     * {@code GameObject}. The returned reference is that of the internal
     * {@code Vector2D} object, so modifications to this {@code Vector2D} will
     * directly affect this {@code GameObject}'s location. This property can be
     * useful, but should be used carefully. In most cases, you should probably use
     * {@code Vector2D.copy()} on the returned vector.
     * <p>
     * Note that the location of an object is separate from the location of an
     * object's {@code Hitbox}. When dealing with collision, you should probably use
     * {@link #getHitbox()}.
     * 
     * @return a reference to the location of this object
     */
    public Vector2D getLocation() {
        return this.location;
    }

    /**
     * Sets the location of this object to the specified {@code x} and {@code y}
     * coordinates.
     * 
     * @param x the x location to be set
     * @param y the y location to be set
     */
    public void setLocation(double x, double y) {
        this.location.setX(x);
        this.location.setY(y);
    }

    /**
     * Returns a {@code Vector2D} object representing the dimensions of this
     * {@code GameObject}'s graphical sprite in world units (ie, not pixels).
     * <p>
     * Note that the these dimensions are strictly for graphics and rendering, and
     * are separate from the dimensions of an object's {@code Hitbox}. When dealing
     * with collision, you should use {@link #getHitbox()}.
     * 
     * @return a reference to the graphical dimensions of this object
     */
    public Vector2D getSpriteDimensions() {
        return new Vector2D(getSpriteWidth() * this.getSpriteScaleX() / Configuration.SPRITE_SIZE,
                getSpriteHeight() * this.getSpriteScaleY() / Configuration.SPRITE_SIZE);
    }

    public Vector2D getSpriteOrigin() {
        return new Vector2D(this.location.getX() + this.getSpriteOffsetX() / (double) Configuration.SPRITE_SIZE,
                this.location.getY() + this.getSpriteOffsetY() / (double) Configuration.SPRITE_SIZE);
    }

    /**
     * Returns a boolean representing whether this particular {@code GameObject} is
     * a {@code Player} object. This is useful for actions that should only be done
     * to the player.
     * 
     * @return whether this is the player
     */
    public boolean isPlayer() {
        return false;
    }

    /**
     * Marks the object as destroyed, so that it can be removed from the game.
     * Subclasses should override this method to perform any other actions
     * associated with an object being destroyed. This should primarily involve
     * modifying internal state, as graphical or sound effects should be applied
     * with {@link #onDestroy()}.
     */
    public void destroy() {
        this.destroyed = true;
        this.onDestroy();
    }

    /**
     * Intended to be called whenever an object is destroyed, this method should
     * apply any graphical or sound effects associated with this object's
     * destruction, or any other non-essential logic. It is separate from
     * {@link #destroy()} so that inherited logic can be maintained, but other
     * effects are not.
     * <p>
     * By default, this does nothing.
     */
    public void onDestroy() {

    }

    /**
     * Returns whether this object is destroyed.
     * 
     * @return whether this object is destroyed
     */
    public boolean isDestroyed() {
        return this.destroyed;
    }

    /**
     * Revive the object without fully resetting it. For some objects, there may not
     * be a difference between this and the {@link #reset()} method.
     */
    public void revive() {
        this.destroyed = false;
        this.onSpawn();
    }

    /**
     * Fully reset an object. For some objects, there may not be a difference
     * between this and the {@link #revive()} method.
     */
    public void reset() {
        this.destroyed = false;
        // Player location handled by level
        if (!this.isPlayer()) {
            this.setLocation(startingLocation.getX(), startingLocation.getY());
        }
        this.timeInAnimationState = 0;
        this.animationState = "default";
        this.freezeAnimations = false;
        this.onSpawn();
    }

    /**
     * Intended to be called whenever an object is spawned, or placed into a level,
     * this method should apply any graphical or sound effects associated with this
     * object's creation, or any other non-essential logic. It is separate from
     * {@link #revive()} and {@link #reset()} so that inherited logic can be
     * maintained, but other effects are not.
     * <p>
     * By default, this does nothing.
     */
    public void onSpawn() {

    }

    /**
     * Perform any updates necessary. All updates should be synced to the amount of
     * time since the last update {@code dt}. The current {@code Level} is also
     * provided, which may contain other useful or necessary properties.
     * <p>
     * Physics updates, including movement, are handled separately, and this method
     * should only update other properties specific to the object.
     * 
     * @param dt    the amount of time since the last update
     * @param level the current {@code Level}
     */
    public void update(double dt, Level level) {
        if (!this.freezeAnimations) {
            this.timeInAnimationState += dt;
        }
        this.effects.removeIf(Effect::isFinished);
    }

    /**
     * Returns this object's class name. This can be useful for actions that should
     * only affect certain classes. It should be used carefully, however, as
     * unexpected results can occur with complex inheritance patterns.
     * 
     * @return the name of this object's class
     */
    public String getObjectType() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns a list of graphical effects applied to this object. This can be used
     * to add, remove, or modify the effects of this object. Mostly, it should be
     * used to add, as updates and removal are handled by the object and by the
     * {@code Level}.
     * 
     * @return the list of {@code Effects}
     */
    public ArrayList<Effect> getEffects() {
        return effects;
    }

    /**
     * Adds a {@link HitboxOutline} effect to the object, with the location and
     * dimensions of the object's hitbox. This can be useful for debugging to view
     * the exact positions and sizes of various objects.
     */
    public void showHitbox() {
        if (!this.getEffects().contains(this.outlineEffect)) {
            this.getEffects().add(this.outlineEffect);
        }
    }

    /**
     * Returns the overall rotation of the object. This does not affect collisions
     * and hitboxes, only visual rotation. Note that rotation for specific frames of
     * animation can be set within that frame's {@link SpriteLocation}, and that
     * this rotation is applied on top of that.
     * 
     * @return the rotation of the object
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Sets the overall rotation of the object. This does not affect collisions and
     * hitboxes, only visual rotation. Note that rotation for specific frames of
     * animation can be set within that frame's {@link SpriteLocation}, and that
     * this rotation is applied on top of that.
     * 
     * @param rotation the rotation of the object
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    @Override
    public Hitbox getHitbox() {
        return this.hitbox;
    }

    @Override
    public String getSpriteSheetFilename() {
        return this.spriteSheetFilename;
    }

    @Override
    public ArrayList<SpriteLocation> getAnimations() {
        return this.animations.getOrDefault(this.animationState,
                new ArrayList<>(Arrays.asList(this.defaultSpriteLocation)));
    }

    @Override
    public double getAnimationDuration() {
        return animationDuration;
    }

    @Override
    public void setAnimationDuration(double animationDuration) {
        this.animationDuration = animationDuration;
    }

    @Override
    public SpriteLocation getCurrentSpriteLocation() {
        return this.getAnimations().get(
                ((int) (this.getTimeInAnimationState() / this.animationDuration)) % this.getAnimations().size());
    }

    @Override
    public double getTimeInAnimationState() {
        return this.timeInAnimationState;
    }

    @Override
    public void initAnimations() {

    }

    @Override
    public void freezeAnimations() {
        this.freezeAnimations = true;
    }

    @Override
    public void setAnimationState(String newState) {
        if (!this.animationState.equals(newState) && this.animations.containsKey(newState)) {
            this.animationState = newState;
        }
        this.freezeAnimations = false;
    }

    @Override
    public String getAnimationState() {
        return this.animationState;
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {

    }

    @Override
    public void collideWithStaticObject(StaticGameObject otherObject) {

    }

}
