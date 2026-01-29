package app.games.pacman;

import app.Configuration;
import app.Settings;
import app.display.common.ui.PauseMenu;
import app.display.pacman.PacmanUI;
import app.gameengine.Game;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.statistics.Scoreboard;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * A game of Pacman.
 * <p>
 * Mostly handles UI and overall logic like game resets.
 * 
 * @see PacmanLevel
 * @see Pacman
 * @see Ghost
 * @see Game
 */
public class PacmanGame extends Game {

    private Group fgUI = new Group();

    public PacmanGame() {
        super();
        this.setPlayer(new Pacman(0, 0));
        this.setIconPath("pacman.png");
        // uncomment when Scoreboard is completed
        // this.scoreboard = new Scoreboard(this.getName(), new ScoreComparator());
    }

    @Override
    public String getName() {
        return "Pacman";
    }

    @Override
    public Pacman getPlayer() {
        return (Pacman) super.getPlayer();
    }

    @Override
    public PacmanLevel getCurrentLevel() {
        return (PacmanLevel) super.getCurrentLevel();
    }

    @Override
    public void init() {
        super.init();
        this.UI = new PacmanUI(this);
        this.loadLevel(CreatePacmanLevel.createLevel(this));
    }

    @Override
    public void resetGame() {
        super.resetGame();
        this.loadLevel(CreatePacmanLevel.createLevel(this));
    }

    @Override
    public void resetCurrentLevel() {
        super.resetCurrentLevel();
        this.loadLevel(CreatePacmanLevel.createLevel(this));
    }

    @Override
    public void pause() {
        Settings.setPaused(true);
        if (this.fgUI != null) {
            this.fgUI.getChildren().clear();
            this.fgUI.getChildren().add(new PauseMenu(this).getRenderable());
        }
    }

    @Override
    public void unpause() {
        Settings.setPaused(false);
        if (this.fgUI != null) {
            this.fgUI.getChildren().clear();
        }
    }

    @Override
    public Parent createRootNode(Group bgGroup, Group fgGround) {
        return new VBox(this.getUICollection().getRenderableUI(), new StackPane(bgGroup, fgGround, this.fgUI));
    }

    @Override
    public double getWindowHeight() {
        return this.getCurrentLevel().getViewHeight() * Configuration.SCALE_FACTOR
                + this.getUICollection().getRenderableUI().getLayoutBounds().getHeight();
    }

    @Override
    public Vector2D screenSpaceToWorldSpace(Vector2D v) {
        v.setY(v.getY() - this.getUICollection().getRenderableUI().getLayoutBounds().getHeight());
        return Vector2D.div(v, Configuration.SCALE_FACTOR);
    }

}
