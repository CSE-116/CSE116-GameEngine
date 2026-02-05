package app.games.snake;

import app.Configuration;
import app.display.common.FontManager;
import app.display.common.ui.UILabelBuilder;
import app.display.snake.SnakeMenu;
import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.statistics.GameStat;
import app.gameengine.statistics.Scoreboard;
import app.gameengine.utils.Timer;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;

/**
 * A classic game of Snake.
 * <p>
 * Mostly manages the UI, menus, and resetting or creating custom levels.
 * 
 * @see SnakeLevel
 * @see Game
 * @see Snake
 */
public class SnakeGame extends Game {

    private int size = 16;
    private double speed = 10; // tiles/second
    private int startingLength = 3;
    private int lengthIncrease = 3; // length increase for each food
    private int numFood = 3;

    public SnakeGame() {
        this.setPlayer(new Snake(0, 0));
        // uncomment when Scoreboard is completed
        // this.scoreboard = new Scoreboard(this.getName(), new ScoreComparator());
        this.pauseMenu = new SnakeMenu(this);
    }

    private Level getNewLevel() {
        Level level = new SnakeLevel(this, size, new Timer(1 / speed), "level " + size / 10, this.lengthIncrease,
                this.startingLength, this.numFood);
        level.setPlayerStartLocation(size / 2, size / 2);
        return level;
    }

    public void setGameParameters(int size, double speed, int startingLength, int lengthIncrease, int numFood) {
        this.size = size;
        this.speed = speed;
        this.startingLength = startingLength;
        this.lengthIncrease = lengthIncrease;
        this.numFood = numFood;
    }

    @Override
    public String getName() {
        return "Snake";
    }

    @Override
    public void init() {
        super.init();
        this.getUICollection().clearElements();
        this.getUICollection().addElement("score",
                new UILabelBuilder().font(FontManager.getFont("Minecraft.ttf", Configuration.TEXT_SCALE * 20))
                        .alignment(Pos.TOP_CENTER).backgroundColor(Color.WHITE.deriveColor(0, 1, 1, 0.5))
                        .text("Score: 0").build());
        this.loadLevel(getNewLevel());
        this.pause();
    }

    @Override
    public void advanceLevel() {
        this.getPlayer().reset();
        this.loadLevel(getNewLevel());
        this.pause();
    }

    @Override
    public void resetCurrentLevel() {
        this.getPlayer().reset();
        if (this.scoreboard != null) {
            this.scoreboard.addScore(new GameStat("Snake", getCurrentLevel().getPlaytime(), getCurrentLevel().getScore()));
        }
        this.loadLevel(getNewLevel());
        this.pause();
    }

    @Override
    public void resetGame() {
        this.resetCurrentLevel();
    }

}
