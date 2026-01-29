package app.display.minesweeper;

import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Button class wrapped in a {@link BeveledBorderPane}.
 * <p>
 * Intended for use in the Minesweeper UI, and not configurable, this serves as
 * a simple beveled button.
 * 
 * @see BeveledBorderPane
 * @see MinesweeperMenu
 */
public class BeveledButton extends StackPane {

    private final Text label;
    private final BeveledBorderPane bevel;
    private final Runnable action;

    /**
     * Constructs a beveled button with the given properties.
     * 
     * @param text     the text to display
     * @param action   the action to occur on click
     * @param width    the width of the button, in pixels
     * @param height   the height of the button, in pixels
     * @param fontSize the font size for the text
     */
    public BeveledButton(String text, Runnable action) {
        this.action = action;

        this.label = new Text(text);
        label.setFont(MinesweeperStyle.mediumFont());
        label.setFill(MinesweeperStyle.buttonTextColor());

        StackPane labelPane = new StackPane(label);
        labelPane.setPrefWidth(MinesweeperStyle.buttonWidth());
        labelPane.setPrefHeight(MinesweeperStyle.buttonHeight());
        labelPane.setAlignment(Pos.CENTER);

        this.bevel = new BeveledBorderPaneBuilder(labelPane)
                .bevelThickness(MinesweeperStyle.buttonBorderSize())
                .backgroundColor(MinesweeperStyle.buttonBgColor())
                .topLeftColor(MinesweeperStyle.buttonLightColor())
                .bottomRightColor(MinesweeperStyle.buttonDarkColor())
                .build();

        this.getChildren().add(bevel);
        this.bevel.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        setMouseHandlers();
    }

    private void setMouseHandlers() {
        this.setOnMouseEntered(e -> {
            bevel.setBackgroundColor(MinesweeperStyle.buttonHoverColor());
            bevel.setBevelColors(MinesweeperStyle.buttonLightColor(), MinesweeperStyle.buttonDarkColor());
        });
        this.setOnMouseExited(e -> {
            bevel.setBackgroundColor(MinesweeperStyle.buttonBgColor());
            bevel.setBevelColors(MinesweeperStyle.buttonLightColor(), MinesweeperStyle.buttonDarkColor());
        });
        this.setOnMousePressed(e -> {
            bevel.setBackgroundColor(MinesweeperStyle.buttonPressedColor());
            bevel.setBevelColors(MinesweeperStyle.buttonDarkColor(), MinesweeperStyle.buttonLightColor());
        });
        this.setOnMouseReleased(e -> {
            bevel.setBackgroundColor(MinesweeperStyle.buttonHoverColor());
            bevel.setBevelColors(MinesweeperStyle.buttonLightColor(), MinesweeperStyle.buttonDarkColor());
            if (action != null) {
                action.run();
            }
        });
    }
}
