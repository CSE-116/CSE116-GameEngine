package app.games.mario;

import app.display.common.SpriteLocation;
import app.gameengine.Game;
import app.games.commonobjects.Goal;

/**
 * A goal in Mario, which transitions the player to the next level.
 * 
 * @see Goal
 * @see MarioLevel
 * @see MarioGame
 */
public class Flag extends Goal {

    public Flag(double x, double y, Game game) {
        super(x, y, game);
        this.spriteSheetFilename = "mario/smb_flags.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
        this.getHitbox().setDimensions(1, 10.5);
    }

    @Override
    public int getSpriteHeight() {
        return 176;
    }

    @Override
    public int getSpriteWidth() {
        return 24;
    }

    @Override
    public int getSpriteOffsetX() {
        return -8;
    }

    @Override
    public int getSpriteOffsetY() {
        return -8;
    }

}
