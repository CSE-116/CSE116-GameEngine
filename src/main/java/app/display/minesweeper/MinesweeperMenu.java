package app.display.minesweeper;

import app.Configuration;
import app.display.common.JFXManager;
import app.display.common.PlaceholderNode;
import app.display.common.ui.UIElement;
import app.gameengine.Level;
import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.statistics.GameStat;
import app.gameengine.statistics.Scoreboard;
import app.games.minesweeper.MinesweeperGame;
import app.games.minesweeper.MinesweeperLevel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Pause menu for the Minesweeper game.
 * <p>
 * Intended to be displayed over the main body of the game when it is paused,
 * this menu has buttons for starting a new game at set difficulties, creating a
 * custom game, or viewing the scoreboard.
 * 
 * @see UIElement
 * @see MinesweeperGame
 * @see MinesweeperLevel
 * @see Scoreboard
 */
public class MinesweeperMenu extends UIElement {

    private Node root;
    private StackPane pane;
    private MinesweeperGame game;
    private Rectangle background;
    private VBox diffSelect;
    private VBox customSelect;

    /**
     * Constructs a new menu for a game of minesweeper. This menu displays options
     * for starting new games and viewing the scoreboard.
     * 
     * @param game the game of Minesweeper
     */
    public MinesweeperMenu(MinesweeperGame game) {
        if (!JFXManager.isInitialized()) {
            this.root = new PlaceholderNode();
            return;
        }
        this.game = game;
        double width = 0;
        double height = 0;
        if (game.getCurrentLevel() != null) {
            width = Configuration.SCALE_FACTOR * game.getCurrentLevel().getViewWidth();
            height = Configuration.SCALE_FACTOR * game.getCurrentLevel().getViewHeight();
        }
        this.background = new Rectangle(width, height);
        this.background.setFill(MinesweeperStyle.menuBgColor());

        Text title = new Text("START NEW GAME");
        title.setFill(MinesweeperStyle.titleColor());
        title.setFont(MinesweeperStyle.largeFont());

        BeveledButton beginner = new BeveledButton("Beginner", () -> game.changeLevel("beginner"));
        BeveledButton intermediate = new BeveledButton("Intermediate", () -> game.changeLevel("intermediate"));
        BeveledButton expert = new BeveledButton("Expert", () -> game.changeLevel("expert"));
        BeveledButton custom = new BeveledButton("Custom", () -> {
            this.pane.getChildren().removeIf(a -> a instanceof VBox);
            this.pane.getChildren().add(customSelect);
        });
        BeveledButton scoreButton = new BeveledButton("Scoreboard", () -> {
            this.game.getScoreboard().loadStats();
            this.pane.getChildren().removeIf(a -> a instanceof VBox);
            this.pane.getChildren().add(getScoreboardMenu());
        });

        this.customSelect = getCustomMenu();

        this.diffSelect = new VBox(MinesweeperStyle.buttonSpacing(), title, beginner, intermediate, expert,
                custom, scoreButton);
        this.diffSelect.setMaxWidth(MinesweeperStyle.buttonWidth());
        this.diffSelect.setAlignment(Pos.CENTER);

        Tooltip beginnerTip = new Tooltip("9 x 9, 10 bombs");
        beginnerTip.setStyle(MinesweeperStyle.tooltipStyle());
        Tooltip.install(beginner, beginnerTip);

        Tooltip intermediateTip = new Tooltip("16 x 16, 40 bombs");
        intermediateTip.setStyle(MinesweeperStyle.tooltipStyle());
        Tooltip.install(intermediate, intermediateTip);

        Tooltip expertTip = new Tooltip("30 x 16, 99 bombs");
        expertTip.setStyle(MinesweeperStyle.tooltipStyle());
        Tooltip.install(expert, expertTip);

        this.pane = new StackPane(background, this.diffSelect);
        this.root = this.pane;
        StackPane.setAlignment(title, Pos.TOP_CENTER);
    }

