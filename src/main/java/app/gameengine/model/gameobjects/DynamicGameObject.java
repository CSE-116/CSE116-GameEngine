package app.gameengine.model.gameobjects;

import app.display.common.sound.AudioManager;
import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.model.physics.Vector2D;
import app.games.commonobjects.Projectile;

/**
 * A {@link GameObject} capable of movement.
 * <p>
 * One of the two main subsets of {@code GameObject}s, objects of this class are
 * capable of movement, through storing both velocity and orientation. They are
 * have health and are capable of taking damage and dying.
 * 
 * @see GameObject
 * @see StaticGameObject
 * @see Game
 * @see Level
 */
public abstract class DynamicGameObject extends GameObject {

    protected int maxHP;
    protected int hp;
    private boolean isOnGround = false;
    private final Vector2D velocity = new Vector2D(0.0, 0.0);
    private final Vector2D orientation = new Vector2D(0.0, 0.0);

    /**
     * Constructs a player with the given location and max HP.
     * 
     * @param x     the x location of the object
     * @param y     the y location of the object
     * @param maxHP the maximum HP of the object
     */
    public DynamicGameObject(double x, double y, int maxHP) {
        super(x, y);
        this.maxHP = maxHP;
        this.hp = this.maxHP;
    }

    /**
     * Fire the provided projectile at the given speed, and add it to the given
     * level. Firing a projectile means setting it's orientation to the same as this
     * {@code DynamicGameObject}'s, and it's velocity to be in that direction.
     * 
     * @param projectile the projectile to be fired
     * @param speed      the speed of the projectile
     * @param level      the level the projectile is fired within
     */
    public void fireProjectile(Projectile projectile, double speed, Level level) {
        projectile.setVelocity(this.getOrientation().getX() * speed, this.getOrientation().getY() * speed);
        Vector2D orientation = Vector2D.normalize(this.orientation);
        projectile.setOrientation(orientation.getX(), orientation.getY());
        projectile.setLocation(this.getLocation().getX() + orientation.getX(),
                this.getLocation().getY() + orientation.getY());
        level.addDynamicObject(projectile);
        AudioManager.playSoundEffect("shoot.wav");
    }

    /**
     * Returns a {@code Vector2D} object representing the current velocity of this
     * {@code DynamicGameObject}. The returned reference is that of the internal
     * {@code Vector2D} object, so modifications to this {@code Vector2D} will
     * directly affect this {@code DynamicGameObject}'s velocity. This property can
     * be useful, but should be used carefully. In most cases, you should probably
     * use {@code Vector2D.copy()} on the returned vector.
     * 
     * @return a reference to the velocity of this object
     */
    public Vector2D getVelocity() {
        return this.velocity;
    }

    /**
     * Sets the velocity of this object to the specified {@code x} and {@code y}
     * values.
     * 
     * @param x the x velocity to be set
     * @param y the y velocity to be set
     */
    public void setVelocity(double x, double y) {
        this.velocity.setX(x);
        this.velocity.setY(y);
    }

    /**
     * Returns a {@code Vector2D} object representing the current orientation of
     * this {@code DynamicGameObject}. The returned reference is that of the
     * internal {@code Vector2D} object, so modifications to this {@code Vector2D}
     * will directly affect this {@code DynamicGameObject}'s orientation. This
     * property can be useful, but should be used carefully. In most cases, you
     * should probably use {@code Vector2D.copy()} on the returned vector.
     * 
     * @return a reference to the orientation of this object
     */
    public Vector2D getOrientation() {
        return this.orientation;
    }

    /**
     * Sets the orientation of this object to the specified {@code x} and {@code y}
     * values.
     * 
     * @param x the x velocity to be set
     * @param y the y velocity to be set
     */
    public void setOrientation(double x, double y) {
        this.orientation.setX(x);
        this.orientation.setY(y);
    }

    public void setHP(int hp) {
        this.hp = Math.min(hp, this.maxHP);
    }

    public int getHP() {
        return hp;
    }

    public int getMaxHP() {
        return maxHP;
    }

    /**
     * Decrease this object's health by the given amount, as long as that amount is
     * greater than 0.
     * 
     * @param damage damage to deal
     */
    public void takeDamage(int damage) {
        if (damage < 0) {
            return;
        }
        this.hp -= damage;
    }

    /**
     * Returns a boolean representing whether the object is currently on the ground.
     * No calculations are performed to determine this, and that must be handled
     * elsewhere.
     * 
     * @return {@code true} is the object is on the ground, {@code false} otherwise
     */
    public boolean isOnGround() {
        return isOnGround;
    }

    /**
     * Sets whether the object is currently on the ground.
     * 
     * @param onGround whether the object is on the ground
     */
    public void setOnGround(boolean onGround) {
        isOnGround = onGround;
    }

    @Override
    public void update(double dt, Level level) {
        super.update(dt, level);
        if (this.hp <= 0) {
            this.destroy();
        }
    }

    @Override
    public void revive() {
        super.revive();
        this.hp = this.maxHP;
        this.setVelocity(0, 0);
    }

    @Override
    public void reset() {
        super.reset();
        this.hp = maxHP;
        this.setVelocity(0, 0);
        this.setOrientation(0, 0);
        this.isOnGround = false;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
