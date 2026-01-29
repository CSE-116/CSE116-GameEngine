package app.display.common.controller;

import java.util.HashMap;

import app.Configuration;
import app.Settings;
import app.display.common.effects.FancyTextEffect;
import app.display.common.effects.PowerupEffect;
import app.gameengine.Game;
import app.gameengine.model.physics.Vector2D;
import app.games.commonobjects.Projectile;
import app.games.topdownobjects.Enemy;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 * Base controls class for handling keyboard input in a game.
 * <p>
 * This class manages key state tracking and handles standard game controls,
 * including pausing, inventory controls, and debug controls. It is intended to
 * be extended by more specific control classes for different game types.
 * 
 * @see Game
 * @see MouseControls
 */
public class KeyboardControls implements EventHandler<KeyEvent> {

    private static final String descriptions = String.join("\n",
            "Q: Show this page",
            "ESC: Pause/unpause",
            "P: Pause/unpause without showing menu",
            "SPACE: Take level action",
            "TAB: Cycle inventory",
            "F1: Reset level",
            "F2: Reset game",
            "F3: Advance level",
            "F4: Show/hide hitboxes",
            "F5: Show/hide enemy paths",
            "F6: Toggle god mode",
            "F7: Toggle noclip",
            "F8: Kill all",
            "F9: Close game");
    private final FancyTextEffect descriptionEffect;

    private static final Color GOD_FILL_COLOR = Color.CYAN.deriveColor(0, 1, 1, 0.25);
    private static final Color GOD_OUTLINE_COLOR = Color.BLUE.deriveColor(0, 1, 1, 0.25);
    private static final Color NOCLIP_FILL_COLOR = Color.RED.deriveColor(0, 1, 1, 0.25);
    private static final Color NOCLIP_OUTLINE_COLOR = Color.DARKRED.deriveColor(0, 1, 1, 0.5);

    protected Game game;
    protected HashMap<KeyCode, Boolean> keyStates = new HashMap<>();

    /**
     * Constructs a new {@code KeyboardControls} instance for the specified
     * {@code Game}.
     * 
     * @param game the {@code Game} instance to be controlled
     */
    public KeyboardControls(Game game) {
        this.game = game;
        this.descriptionEffect = new FancyTextEffect(new Vector2D(0, 0), 10, descriptions);
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            this.keyStates.put(event.getCode(), true);
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            this.keyStates.put(event.getCode(), false);
        }

        if (event.getEventType() != KeyEvent.KEY_PRESSED) {
            return;
        }
        // Normal controls
        if (event.getCode() == KeyCode.F9) {
            Platform.exit();
        } else if (event.getCode() == KeyCode.P) {
            Settings.togglePaused();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            Settings.togglePaused();
            if (Settings.paused()) {
                this.game.pause();
            } else {
                this.game.unpause();
            }
        }
        // Pausable controls
        if (!Settings.paused()) {
            if (event.getCode() == KeyCode.SPACE) {
                this.game.getCurrentLevel().actionButtonPressed();
            } else if (event.getCode() == KeyCode.TAB) {
                this.game.getPlayer().cycleInventory();
            }
        }
        // Debug controls
        if (event.getEventType() == KeyEvent.KEY_PRESSED && Configuration.DEBUG_MODE) {
            if (event.getCode() == KeyCode.Q) {
                this.descriptionEffect.reset();
                this.game.getCurrentLevel().getActiveEffects().put(this.descriptionEffect,
                        this.game.getPlayer().getLocation());
            } else if (event.getCode() == KeyCode.F1) {
                this.game.resetCurrentLevel();
            } else if (event.getCode() == KeyCode.F2) {
                this.game.resetGame();
            } else if (event.getCode() == KeyCode.F3) {
                this.game.advanceLevel();
            } else if (event.getCode() == KeyCode.F4) {
                Settings.toggleShowHitboxes();
            } else if (event.getCode() == KeyCode.F5) {
                Settings.toggleShowPaths();
            } else if (event.getCode() == KeyCode.F6) {
                Settings.toggleGodMode();
                this.game.getPlayer().getEffects()
                        .add(new PowerupEffect(GOD_FILL_COLOR, GOD_OUTLINE_COLOR, Settings::godMode));
            } else if (event.getCode() == KeyCode.F7) {
                Settings.toggleNoclip();
                this.game.getPlayer().getEffects()
                        .add(new PowerupEffect(NOCLIP_FILL_COLOR, NOCLIP_OUTLINE_COLOR, Settings::noclip));
            } else if (event.getCode() == KeyCode.F8) {
                this.game.getCurrentLevel().getDynamicObjects().forEach(obj -> {
                    if (obj instanceof Enemy || obj instanceof Projectile)
                        obj.destroy();
                });
            }
        }
    }

    /**
     * Process any inputs that have been sent since the last frame, and update
     * game/player state accordingly.
     * <p>
     * This method is not guaranteed to do anything, as it's possible to perform all
     * necessary operations immediately when the key is pressed. However, this
     * method is often necessary to sync operations with dt.
     * 
     * @param dt the time elapsed since the last frame, in seconds
     */
    public void processInput(double dt) {

    }

    /**
     * Reset the controls to their initial state. Namely, this means clearing the
     * keys currently being pressed.
     */
    public void reset() {
        keyStates.clear();
    }

}
