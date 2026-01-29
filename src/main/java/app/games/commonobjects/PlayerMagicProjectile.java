package app.games.commonobjects;

import app.display.common.SpriteLocation;

/**
 * A low damage projectile which only damages enemies.
 * 
 * @see Projectile
 * @see MagicPickup
 */
public class PlayerMagicProjectile extends Projectile {

    public PlayerMagicProjectile(double x, double y) {
        super(x, y, 35);
        this.getHitbox().setDimensions(0.5, 0.5);
        this.getHitbox().setOffset(0.25, 0.25);
        this.spriteSheetFilename = "MiniWorldSprites/Objects/Bullet.png";
        this.defaultSpriteLocation = new SpriteLocation(1, 1);
    }

}
