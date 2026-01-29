package app.games.mario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import app.display.common.BlankTile;
import app.display.common.SpriteGraphics;
import app.display.common.SpriteLocation;
import app.display.common.effects.TimedEffect;
import app.gameengine.Level;
import app.gameengine.model.physics.Vector2D;
import app.games.platformerobjects.Collider;

/**
 * An enemy in Mario that is a turtle.
 * 
 * @see Goomba
 * @see Enemy
 * @see MarioLevel
 * @see MarioGame
 */
public class Koopa extends Goomba {

    public Koopa(double x, double y) {
        super(x, y);
        this.spriteSheetFilename = "mario/smb_enemies_tall.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
        this.collider = new Collider(this.getLocation(), new Vector2D(1, 0.2), new Vector2D(0, 1.31));
        this.leftCollider = new Collider(this.getLocation(), new Vector2D(1, 0.2), new Vector2D(-1, 1.31));
        this.rightCollider = new Collider(this.getLocation(), new Vector2D(1, 0.2), new Vector2D(1, 1.31));
        this.getHitbox().setDimensions(1, 1.5);
        this.setAnimationState("move_left");
    }

    @Override
    public void onDestroy() {
        this.getEffects().add(new TimedEffect(new SpriteGraphics(new BlankTile(this.getLocation().getX(),
                this.getLocation().getY(), "mario/smb_enemies_tall.png", new SpriteLocation(5, 0), getSpriteWidth(),
                getSpriteHeight())), DEATH_EFFECT_DURATION));
    }

    @Override
    public void initAnimations() {
        super.initAnimations();
        this.animations = new HashMap<>();
        this.animations.put("move_left", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 0),
                new SpriteLocation(1, 0))));
        this.animations.put("move_right", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 0, true, false),
                new SpriteLocation(1, 0, true, false))));
    }

    @Override
    public void update(double dt, Level level) {
        super.update(dt, level);
        this.setAnimationState(this.getOrientation().getX() > 0 ? "move_right" : "move_left");
    }

    @Override
    public int getSpriteHeight() {
        return 24;
    }

    @Override
    public int getSpriteWidth() {
        return 16;
    }

}
