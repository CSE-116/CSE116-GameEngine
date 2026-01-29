package app.games.commonobjects;

import app.gameengine.Level;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * An abstract projectile which damages enemies.
 * <p>
 * Intended to be extended for other kinds of projectiles, for both the player
 * and enemies.
 * 
 * @see PlayerMagicProjectile
 * @see PlayerAxeProjectile
 * @see EnemyArrowProjectile
 * @see EnemyHomingProjectile
 */
public abstract class Projectile extends DynamicGameObject {

    private int damage;

    public Projectile(double x, double y, int damage) {
        super(x, y, 20);
        this.damage = damage;
        this.setOnGround(true);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void update(double dt, Level level) {
        super.update(dt, level);
        this.setRotation(this.getOrientation().rotation() - 90);
    }

    @Override
    public void collideWithStaticObject(StaticGameObject otherObject) {
        if (otherObject.isSolid()) {
            this.destroy();
        }
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        if (!otherObject.isPlayer()) {
            otherObject.takeDamage(this.damage);
            this.destroy();
        }
    }

}
