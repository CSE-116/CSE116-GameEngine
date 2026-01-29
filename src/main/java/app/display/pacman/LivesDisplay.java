package app.display.pacman;

import app.Configuration;
import app.display.common.FontManager;
import app.display.common.JFXManager;
import app.display.common.PlaceholderNode;
import app.display.common.ui.UIElement;
import app.gameengine.Level;
import app.games.pacman.Pacman;
import app.games.pacman.PacmanGame;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Text display for Pacman that shows the number of remaining lives.
 * <p>
 * Simple text element that uses a pixely font to display Pacman's remaining
 * lives.
 * 
 * @see Pacman
 * @see PacmanGame
 * @see UIElement
 */
public class LivesDisplay extends UIElement {

    private Node root;
    private Label label;
    private PacmanGame game;

    /**
     * Creates a display for the number of lives in a game of Pacman.
     * 
     * @param game the game of Pacman
     */
    public LivesDisplay(PacmanGame game) {
        this.game = game;
        if (!JFXManager.isInitialized()) {
            this.root = new PlaceholderNode();
            return;
        }
        this.label = new Label("LIVES: 0");
        this.label.setFont(FontManager.getFont("Minecraft.ttf", 20 * Configuration.TEXT_SCALE));

        this.label.setTextAlignment(TextAlignment.CENTER);
        this.label.setTextFill(Color.WHITE);
        this.root = this.label;
    }

    @Override
    public Node getRenderable() {
        return this.root;
    }

    @Override
    public void update(double dt, Level level) {
        if (this.label == null) {
            return;
        }
        int lives = this.game.getCurrentLevel().getPlayer().getLives();
        this.label.setText(String.format("LIVES: %d", lives));
    }

}
