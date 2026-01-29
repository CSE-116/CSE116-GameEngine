package app.games.minesweeper;

import app.display.common.SpriteLocation;
import app.display.minesweeper.MinesweeperStyle;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A Minesweeper tile that displays the number of nearby bombs.
 */
public class NumberTile extends StaticGameObject {

    public NumberTile(double x, double y, int num) {
        super(x, y);
        this.spriteSheetFilename = MinesweeperStyle.TILE_FILE;
        this.defaultSpriteLocation = new SpriteLocation(num, 0);
    }

}
