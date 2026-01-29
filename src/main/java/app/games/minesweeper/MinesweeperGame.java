package app.games.minesweeper;

import app.Configuration;
import app.Settings;
import app.display.minesweeper.BeveledBorderPaneBuilder;
import app.display.minesweeper.MinesweeperMenu;
import app.display.minesweeper.MinesweeperStyle;
import app.display.minesweeper.MinesweeperUI;
import app.gameengine.Game;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.statistics.Scoreboard;
import app.games.minesweeper.MinesweeperLevel.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Game for playing Minesweeper.
 * <p>
 * The majority of this class is devoted to setting up the visuals, hence its
 * separation from the rest of the classes for Minesweeper. Actual game logic is
 * primarily handled within the {@link MinesweeperLevel} class.
 * 
 * @see MinesweeperLevel
 * @see MinesweeperMenu
 * @see Game
 */
public class MinesweeperGame extends Game {

    private Group menuGroup = new Group();
    private Group gameGroup;
    private MinesweeperMenu menu;
    private Rectangle background = new Rectangle(0, 0, Color.TRANSPARENT);

    /**
     * Constructs a new game of Minesweeper.
     */
    public MinesweeperGame() {
        super();
        this.setPlayer(new NotPlayer());
        this.setIconPath("minesweeper.png");
        // uncomment when Scoreboard is completed
        // this.scoreboard = new Scoreboard(this.getName(), new ScoreComparator());
    }

    public GameState getGameState() {
        return this.getCurrentLevel().getState();
    }

    @Override
    public String getName() {
        return "Minesweeper";
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        this.menu.update(dt, getCurrentLevel());
        this.background.setWidth(Configuration.SCALE_FACTOR * this.getCurrentLevel().getViewWidth());
        this.background.setHeight(Configuration.SCALE_FACTOR * this.getCurrentLevel().getViewHeight());
    }

    @Override
    public void resetCurrentLevel() {
        this.getCurrentLevel().reset();
    }

    @Override
    public void resetGame() {
        this.changeLevel("trivial");
        this.pause();
    }

    @Override
    public MinesweeperLevel getCurrentLevel() {
        return (MinesweeperLevel) super.getCurrentLevel();
    }

    @Override
    public void changeLevel(String name) {
        this.loadLevel(new MinesweeperLevel(this, name));
        this.unpause();
    }

    @Override
    public void init() {
        super.init();
        this.UI = new MinesweeperUI(this);
        this.menu = new MinesweeperMenu(this);
        this.changeLevel("trivial");
        this.pause();
    }

    @Override
    public void pause() {
        Settings.setPaused(true);
        this.menuGroup.getChildren().clear();
        this.menuGroup.getChildren().add(this.menu.getRenderable());
    }

    @Override
    public void unpause() {
        Settings.setPaused(false);
        this.menuGroup.getChildren().clear();
        this.menuGroup.getChildren().add(this.background);
    }

    @Override
    public Parent createRootNode(Group bgGroup, Group fgGroup) {
        this.gameGroup = fgGroup;
        StackPane main = new StackPane(bgGroup, fgGroup, menuGroup);
        StackPane mainPane = new BeveledBorderPaneBuilder(main)
                .backgroundColor(MinesweeperStyle.uiBgColor())
                .bottomRightColor(MinesweeperStyle.uiLightColor())
                .topLeftColor(MinesweeperStyle.uiDarkColor())
                .bevelThickness(MinesweeperStyle.uiBorderSize())
                .build();
        mainPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        Region separator = new Region();
        separator.setPrefHeight(MinesweeperStyle.uiBorderSize() * 2);
        separator.setBorder(new Border(new BorderStroke(MinesweeperStyle.uiBorderColor(),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(MinesweeperStyle.uiBorderSize()), Insets.EMPTY)));

        VBox content = new VBox(this.getUICollection().getRenderableUI(), separator, mainPane);
        content.setAlignment(Pos.TOP_CENTER);
        content.setBorder(new Border(new BorderStroke(MinesweeperStyle.uiBorderColor(), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(MinesweeperStyle.uiBorderSize() * 2), Insets.EMPTY)));
        StackPane outerPane = new BeveledBorderPaneBuilder(content)
                .backgroundColor(MinesweeperStyle.uiBorderColor())
                .bottomRightColor(MinesweeperStyle.uiDarkColor())
                .topLeftColor(MinesweeperStyle.uiLightColor())
                .bevelThickness(MinesweeperStyle.uiBorderSize()).build();

        return outerPane;
    }

    @Override
    public double getWindowHeight() {
        return super.getWindowHeight() + this.getUICollection().getRenderableUI().getLayoutBounds().getHeight()
                + MinesweeperStyle.uiBorderSize() * 10;
    }

    @Override
    public double getWindowWidth() {
        return super.getWindowWidth() + MinesweeperStyle.uiBorderSize() * 8;
    }

    @Override
    public Vector2D screenSpaceToWorldSpace(Vector2D rootSceneLocation) {
        Point2D gameLocation = this.gameGroup.localToScene(0, 0);

        rootSceneLocation.setX(rootSceneLocation.getX() - gameLocation.getX());
        rootSceneLocation.setY(rootSceneLocation.getY() - gameLocation.getY());

        return Vector2D.div(rootSceneLocation, Configuration.SCALE_FACTOR);
    }

}
