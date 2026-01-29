package app.games.mario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import app.display.common.BlankTile;
import app.display.common.SpriteGraphics;
import app.display.common.SpriteLocation;
import app.display.common.effects.TimedEffect;
import app.gameengine.Level;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.Vector2D;
import app.games.platformerobjects.Collider;
import app.games.topdownobjects.Enemy;

/**
 * An enemy shaped like a mushroom who canonically betrayed the mushroom kingdom.
 * 
 * @see Enemy
 * @see Koopa
 * @see MarioLevel
 * @see MarioGame
 */
public class Goomba extends Enemy {

    protected static final double DEATH_EFFECT_DURATION = 2;
    protected Collider collider;
    protected Collider leftCollider;
    protected Collider rightCollider;

    public Goomba(double x, double y) {
        super(x, y, 1, 1);
        this.spriteSheetFilename = "mario/smb_enemies_square.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
        this.collider = new Collider(this.getLocation(), new Vector2D(1, 0.2), new Vector2D(0, 0.81));
        this.leftCollider = new Collider(this.getLocation(), new Vector2D(1, 0.2), new Vector2D(-1, 0.81));
        this.rightCollider = new Collider(this.getLocation(), new Vector2D(1, 0.2), new Vector2D(1, 0.81));
        this.setOrientation(1, 0);
        this.getEffects().clear();
    }

    public Collider getCollider() {
        return this.collider;
    }

    @Override
    public void onDestroy() {
        this.getEffects().add(new TimedEffect(new SpriteGraphics(new BlankTile(this.getLocation().getX(),
                this.getLocation().getY(), "mario/smb_enemies_square.png", new SpriteLocation(1, 0))),
                DEATH_EFFECT_DURATION));
    }

    @Override
    public void initAnimations() {
        this.animations = new HashMap<>();
        this.animations.put("default", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 0),
                new SpriteLocation(0, 0, true, false))));
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        if (otherObject.isPlayer() && otherObject.getVelocity().getY() > 0
                && otherObject.getLocation().getY() < this.getLocation().getY()) {
            this.destroy();
            otherObject.getVelocity().setY(-10);
        } else if (otherObject.isPlayer()) {
            otherObject.destroy();
        } else {
            this.getOrientation().setX(-this.getOrientation().getX());
        }
    }

    @Override
    public void collideWithStaticObject(StaticGameObject otherObject) {
        this.getOrientation().setX(-this.getOrientation().getX());
    }

    @Override
    public void update(double dt, Level level) {
        super.update(dt, level);
        if (this.collider.checkCollision(level.getPhysicsEngine(), level)) {
            this.setOnGround(true);
            Collider collider = this.getOrientation().getX() < 0 ? this.leftCollider : this.rightCollider;
            if (!collider.checkCollision(level.getPhysicsEngine(), level)) {
                this.getOrientation().setX(-this.getOrientation().getX());
            }
        } else {
            this.setOnGround(false);
        }
        this.getVelocity().setX(this.getOrientation().getX() * this.movementSpeed);
    }

    @Override
    public void reset() {
        super.reset();
        this.setOrientation(1, 0);
    }

}
