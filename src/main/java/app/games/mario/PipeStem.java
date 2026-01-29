package app.games.mario;

import app.display.common.SpriteLocation;
import app.games.commonobjects.Wall;

/**
 * The stem of a pipe in Mario.
 * 
 * @see PipeEnd
 * @see Wall
 */
public class PipeStem extends Wall {

    public PipeStem(double x, double y) {
        super(x, y);
        this.spriteSheetFilename = "mario/smb_pipes.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 1);
        this.getHitbox().setDimensions(2, 1);
    }

    @Override
    public int getSpriteHeight() {
        return 16;
    }

    @Override
    public int getSpriteWidth() {
        return 32;
    }

}
