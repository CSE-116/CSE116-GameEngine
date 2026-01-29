package app.games.minesweeper;

import app.display.common.SpriteLocation;
import app.display.minesweeper.MinesweeperStyle;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A Minesweeper tile that hides the tile underneath, or displays a flag.
 */
public class CoverTile extends StaticGameObject {

    private TileState state = TileState.COVER;

    /**
     * The possible states that a {@code CoverTile} can be in.
     */
    public enum TileState {
        COVER, INVISIBLE, FLAGGED, FLAGGEDWRONG, QUESTION
    }

    public CoverTile(double x, double y) {
        super(x, y);
        this.spriteSheetFilename = MinesweeperStyle.TILE_FILE;
        this.defaultSpriteLocation = new SpriteLocation(0, 1);
    }

    /**
     * Returns the current state of this tile.
     * 
     * @return the tile state
     */
    public TileState getTileState() {
        return this.state;
    }

    /**
     * Returns whether this tile is in a flagged state. This is equivalent to
     * checking if the value of {@link #getTileState()} equals
     * {@link TileState#FLAGGED}, but may be more convenient.
     * 
     * @return whether this tile is flagged
     */
    public boolean isFlagged() {
        return this.state == TileState.FLAGGED;
    }

    /**
     * Sets the state of this tile to {@code state}, and updates the sprite
     * appropriately.
     * 
     * @param state the state to enter
     */
    public void setState(TileState state) {
        this.state = state;
        switch (this.state) {
            case COVER:
                this.defaultSpriteLocation.setColumn(0);
                this.defaultSpriteLocation.setRow(1);
                break;
            case INVISIBLE:
                this.defaultSpriteLocation.setColumn(0);
                this.defaultSpriteLocation.setRow(0);
                break;
            case FLAGGED:
                this.defaultSpriteLocation.setColumn(1);
                this.defaultSpriteLocation.setRow(1);
                break;
            case FLAGGEDWRONG:
                this.defaultSpriteLocation.setColumn(3);
                this.defaultSpriteLocation.setRow(1);
                break;
            case QUESTION:
                this.defaultSpriteLocation.setColumn(2);
                this.defaultSpriteLocation.setRow(1);
                break;
        }
    }

}
