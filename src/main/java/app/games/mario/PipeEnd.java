package app.games.mario;

import app.display.common.SpriteLocation;
import app.games.commonobjects.Wall;

/**
 * The end of a pipe in Mario.
 * 
 * @see PipeStem
 * @see Wall
 */
public class PipeEnd extends Wall {

    public PipeEnd(double x, double y) {
        super(x, y);
        this.spriteSheetFilename = "mario/smb_pipes.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
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
