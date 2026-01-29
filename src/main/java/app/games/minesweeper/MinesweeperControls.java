package app.games.minesweeper;

import java.util.ArrayList;
import java.util.HashMap;

import app.Settings;
import app.display.common.controller.MouseControls;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.GameUtils;
import app.games.minesweeper.CoverTile.TileState;
import app.games.minesweeper.MinesweeperLevel.GameState;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Mouse controls for the Minesweeper Game.
 * <p>
 * Handles basic behavior depending on click type, including changing face
 * display state and clicked tile display state. Specific click behavior, such
 * as uncovering tiles and bombs, should be handled from within the
 * {@link MinesweeperGame} and {@link MinesweeperLevel} classes.
 * <p>
 * Left click is used to uncover a tile, revealing it as either a bomb or a safe
 * tile. If a tile is marked as flagged or questioned, a left click does
 * nothing. A left click is performed on mouse release, so you may safely move
 * the mouse to another location to avoid an accidental click.
 * <p>
 * Right click is used to mark a tile as flagged or questioned. It rotates
 * between standard -> flagged -> questioned -> standard. However, this behavior
 * is not handled within this class
 * <p>
 * Middle click is used to uncover a tile, as well as all surrounding tiles
 * (immediately adjacent and diagonal). As with left click, this does not
 * include tiles that are flagged or questioned, and the operation is only
 * performed on mouse release.
 * 
 * @see MinesweeperGame
 * @see MinesweeperLevel
 * @see MouseControls
 */
public class MinesweeperControls extends MouseControls {

    private MinesweeperGame game;
    private Vector2D clickLocation;
    private ArrayList<Vector2D> middleClickLocations = new ArrayList<>();

    public MinesweeperControls(MinesweeperGame game) {
        super(game);
        this.game = game;
    }

    @Override
    public void handle(MouseEvent event) {
        super.handle(event);

        if (Settings.paused()) {
            return;
        }

        Vector2D gameLoc = Vector2D
                .floor(this.game.screenSpaceToWorldSpace(this.mouseStates.get(event.getButton()).getValue()));

        GameState currentState = this.game.getCurrentLevel().getState();
        // Win/lose controls disabled, must click face
        if (currentState == GameState.WIN || currentState == GameState.LOSE) {
            return;
        }
        // Set state for face
        if (GameUtils.isInBounds(this.game.getCurrentLevel(), gameLoc)) {
            this.setGameState(event, this.game.getCurrentLevel().getState());
        }
        HashMap<Vector2D, CoverTile> hiddenTiles = this.game.getCurrentLevel().getHiddenTiles();
        CoverTile targetTile = hiddenTiles.get(gameLoc);
        // Press/release controls
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (event.getButton() == MouseButton.PRIMARY && targetTile != null && !targetTile.isFlagged()
                    && !(targetTile.getTileState() == TileState.QUESTION)) {
                this.clickLocation = gameLoc;
                targetTile.setState(TileState.INVISIBLE);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                this.game.getCurrentLevel().handleRightClick(gameLoc);
            } else if (event.getButton() == MouseButton.MIDDLE) {
                this.clickLocation = gameLoc;
                this.middleClickLocations.clear();
                ArrayList<Vector2D> adj = this.game.getCurrentLevel().getAdjacentVectors(gameLoc);
                if (targetTile != null) {
                    adj.add(targetTile.getLocation());
                }
                for (Vector2D v : adj) {
                    if (hiddenTiles.containsKey(v) && !hiddenTiles.get(v).isFlagged()
                            && !(hiddenTiles.get(v).getTileState() == TileState.QUESTION)) {
                        this.middleClickLocations.add(v);
                        hiddenTiles.get(v).setState(TileState.INVISIBLE);
                    }
                }
            }
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED && clickLocation != null) {
            if (!clickLocation.equals(gameLoc)) {
                if (hiddenTiles.containsKey(clickLocation)
                        && hiddenTiles.get(clickLocation).getTileState() == TileState.INVISIBLE) {
                    hiddenTiles.get(clickLocation).setState(TileState.COVER);
                }
                for (Vector2D v : middleClickLocations) {
                    if (hiddenTiles.containsKey(v) && hiddenTiles.get(v).getTileState() == TileState.INVISIBLE) {
                        hiddenTiles.get(v).setState(TileState.COVER);
                    }
                }
                return;
            } else if (event.getButton() == MouseButton.PRIMARY) {
                this.game.getCurrentLevel().handleLeftClick(clickLocation);
            } else if (event.getButton() == MouseButton.MIDDLE) {
                for (Vector2D v : this.middleClickLocations) {
                    this.game.getCurrentLevel().handleLeftClick(v);
                }
                this.middleClickLocations.clear();
            }
        }
    }

    private void setGameState(MouseEvent event, GameState state) {
        // Don't change game state on win or lose (must click face)
        if (state == GameState.WIN || state == GameState.LOSE) {
            return;
        } else if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            this.game.getCurrentLevel().setState(GameState.CLICK);
        } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            this.game.getCurrentLevel().setState(GameState.PLAYING);
        }
    }

}
