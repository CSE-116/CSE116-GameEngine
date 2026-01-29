package app.games.commonobjects;

import java.util.ArrayList;
import java.util.Arrays;

import app.display.common.SpriteLocation;

/**
 * A high damage projectile which only damages enemies.
 * 
 * @see Projectile
 * @see PlayerAxePickup
 */
public class PlayerAxeProjectile extends Projectile {

    public PlayerAxeProjectile(double x, double y) {
        super(x, y, 60);
        this.getHitbox().setDimensions(0.7, 0.7);
        this.getHitbox().setOffset(0.15, 0.15);
        this.spriteSheetFilename = "MiniWorldSprites/Objects/Axe.png";
        this.defaultSpriteLocation = new SpriteLocation(1, 1);
        this.animations.put("default", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 0),
                new SpriteLocation(1, 0),
                new SpriteLocation(2, 0),
                new SpriteLocation(3, 0),
                new SpriteLocation(0, 1),
                new SpriteLocation(1, 1),
                new SpriteLocation(2, 1),
                new SpriteLocation(3, 1))));
    }

}
