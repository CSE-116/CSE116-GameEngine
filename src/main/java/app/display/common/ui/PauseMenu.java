package app.display.common.ui;

import app.Configuration;
import app.display.common.FontManager;
import app.display.common.JFXManager;
import app.display.common.PlaceholderNode;
import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.statistics.GameStat;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * A general purpose pause menu with a scoreboard display.
 * <p>
 * Basic pause screen with a semi-transparent background and the text "GAME
 * PAUSED". Also includes buttons for returning to the game and displaying the
 * scoreboard.
 * <p>
 * Other menus should extend this to modify styling or add more functionality.
 * 
 * @see UIElement
 * @see UICollection
 */
public class PauseMenu extends UIElement {

    // ------------------------------ Constants -------------------------------
    // Spacing
    protected double FONT_SIZE_LARGE = Configuration.TEXT_SCALE * 20;
    protected double FONT_SIZE_MEDIUM = Configuration.TEXT_SCALE * 12;
    protected double FONT_SIZE_SMALL = Configuration.TEXT_SCALE * 8;
    protected double BUTTON_SPACING = Configuration.TEXT_SCALE * 5;
    protected double BUTTON_WIDTH = 120 * Configuration.TEXT_SCALE;
    protected double BUTTON_HEIGHT = 15 * Configuration.TEXT_SCALE;
    protected double SCOREBOARD_HEIGHT = Configuration.TEXT_SCALE * 20;
    protected double SCOREBOARD_PADDING = Configuration.TEXT_SCALE * 3;
    // Object colors
    protected Color BACKGROUND_COLOR = Color.BLACK.deriveColor(0, 1, 1, 0.5);
    // Styles
    protected Color TITLE_COLOR = Color.RED;
    protected String TITLE_FONT_FILE = "Minecraft.ttf";
    protected Color SECONDARY_COLOR = Color.LIGHTGRAY;
    protected String SECONDARY_FONT_FILE = "Minecraft.ttf";
    protected Color SCOREBOARD_HEADER_COLOR = Color.YELLOW;
    protected String SCOREBOARD_HEADER_FONT_FILE = "digital-7/digital-7.ttf";
    protected Color SCOREBOARD_ENTRY_COLOR = Color.LIMEGREEN;
    protected String SCOREBOARD_ENTRY_FONT_FILE = "digital-7/digital-7.ttf";
    protected Color BUTTON_TEXT_COLOR = Color.BLACK;
    protected String BUTTON_TEXT_FONT_FILE = "Minecraft.ttf";

    /**
     * Root node of the menu.
     */
    protected Node root;
    /**
     * Root pane of the menu. This is usually the same as {@link #root}.
     */
    protected StackPane pane;
    /**
     * The {@link Game} this menu belongs to.
     */
    protected Game game;
    /**
     * The shape overlaid on top of the game when paused.
     */
    protected Rectangle background;
    /**
     * The actual buttons, labels, etc. that make up the menu.
     */
    protected VBox mainMenu;

    /**
     * Constructs a new pause menu. This is intended to be the default for most
     * games. Includes a scoreboard display.
     * <p>
     * This should only be used for tuning constants and other trivial behavior.
     * Actual setup should be performed by {@link #init()}.
     * 
     * @param game the game associated with this pause menu
     */
    public PauseMenu(Game game) {
        this.game = game;
    }

    protected void init() {
        if (!JFXManager.isInitialized()) {
            this.root = new PlaceholderNode();
            return;
        }
        double width = 0;
        double height = 0;
        if (game.getCurrentLevel() != null) {
            width = Configuration.SCALE_FACTOR * game.getCurrentLevel().getViewWidth();
            height = Configuration.SCALE_FACTOR * game.getCurrentLevel().getViewHeight();
        }
        this.background = new Rectangle(width, height);
        this.background.setFill(BACKGROUND_COLOR);

        Text title = new Text("GAME PAUSED");
        title.setFill(TITLE_COLOR);
        title.setFont(FontManager.getFont(TITLE_FONT_FILE, FONT_SIZE_LARGE));

        Button resumeButton = new Button("Return to Game");
        resumeButton.setOnAction(e -> game.unpause());
        resumeButton.setTextFill(BUTTON_TEXT_COLOR);
        resumeButton.setFont(FontManager.getFont(BUTTON_TEXT_FONT_FILE, FONT_SIZE_MEDIUM));
        resumeButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Button scoreboardButton = new Button("Scoreboard");
        scoreboardButton.setOnAction(e -> {
            if (this.game.getScoreboard() != null) {
                this.game.getScoreboard().loadStats();
            }
            this.pane.getChildren().removeIf(a -> a instanceof VBox);
            this.pane.getChildren().add(buildScoreboardMenu());
        });
        scoreboardButton.setTextFill(BUTTON_TEXT_COLOR);
        scoreboardButton.setFont(FontManager.getFont(BUTTON_TEXT_FONT_FILE, FONT_SIZE_MEDIUM));
        scoreboardButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Button exitButton = new Button("Exit Game");
        exitButton.setOnAction(e -> Platform.exit());
        exitButton.setTextFill(BUTTON_TEXT_COLOR);
        exitButton.setFont(FontManager.getFont(BUTTON_TEXT_FONT_FILE, FONT_SIZE_MEDIUM));
        exitButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        this.mainMenu = new VBox(BUTTON_SPACING, title, resumeButton, scoreboardButton, exitButton);
        this.mainMenu.setAlignment(Pos.CENTER);

        this.pane = new StackPane(background, mainMenu);
        this.root = this.pane;
        StackPane.setAlignment(background, Pos.TOP_LEFT);
    }

