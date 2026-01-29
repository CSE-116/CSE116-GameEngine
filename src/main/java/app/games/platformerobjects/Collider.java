package app.games.platformerobjects;

import app.gameengine.Level;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.Hitbox;
import app.gameengine.model.physics.PhysicsEngine;
import app.gameengine.model.physics.Vector2D;

/**
 * A special hitbox intended for use in platformers.
 * <p>
 * Provides an additional utility to determine if it is colliding with any
 * static objects. This can be useful for determining if certain objects are on
 * the ground or not.
 * 
 * @see Hitbox
 * @see PlatformerPlayer
 * @see PlatformerLevel
 */
public class Collider extends Hitbox {

    public Collider(Vector2D location, Vector2D dimensions) {
        super(location, dimensions);
    }

    public Collider(Vector2D baseLocation, Vector2D dimensions, Vector2D offset) {
        super(baseLocation, dimensions, offset);
    }

    public boolean checkCollision(PhysicsEngine engine, Level level) {
        for (StaticGameObject obj : level.getStaticObjects()) {
            if (obj.isSolid() && engine.detectCollision(this, obj.getHitbox())
                    && this.getLocation().getY() < obj.getLocation().getY()) {
                return true;
            }
        }
        return false;
    }

}
