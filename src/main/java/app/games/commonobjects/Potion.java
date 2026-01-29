package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A static object that heals the player on collision.
 * 
 * @see StaticGameObject
 */
public class Potion extends StaticGameObject {

    public Potion(double x, double y, int heal) {
        super(x, y);
        this.spriteSheetFilename = "MiniWorldSprites/User Interface/Icons-Essentials.png";
        if (heal >= 0) {
            this.defaultSpriteLocation = new SpriteLocation(0, 1);
        } else {
            this.defaultSpriteLocation = new SpriteLocation(1, 1);
        }
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
