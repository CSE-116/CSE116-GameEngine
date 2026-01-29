package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * An instant-death spike obstacle.
 * 
 * @see StaticGameObject
 */
public class Spike extends StaticGameObject {

    public Spike(double x, double y) {
        super(x, y);
        this.spriteSheetFilename = "MiniWorldSprites/User Interface/UiIcons.png";
        this.defaultSpriteLocation = new SpriteLocation(2, 10);
        this.getHitbox().setDimensions(0.8, 0.8);
        this.getHitbox().setOffset(0.1, 0.1);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
