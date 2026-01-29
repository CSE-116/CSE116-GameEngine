package app.display.minesweeper;

import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Builder class for creating {@link BeveledBorderPane} instances with custom
 * properties.
 * <p>
 * Allows configuration of bevel thickness, background and bevel colors, and
 * content. Note that content must be set.
 * 
 * @see BeveledBorderPane
 */
public class BeveledBorderPaneBuilder {

    private Node content;
    private double bevelThickness = MinesweeperStyle.uiBorderSize();
    private Color backgroundColor = MinesweeperStyle.uiBgColor();
    private Color topLeftColor = MinesweeperStyle.uiDarkColor();
    private Color bottomRightColor = MinesweeperStyle.uiLightColor();

    /**
     * Constructs a builder for a {@link BeveledBorderPane} with the given content
     * 
     * @param content the content of the pane
     */
    public BeveledBorderPaneBuilder(Node content) {
        this.content = content;
    }

    /**
     * Sets the thickness of the beveled edge, in pixels.
     * 
     * @param thickness the bevel thickness
     * @return this builder instance for method chaining
     */
    public BeveledBorderPaneBuilder bevelThickness(double thickness) {
        this.bevelThickness = thickness;
        return this;
    }

    /**
     * Sets the background color of the pane.
     * 
     * @param color the background color
     * @return this builder instance for method chaining
     */
    public BeveledBorderPaneBuilder backgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }

    /**
     * Sets the color used for the top and left bevel edges.
     *
     * @param color the edge color
     * @return this builder instance for method chaining
     */
    public BeveledBorderPaneBuilder topLeftColor(Color color) {
        this.topLeftColor = color;
        return this;
    }

    /**
     * Sets the color used for the bottom and right bevel edges.
     *
     * @param color the edge color
     * @return this builder instance for method chaining
     */
    public BeveledBorderPaneBuilder bottomRightColor(Color color) {
        this.bottomRightColor = color;
        return this;
    }

    /**
     * Creates a new {@link BeveledBorderPane} using the currently configured
     * values.
     *
     * @return a new {@code BeveledBorderPane} instance
     */
    public BeveledBorderPane build() {
        return new BeveledBorderPane(content, bevelThickness, backgroundColor, topLeftColor, bottomRightColor);
    }
}
