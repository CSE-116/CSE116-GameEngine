package app.display.common.ui;

import app.Configuration;
import app.display.common.JFXManager;
import app.display.common.PlaceholderNode;
import app.gameengine.Game;
import app.gameengine.Level;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * A UI element that displays a styled text label on the screen.
 * <p>
 * The label can be customized with font, color, alignment, and offset, and
 * updates its text automatically based on the current level's UI string.
 * 
 * @see UILabelBuilder
 * @see UIElement
 * @see UILabel
 * @see Game
 */
public class UILabel extends UIElement {

    private Node root;
    private Label label;

    /**
     * Constructs a new ui label with the default properties.
     */
    public UILabel() {
        this("default", Font.font(Configuration.DEFAULT_TEXT_SIZE), Configuration.DEFAULT_TEXT_SIZE, Configuration.DEFAULT_TEXT_SIZE, Color.WHITE,
                Color.BLACK, Pos.TOP_RIGHT);
    }

    /**
     * Constructs a new ui label with the given properties
     * 
     * @param text            the text to display
     * @param font            the font to use
     * @param xOffset         the x offset, in pixels, of this health bar within its
     *                        container
     * @param yOffset         the y offset, in pixels, of this health bar within its
     *                        container
     * @param backgroundColor the color of the background
     * @param textColor       the color of the text
     * @param alignment       the alignment of the label within its container
     */
    public UILabel(String text, Font font, double xOffset, double yOffset, Color backgroundColor, Color textColor,
            Pos alignment) {
        if (!JFXManager.isInitialized()) {
            this.root = new PlaceholderNode();
            return;
        }
        this.label = new Label(text);
        this.root = this.label;
        this.label.setBackground(new Background(new BackgroundFill(backgroundColor, CornerRadii.EMPTY, Insets.EMPTY)));
        this.label.setFont(font);
        this.label.setTextFill(textColor);
        StackPane.setAlignment(this.label, alignment);
        Insets offset = this.computeOffset(alignment, xOffset, yOffset);
        StackPane.setMargin(this.label, offset);
    }

    @Override
    public Node getRenderable() {
        return this.root;
    }

    @Override
    public void update(double dt, Level level) {
        if (this.label != null && !this.label.getText().equals(level.getUIString())) {
            this.label.setText(level.getUIString());
        }
    }

}