    /**
     * Formats a double as a human-readable number of minutes and seconds. For
     * example, 100 seconds would result in 01:40. Fractional components are
     * ignored.
     * 
     * @param duration number of seconds
     * @return readable duration
     */
    protected String formatDuration(double duration) {
        int seconds = (int) duration;
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Returns the text element that will be displayed at the top of the scoreboard
     * to label the columns.
     * 
     * @return the scoreboard header text element
     */
    protected Text getScoreboardHeader() {
        return new Text(String.format("%-15s %5s %6s", "NAME", "TIME", "SCORE"));
    }

    /**
     * Returns a text element for the given {@link GameStat} to be used in the
     * scoreboard.
     * 
     * @param stat the info to use in the entry
     * @return the text element corresponding to this entry
     */
    protected Text getScoreboardEntry(GameStat stat) {
        String line = String.format("%-15s %5s %6.0f", stat.getEntryName(), formatDuration(stat.getPlaytime()),
                stat.getScore());
        return new Text(line);
    }

    /**
     * Returns a text element representing no entry, usually due to extra space in
     * the scoreboard.
     * 
     * @return a text element for an empty entry
     */
    protected Text getEmptyScoreboardEntry() {
        return new Text(String.format("%-15s %5s %6s", "---", "--:--", "---"));
    }

    /**
     * Creates and returns the menu for displaying the scoreboard. Generally
     * contains a title, the scoreboard itself, and a button to return to the main
     * menu.
     * <p>
     * This method uses {@link #getScoreboardHeader()},
     * {@link #getScoreboardEntry(GameStat)}, and {@link #getEmptyScoreboardEntry()}
     * to format the scoreboard. If you are extending this class and only want to
     * make small changes to the scoreboard, those should be overridden instead of
     * this.
     * 
     * @return the scoreboard element
     */
    protected VBox buildScoreboardMenu() {
        Text sbTitle = new Text("SCOREBOARD");
        sbTitle.setFill(TITLE_COLOR);
        sbTitle.setFont(FontManager.getFont(TITLE_FONT_FILE, FONT_SIZE_LARGE));

        VBox menu = new VBox(BUTTON_SPACING, sbTitle);
        menu.setAlignment(Pos.CENTER);

        if (this.game.getScoreboard() == null) {
            Text none = new Text("No scoreboard available");
            none.setFill(SECONDARY_COLOR);
            none.setFont(FontManager.getFont(SECONDARY_FONT_FILE, FONT_SIZE_LARGE));
            menu.getChildren().add(none);
        } else {
            Text header = getScoreboardHeader();
            header.setFill(SCOREBOARD_HEADER_COLOR);
            header.setFont(FontManager.getFont(SCOREBOARD_HEADER_FONT_FILE, FONT_SIZE_MEDIUM));
            VBox.setMargin(header, new Insets(0, 0, 0, SCOREBOARD_PADDING));
            menu.getChildren().add(header);

            double availableHeight = this.background.getHeight() - FONT_SIZE_LARGE - BUTTON_SPACING - FONT_SIZE_MEDIUM;
            double entryHeight = SCOREBOARD_HEIGHT + BUTTON_SPACING;
            int maxEntries = (int) (availableHeight / entryHeight);

            int count = 0;
            LinkedListNode<GameStat> node = this.game.getScoreboard().getScoreList();
            for (; count < maxEntries && node != null; count++, node = node.getNext()) {
                GameStat stat = node.getValue();
                if (stat.getEntryName().toLowerCase().contains("custom") || Double.isInfinite(stat.getScore())) {
                    count--;
                    continue;
                }
                Text entry = getScoreboardEntry(stat);
                entry.setFill(SCOREBOARD_ENTRY_COLOR);
                entry.setFont(FontManager.getFont(SCOREBOARD_ENTRY_FONT_FILE, FONT_SIZE_MEDIUM));
                VBox.setMargin(entry, new Insets(0, 0, 0, SCOREBOARD_PADDING));
                menu.getChildren().add(entry);
            }
            for (; count < maxEntries; count++) {
                Text empty = getEmptyScoreboardEntry();
                empty.setFill(SCOREBOARD_ENTRY_COLOR);
                empty.setFont(FontManager.getFont(SCOREBOARD_ENTRY_FONT_FILE, FONT_SIZE_MEDIUM));
                VBox.setMargin(empty, new Insets(0, 0, 0, SCOREBOARD_PADDING));
                menu.getChildren().add(empty);
            }
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            this.pane.getChildren().removeIf(a -> a instanceof VBox);
            this.pane.getChildren().add(mainMenu);
        });
        backButton.setTextFill(BUTTON_TEXT_COLOR);
        backButton.setFont(FontManager.getFont(BUTTON_TEXT_FONT_FILE, FONT_SIZE_MEDIUM));
        backButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        menu.getChildren().add(backButton);
        return menu;
    }

    @Override
    public Node getRenderable() {
        if (this.root == null) {
            this.init();
        }
        return this.root;
    }

    @Override
    public void update(double dt, Level level) {
        if (background != null) {
            background.setWidth(Configuration.SCALE_FACTOR * level.getViewWidth());
            background.setHeight(Configuration.SCALE_FACTOR * level.getViewHeight());
        }
    }
}