    private String formatDuration(double duration) {
        int seconds = (int) duration;
        int minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private BeveledBorderPane getScoreboardEntry(GameStat stat) {
        Text name, time;
        if (stat == null) {
            name = new Text("---");
            name.setFill(MinesweeperStyle.sbEmptyColor());
            time = new Text("--:--");
            time.setFill(MinesweeperStyle.sbEmptyColor());
        } else {
            name = new Text(stat.getEntryName());
            name.setFill(MinesweeperStyle.sbTextColor());
            time = new Text(formatDuration(stat.getPlaytime()));
            time.setFill(MinesweeperStyle.sbTextColor());
        }
        StackPane.setAlignment(name, Pos.CENTER_LEFT);
        name.setFont(MinesweeperStyle.scoreboardFont());
        StackPane.setAlignment(time, Pos.CENTER_RIGHT);
        time.setFont(MinesweeperStyle.scoreboardFont());

        StackPane row = new StackPane(name, time);
        row.setPadding(new Insets(0, MinesweeperStyle.sbPadding(), 0,
                MinesweeperStyle.sbPadding()));

        BeveledBorderPane entryPane = new BeveledBorderPaneBuilder(row)
                .backgroundColor(MinesweeperStyle.sbBgColor())
                .topLeftColor(MinesweeperStyle.sbDarkColor())
                .bottomRightColor(MinesweeperStyle.sbLightColor())
                .bevelThickness(MinesweeperStyle.sbBorderSize())
                .build();
        entryPane.setMaxWidth(MinesweeperStyle.sbWidth());
        entryPane.setPrefWidth(MinesweeperStyle.sbWidth());
        entryPane.setMaxHeight(MinesweeperStyle.sbHeight());
        entryPane.setPrefHeight(MinesweeperStyle.sbHeight());
        return entryPane;
    }

    private VBox getScoreboardMenu() {
        Text text = new Text("SCOREBOARD");
        text.setFill(MinesweeperStyle.titleColor());
        text.setFont(MinesweeperStyle.largeFont());

        VBox menu = new VBox(MinesweeperStyle.buttonSpacing(), text);
        menu.setAlignment(Pos.CENTER);

        boolean scoreboardExists = true;
        if (game.getScoreboard() == null) {
            scoreboardExists = false;
        } else {
            if (this.game.getScoreboard().getScoreTree() == null) {
                scoreboardExists = false;
            }
        }
        if (!scoreboardExists) {
            Text noneText = new Text("No scoreboard available");
            noneText.setFill(MinesweeperStyle.sbNoneColor());
            noneText.setFont(MinesweeperStyle.mediumFont());
            menu.getChildren().add(noneText);
        } else {
            double availableHeight = this.background.getHeight() - MinesweeperStyle.fontSizeLarge()
                    - MinesweeperStyle.buttonHeight() - MinesweeperStyle.buttonSpacing() * 3;
            double entryHeight = MinesweeperStyle.sbHeight() + MinesweeperStyle.buttonSpacing();
            int maxEntries = (int) (availableHeight / entryHeight);

            int count = 0;
            LinkedListNode<GameStat> node = this.game.getScoreboard().getScoreList();
            for (; count < maxEntries && node != null; count++, node = node.getNext()) {
                GameStat stat = node.getValue();
                if (stat.getEntryName().toLowerCase().contains("custom") || Double.isInfinite(stat.getScore())) {
                    count--;
                    continue;
                }
                menu.getChildren().add(getScoreboardEntry(stat));
            }
            for (; count < maxEntries; count++) {
                menu.getChildren().add(getScoreboardEntry(null));
            }
        }

        BeveledButton back = new BeveledButton("Back", () -> {
            this.pane.getChildren().removeIf(a -> a instanceof VBox);
            this.pane.getChildren().add(diffSelect);
        });
        menu.getChildren().add(back);
        return menu;
    }

    private TextField getStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setStyle(MinesweeperStyle.textFieldStyle());
        field.setPromptText(prompt);
        field.setFont(MinesweeperStyle.smallFont());
        field.setMaxWidth(MinesweeperStyle.buttonWidth());
        return field;
    }

    private VBox getCustomMenu() {
        Text text = new Text("CUSTOM LEVEL");
        text.setFill(MinesweeperStyle.titleColor());
        text.setFont(MinesweeperStyle.largeFont());
        TextField widthField = getStyledTextField("Width");
        TextField heightField = getStyledTextField("Height");
        TextField bombsField = getStyledTextField("Bombs");

        BeveledButton back = new BeveledButton("Back", () -> {
            this.pane.getChildren().removeIf(a -> a instanceof VBox);
            this.pane.getChildren().add(diffSelect);
        });

        BeveledButton start = new BeveledButton("Start", () -> {
            try {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int bombs = Integer.parseInt(bombsField.getText());
                if (width < 9 || height < 9 || bombs < 1) {
                    throw new NumberFormatException();
                } else if (bombs > width * height - 1) {
                    System.err.println("* Too many bombs *");
                }
                this.game.loadLevel(new MinesweeperLevel(game, width, height, bombs));
                this.game.unpause();
            } catch (NumberFormatException ex) {
                System.err.println("Invalid input: width & height must be >= 9, and bombs must be >= 1");
            }
        });

        VBox menu = new VBox(MinesweeperStyle.buttonSpacing(), text, start, widthField, heightField, bombsField,
                back);
        menu.setAlignment(Pos.CENTER);
        menu.setMaxWidth(MinesweeperStyle.buttonWidth());

        return menu;
    }

    @Override
    public Node getRenderable() {
        return this.root;
    }

    @Override
    public void update(double dt, Level level) {
        if (this.background != null) {
            this.background.setWidth(Configuration.SCALE_FACTOR * level.getViewWidth());
            this.background.setHeight(Configuration.SCALE_FACTOR * level.getViewHeight());
        }
    }

}
