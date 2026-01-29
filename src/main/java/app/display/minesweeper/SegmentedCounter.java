package app.display.minesweeper;

import app.display.common.JFXManager;
import app.display.common.PlaceholderNode;
import app.display.common.ui.UIElement;
import app.gameengine.Level;
import app.games.minesweeper.MinesweeperGame;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

/**
 * UIElement that displays the remaining number of flags in a game of
 * Minesweeper.
 * <p>
 * Using a 7-segment font, displays the number of remaining flags in the game.
 * Uses a {@link BeveledBorderPane} to appear inset.
 * 
 * @see UIElement
 * @see BeveledBorderPane
 * @see MinesweeperGame
 */
public class SegmentedCounter extends UIElement {

    private Label label;
    private MinesweeperGame game;
    private Node root;
    private StackPane pane;

    /**
     * Constructs a new flag counter for a game of Minesweeper.
     * 
     * @param game the game of Minesweeper
     */
    public SegmentedCounter(MinesweeperGame game) {
        this.game = game;
        if (!JFXManager.isInitialized()) {
            this.root = new PlaceholderNode();
            return;
        }
        this.label = new Label("000");

        this.label.setFont(MinesweeperStyle.counterFont());
        this.label.setTextAlignment(TextAlignment.CENTER);
        this.label.setTextFill(MinesweeperStyle.counterTextColor());

        Label bgLabel = new Label("888");
        bgLabel.setFont(MinesweeperStyle.counterFont());
        bgLabel.setTextAlignment(TextAlignment.CENTER);
        bgLabel.setTextFill(MinesweeperStyle.counterSecondaryColor());

        Rectangle background = new Rectangle();
        background.setFill(MinesweeperStyle.counterBackgroundColor());
        background.widthProperty().bind(bgLabel.widthProperty().add(MinesweeperStyle.counterPadding()));
        background.heightProperty().bind(bgLabel.heightProperty());

        this.pane = new BeveledBorderPaneBuilder(new StackPane(background, bgLabel, this.label))
                .bevelThickness(MinesweeperStyle.counterBorderSize())
                .topLeftColor(MinesweeperStyle.counterDarkColor())
                .bottomRightColor(MinesweeperStyle.counterLightColor())
                .build();
        this.root = this.pane;
        this.pane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    }

    @Override
    public Node getRenderable() {
        return this.root;
    }

    @Override
    public void update(double dt, Level level) {
        if (this.label != null) {
            int flags = this.game.getCurrentLevel().getAvailableFlags();
            this.label.setText(String.format("%03d", flags));
        }
    }

}
