package app.games.minesweeper;

import app.display.common.SpriteLocation;
import app.display.minesweeper.FaceDisplay;
import app.display.minesweeper.MinesweeperStyle;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A Minesweeper tile that shows one of the face sprites.
 * <p>
 * Logic for displaying the different faces is contained within
 * {@link FaceDisplay}
 * 
 * @see FaceDisplay
 */
public class Face extends StaticGameObject {

    public Face() {
        super(0, 0);
        this.spriteSheetFilename = MinesweeperStyle.FACE_FILE;
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
    }

    @Override
    public int getSpriteWidth() {
        return 32;
    }

    @Override
    public int getSpriteHeight() {
        return 32;
    }

}
