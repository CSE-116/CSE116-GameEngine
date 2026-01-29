package app.gameengine.model.gameobjects;

import app.gameengine.Game;

/**
 * A static object which is added to the player's inventory on collision.
 * 
 * @see StaticGameObject
 * @see Player
 */
public abstract class Collectible extends StaticGameObject {

    public Collectible(double x, double y, Game game, String itemID) {
        super(x, y);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
