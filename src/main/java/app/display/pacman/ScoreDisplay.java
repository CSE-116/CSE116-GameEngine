package app.display.pacman;

import app.Configuration;
import app.display.common.FontManager;
import app.display.common.JFXManager;
import app.display.common.PlaceholderNode;
import app.display.common.ui.UIElement;
import app.gameengine.Level;
import app.games.pacman.PacmanGame;
import app.games.pacman.PacmanLevel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Text display for Pacman that shows the current score.
 * <p>
 * Simple text element that uses a pixely font to display the current score in a
 * game of Pacman.
 * 
 * @see PacmanLevel
 * @see PacmanGame
 * @see UIElement
 */
public class ScoreDisplay extends UIElement {

    private Node root;
    private Label label;
    private PacmanGame game;

    /**
     * Creates a display for the score in a game of Pacman.
     * 
     * @param game the game of Pacman
     */
    public ScoreDisplay(PacmanGame game) {
        this.game = game;
        if (!JFXManager.isInitialized()) {
            this.root = new PlaceholderNode();
            return;
        }
        this.label = new Label("SCORE: 000");
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
        double score = this.game.getCurrentLevel().getScore();
        this.label.setText(String.format("SCORE: %03.0f", score));
    }

}
