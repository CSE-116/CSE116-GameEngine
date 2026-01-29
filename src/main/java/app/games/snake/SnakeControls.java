package app.games.snake;

import java.util.HashMap;
import java.util.Map;

import app.Settings;
import app.display.common.controller.KeyboardControls;
import app.gameengine.Game;
import app.gameengine.model.physics.Vector2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Keyboard controls for Snake.
 * <p>
 * These are relatively simple controls that maintain velocity in the most
 * recently pressed direction. Upon pressing a new direction, it is queued, and
 * as soon as that is a valid direction of movement it is performed, allowing
 * for quick turnarounds.
 * 
 * @see KeyboardControls
 * @see SnakeGame
 * @see SnakeLevel
 */
public class SnakeControls extends KeyboardControls {

    private static HashMap<String, Vector2D> directionToVector = new HashMap<>(Map.of(
            "up", new Vector2D(0, -1),
            "down", new Vector2D(0, 1),
            "left", new Vector2D(-1, 0),
            "right", new Vector2D(1, 0)));

    private Vector2D previousDirection;
    private Vector2D currentDirection;

    public SnakeControls(Game game) {
        super(game);
    }

    @Override
    public void handle(KeyEvent event) {
        super.handle(event);

        if (event.getEventType() != KeyEvent.KEY_PRESSED) {
            return;
        }

        boolean upPressed = event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W;
        boolean downPressed = event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S;
        boolean leftPressed = event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A;
        boolean rightPressed = event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D;

        if (upPressed || downPressed || leftPressed || rightPressed && Settings.paused()) {
            this.game.unpause();
        }

        if (upPressed) {
            this.previousDirection = this.currentDirection;
            this.currentDirection = directionToVector.get("up");
        } else if (downPressed) {
            this.previousDirection = this.currentDirection;
            this.currentDirection = directionToVector.get("down");
        } else if (leftPressed) {
            this.previousDirection = this.currentDirection;
            this.currentDirection = directionToVector.get("left");
        } else if (rightPressed) {
            this.previousDirection = this.currentDirection;
            this.currentDirection = directionToVector.get("right");
        }
    }

    @Override
    public void processInput(double dt) {
        if (isValidDirection(this.previousDirection)) {
            this.game.getPlayer().setOrientation(this.previousDirection.getX(), this.previousDirection.getY());
            this.previousDirection = null;
        } else if (isValidDirection(this.currentDirection)) {
            this.game.getPlayer().setOrientation(this.currentDirection.getX(), this.currentDirection.getY());
            this.currentDirection = null;
            this.previousDirection = null;
        }
    }

    private boolean isValidDirection(Vector2D v) {
        return v != null && !v.equals(this.game.getPlayer().getOrientation())
                && !v.equals(Vector2D.negate(this.game.getPlayer().getOrientation()));
    }

}
