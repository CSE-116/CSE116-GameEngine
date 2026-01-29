package app.display.minesweeper;

import app.Settings;
import app.display.common.SpriteGraphics;
import app.display.common.ui.UIElement;
import app.gameengine.Level;
import app.games.minesweeper.Face;
import app.games.minesweeper.MinesweeperGame;
import app.games.minesweeper.MinesweeperLevel;
import app.games.minesweeper.MinesweeperLevel.GameState;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Displays the face for a game of Minesweeper.
 * <p>
 * Intended to be placed at the top center of the UI in a game of Minesweeper.
 * The face that is displayed depends on the current state of the game. A
 * different face is used when clicking a game tile, and when the game is won or
 * lost.
 * <p>
 * When the face is clicked, the level should be reset.
 * 
 * @see Face
 * @see MinesweeperGame
 * @see MinesweeperLevel
 * @see UIElement
 * @see MinesweeperUI
 * @see SpriteGraphics
 */
public class FaceDisplay extends UIElement {

    private Face face;
    private SpriteGraphics graphic;
    private MinesweeperGame game;
    private boolean pressed;

    /**
     * Constructs a display showing the {@link Face} object for a game of
     * minesweeper.
     * 
     * @param game the associated game
     */
    public FaceDisplay(MinesweeperGame game) {
        this.game = game;
        this.face = new Face();
        this.graphic = new SpriteGraphics(face);
        StackPane.setAlignment(graphic, Pos.CENTER);
        this.graphic.setOnMousePressed(this::faceClick);
        this.graphic.setOnMouseReleased(this::faceClick);
    }

    private void faceClick(MouseEvent event) {
        if (Settings.paused()) {
            return;
        }
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            this.pressed = true;
            face.getCurrentSpriteLocation().setColumn((face.getCurrentSpriteLocation().getColumn() + 2) % 4);
            this.graphic.resetSpriteViewport(this.face);
            return;
        }
        this.pressed = false;
        this.game.resetCurrentLevel();
    }

    @Override
    public Node getRenderable() {
        return this.graphic;
    }

    @Override
    public void update(double dt, Level level) {
        if (this.pressed) {
            return;
        }
        if (this.game.getGameState() == GameState.WIN) {
            face.getCurrentSpriteLocation().setColumn(0);
            face.getCurrentSpriteLocation().setRow(1);
        } else if (this.game.getGameState() == GameState.CLICK) {
            face.getCurrentSpriteLocation().setColumn(1);
            face.getCurrentSpriteLocation().setRow(0);
        } else if (this.game.getGameState() == GameState.LOSE) {
            face.getCurrentSpriteLocation().setColumn(1);
            face.getCurrentSpriteLocation().setRow(1);
        } else {
            face.getCurrentSpriteLocation().setColumn(0);
            face.getCurrentSpriteLocation().setRow(0);
        }
        this.graphic.resetSpriteViewport(this.face);
    }

}
