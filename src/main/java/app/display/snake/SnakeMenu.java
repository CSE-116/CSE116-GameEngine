package app.display.snake;

import app.display.common.FontManager;
import app.display.common.JFXManager;
import app.display.common.ui.PauseMenu;
import app.gameengine.statistics.GameStat;
import app.games.snake.SnakeGame;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class SnakeMenu extends PauseMenu {

    protected String TEXT_FIELD_FONT_FILE = "digital-7/digital-7.ttf";
    protected String TOOLTIP_STYLE = "-fx-background-color: black; " +
            "-fx-text-fill: white; " +
            "-fx-font-family: 'digital-7'; " +
            "-fx-font-size: " + FONT_SIZE_SMALL + "px; " +
            "-fx-padding: 4 8 4 8;";

    private SnakeGame game;

    public SnakeMenu(SnakeGame game) {
        super(game);
        this.game = game;
    }

    @Override
    protected void init() {
        super.init();
        if (!JFXManager.isInitialized()) {
            return;
        }

        Button customButton = new Button("Custom Level");
        customButton.setOnAction(e -> {
            this.pane.getChildren().removeIf(a -> a instanceof VBox);
            this.pane.getChildren().add(getCustomMenu());
        });
        customButton.setTextFill(BUTTON_TEXT_COLOR);
        customButton.setFont(FontManager.getFont(BUTTON_TEXT_FONT_FILE, FONT_SIZE_MEDIUM));
        customButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        this.mainMenu.getChildren().add(2, customButton);
    }

    private VBox getCustomMenu() {
        Text title = new Text("CUSTOM LEVEL");
        title.setFill(TITLE_COLOR);
        title.setFont(FontManager.getFont(TITLE_FONT_FILE, FONT_SIZE_LARGE));

        TextField sizeField = new TextField();
        sizeField.setPromptText("Level Size");
        sizeField.setFont(FontManager.getFont(TEXT_FIELD_FONT_FILE, FONT_SIZE_SMALL));
        sizeField.setMaxWidth(BUTTON_WIDTH);
        TextField startLengthField = new TextField();
        startLengthField.setPromptText("Starting Length");
        startLengthField.setFont(FontManager.getFont(TEXT_FIELD_FONT_FILE, FONT_SIZE_SMALL));
        startLengthField.setMaxWidth(BUTTON_WIDTH);
        TextField lengthIncreaseField = new TextField();
        lengthIncreaseField.setPromptText("Length Increase");
        lengthIncreaseField.setFont(FontManager.getFont(TEXT_FIELD_FONT_FILE, FONT_SIZE_SMALL));
        lengthIncreaseField.setMaxWidth(BUTTON_WIDTH);
        TextField numFoodField = new TextField();
        numFoodField.setPromptText("Food Amount");
        numFoodField.setFont(FontManager.getFont(TEXT_FIELD_FONT_FILE, FONT_SIZE_SMALL));
        numFoodField.setMaxWidth(BUTTON_WIDTH);
        TextField speedField = new TextField();
        speedField.setPromptText("Speed (tiles/s)");
        speedField.setFont(FontManager.getFont(TEXT_FIELD_FONT_FILE, FONT_SIZE_SMALL));
        speedField.setMaxWidth(BUTTON_WIDTH);

        Tooltip sizeTip = new Tooltip("Size, default 16");
        sizeTip.setStyle(TOOLTIP_STYLE);
        Tooltip.install(sizeField, sizeTip);
        Tooltip startTip = new Tooltip("Starting Length, default 3");
        startTip.setStyle(TOOLTIP_STYLE);
        Tooltip.install(startLengthField, startTip);
        Tooltip lengthTip = new Tooltip("Length Increase, default 3");
        lengthTip.setStyle(TOOLTIP_STYLE);
        Tooltip.install(lengthIncreaseField, lengthTip);
        Tooltip foodTip = new Tooltip("Food amount, default 3");
        foodTip.setStyle(TOOLTIP_STYLE);
        Tooltip.install(numFoodField, foodTip);
        Tooltip speedTip = new Tooltip("Speed (tiles/s), default 10");
        speedTip.setStyle(TOOLTIP_STYLE);
        Tooltip.install(speedField, speedTip);

        Button back = new Button("Back");
        back.setOnAction(e -> {
            this.pane.getChildren().removeIf(a -> a instanceof VBox);
            this.pane.getChildren().add(this.mainMenu);
        });
        back.setTextFill(BUTTON_TEXT_COLOR);
        back.setFont(FontManager.getFont(BUTTON_TEXT_FONT_FILE, FONT_SIZE_MEDIUM));
        back.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        Button start = new Button("Start");
        start.setOnAction(e -> {
            try {
                int size = Integer.parseInt(sizeField.getText());
                int startLength = Integer.parseInt(startLengthField.getText());
                int lengthIncrease = Integer.parseInt(lengthIncreaseField.getText());
                int numFood = Integer.parseInt(numFoodField.getText());
                double speed = Double.parseDouble(speedField.getText());
                if (Double.isInfinite(speed)) {
                    throw new NumberFormatException();
                }
                this.game.setGameParameters(size, speed, startLength, lengthIncrease, numFood);
                this.game.advanceLevel();
                this.game.unpause();
            } catch (NumberFormatException ex) {
                System.err.println("Invalid input: all fields must be integers, except speed, which may be a decimal");
            }
        });
        start.setTextFill(BUTTON_TEXT_COLOR);
        start.setFont(FontManager.getFont(BUTTON_TEXT_FONT_FILE, FONT_SIZE_MEDIUM));
        start.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);

        VBox menu = new VBox(BUTTON_SPACING, title, start, sizeField, startLengthField, lengthIncreaseField,
                numFoodField, speedField, back);
        menu.setAlignment(Pos.CENTER);

        return menu;
    }

    @Override
    protected Text getScoreboardHeader() {
        return new Text(String.format("%5s %6s", "TIME", "SCORE"));
    }

    @Override
    protected Text getScoreboardEntry(GameStat stat) {
        String line = String.format("%5s %6.0f", formatDuration(stat.getPlaytime()), stat.getScore());
        return new Text(line);
    }

    @Override
    protected Text getEmptyScoreboardEntry() {
        return new Text(String.format("%5s %6s", "--:--", "---"));
    }

}
