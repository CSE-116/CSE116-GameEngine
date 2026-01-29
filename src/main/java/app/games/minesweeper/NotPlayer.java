package app.games.minesweeper;

import java.util.HashMap;

import app.gameengine.model.gameobjects.Player;

/**
 * A player that is invisible, for use in games that do not have a real player.
 */
public class NotPlayer extends Player {

    public NotPlayer() {
        super(0, 0, 1);
        this.spriteSheetFilename = "minesweeper/notPlayer.png";
        this.animations = new HashMap<>();
    }

    @Override
    public void showHitbox() {

    }

}
