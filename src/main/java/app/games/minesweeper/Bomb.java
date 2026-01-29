package app.games.minesweeper;

import app.display.common.SpriteLocation;
import app.display.minesweeper.MinesweeperStyle;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A bomb in a game of Minesweeper.
 */
public class Bomb extends StaticGameObject {

    public Bomb(double x, double y) {
        super(x, y);
        this.spriteSheetFilename = MinesweeperStyle.TILE_FILE;
        this.defaultSpriteLocation = new SpriteLocation(4, 1);
    }

    public void detonate() {
        this.defaultSpriteLocation.setColumn(5);
    }

}
