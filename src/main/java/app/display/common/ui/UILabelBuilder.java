package app.display.common.ui;

import app.Configuration;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Builder class for creating {@link UILabel} instances with custom properties.
 * <p>
 * Allows configuration of text, font, colors, alignment, and offset for UI
 * labels.
 * 
 * @see UILabel
 * @see UIElement
 * @see UICollection
 */
public class UILabelBuilder {

    private String text = "default";
    private Font font = Font.font(Configuration.DEFAULT_TEXT_SIZE);
    private double xOffset = Configuration.DEFAULT_TEXT_SIZE;
    private double yOffset = Configuration.DEFAULT_TEXT_SIZE;
    private Color backgroundColor = Color.WHITE;
    private Color textColor = Color.BLACK;
    private Pos alignment = Pos.TOP_RIGHT;

    /**
     * Sets the text of the label.
     * 
     * @param text the text to display
     * @return the same {@code UILabelBuilder} instance for method chaining
     */
    public UILabelBuilder text(String text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the font of the label.
     * 
     * @param font the font to use
     * @return the same {@code UILabelBuilder} instance for method chaining
     */
    public UILabelBuilder font(Font font) {
        this.font = font;
        return this;
    }

    /**
     * Sets the x and y offset of the label within its container.
     * 
     * @param offset the x and y offset, in pixels
     * @return the same {@code UILabelBuilder} instance for method chaining
     */
    public UILabelBuilder offset(double offset) {
        return offset(offset, offset);
    }

    /**
     * Sets the x and y offset of the label within its container.
     * 
     * @param x the x offset, in pixels
     * @param y the y offset, in pixels
     * @return the same {@code UILabelBuilder} instance for method chaining
     */
    public UILabelBuilder offset(double x, double y) {
        this.xOffset = x;
        this.yOffset = y;
        return this;
    }

    /**
     * Sets the background color of the label.
     * 
     * @param color the background color to use
     * @return the same {@code UILabelBuilder} instance for method chaining
     */
    public UILabelBuilder backgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }

    /**
     * Sets the text color of the label.
     * 
     * @param color the color to use for the text
     * @return the same {@code UILabelBuilder} instance for method chaining
     */
    public UILabelBuilder textColor(Color color) {
        this.textColor = color;
        return this;
    }

    /**
     * Sets the alignment of the label within its container. This may be
     * overridden by the {@link UICollection} that posesses this label.
     * 
     * @param alignment the alignment to use
     * @return the same {@code UILabelBuilder} instance for method chaining
     */
    public UILabelBuilder alignment(Pos alignment) {
        this.alignment = alignment;
        return this;
    }

    /**
     * Creates a {@link UILabel} with the properties previously set on this
     * builder.
     * 
     * @return a new ui label object
     */
    public UILabel build() {
        return new UILabel(text, font, xOffset, yOffset, backgroundColor, textColor, alignment);
    }

}
