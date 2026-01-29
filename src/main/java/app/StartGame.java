package app;

import java.util.ArrayList;

import app.display.common.AssetManager;
import app.display.common.Background;
import app.display.common.BlankTile;
import app.display.common.SpriteGraphics;
import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.model.gameobjects.GameObject;
import app.games.GameFactory;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Main entry point for the game engine.
 * <p>
 * Run the main method within this class to start the game engine. Which game is
 * played depends on the value of {@link Configuration#GAME} (currently
 * {@value Configuration#GAME}) and construction of {@code Game} instances
 * within the {@link GameFactory}.
 * <p>
 * All rendering logic is handled within this class, including window sizing and
 * object/effect/backround rendering.
 * 
 * @see Configuration
 * @see GameFactory
 * @see Game
 * @see Level
 */
public class StartGame extends Application {

    private static boolean VERBOSE = true;

    // Game
    private Game game;
    private Group foregroundGroup;

    // Background
    private Group backgroundGroup;

    // Main components
    private double decorationWidth;
    private double decorationHeight;
    private Parent root;
    private Scene scene;
    private Stage stage;

    // Performance
    private static long start;
    private static long lastUpdate;
    private static int frames = 0;

    @Override
    public void init() {
        this.game = GameFactory.getGame(Configuration.GAME);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.foregroundGroup = new Group();
        this.foregroundGroup.setManaged(false);
        this.backgroundGroup = new Group();
        this.backgroundGroup.setManaged(false);

        this.root = this.game.createRootNode(backgroundGroup, foregroundGroup);
        this.scene = new Scene(this.root);

        stage.setTitle(this.game.getName());
        stage.getIcons().add(AssetManager.getIconImage(this.game.getIconPath()));
        stage.setScene(scene);
        stage.show();

        new AnimationTimer() {
            private Level prevLevel = null;

            @Override
            public void handle(long now) {
                Level currentLevel = game.getCurrentLevel();
                if (currentLevel == null) {
                    System.err.println("*** No level loaded (current level is null). Exiting ***");
                    Platform.exit();
                    return;
                }
                if (lastUpdate == 0) {
                    start = now;
                    lastUpdate = now;
                }
                long elapsed = now - lastUpdate;
                lastUpdate = now;
                frames++;
                double dt = elapsed / 1_000_000_000.0;
                if (prevLevel != currentLevel) {
                    prevLevel = currentLevel;
                    switchLevel();
                }
                if (currentLevel.isLoaded()) {
                    double maxDelay = 0.1;
                    game.update(Math.min(dt, maxDelay));
                } else {
                    currentLevel.setLoaded();
                }
                renderGraphics();

                double newWidth = stage.getWidth() - scene.getWidth();
                double newHeight = stage.getHeight() - scene.getHeight();
                if ((newWidth != decorationWidth || newHeight != decorationHeight) && newWidth > 0 && newHeight > 0) {
                    decorationWidth = stage.getWidth() - scene.getWidth();
                    decorationHeight = stage.getHeight() - scene.getHeight();
                    resizeStage();
                }

                if (dt > 0.01) {
                    if (VERBOSE) {
                        System.out.printf("Getting slow: %2.0f ms/frame (%-3.0f fps)\n", dt * 1000, 1 / dt);
                    }
                }
            }
        }.start();

    }

    @Override
    public void stop() throws Exception {
        double time = (lastUpdate - start) / 1_000_000_000.0 / frames;
        System.out.println("Average time per frame: " + time * 1000 + " ms");
        System.out.println("Average fps: " + 1 / time);
        if (this.game.getScoreboard() != null) {
            this.game.getScoreboard().saveStats();
        }
    }

    private void switchLevel() {
        resizeStage();

        scene.setOnKeyPressed(game.getCurrentLevel().getKeyboardControls());
        scene.setOnKeyReleased(game.getCurrentLevel().getKeyboardControls());
        scene.setOnMousePressed(game.getCurrentLevel().getMouseControls());
        scene.setOnMouseReleased(game.getCurrentLevel().getMouseControls());
    }

    private void resizeStage() {
        double width = game.getWindowWidth() + decorationWidth;
        double height = game.getWindowHeight() + decorationHeight;

        stage.setWidth(width);
        stage.setHeight(height);
    }

    private void renderGraphics() {
        Rectangle window = getWindow();
        Rectangle view = getView();

        double scaleFactor = Configuration.SCALE_FACTOR;
        double hiddenWidth = window.getWidth() - view.getWidth();
        double hiddenHeight = window.getHeight() - view.getHeight();
        // Keep player centered if view is smaller than window
        this.foregroundGroup.setTranslateX(Math.clamp(-view.getX() * scaleFactor, -hiddenWidth * scaleFactor, 0));
        this.foregroundGroup.setTranslateY(Math.clamp(-view.getY() * scaleFactor, -hiddenHeight * scaleFactor, 0));
        this.foregroundGroup.getChildren().clear();
        // Render background
        renderBackground();
        ArrayList<GameObject> allObjects = new ArrayList<>();
        // Render terrain
        allObjects.addAll(this.game.getCurrentLevel().getStaticObjects());
        // Render game objects
        allObjects.addAll(this.game.getCurrentLevel().getDynamicObjects());
        // Render the player
        allObjects.removeIf(GameObject::isPlayer);
        allObjects.add(this.game.getPlayer());

        for (GameObject object : allObjects) {
            // Cull objects that aren't visible
            if (!isInBounds(object, view)) {
                continue;
            }
            // Render object sprite
            SpriteGraphics tile = new SpriteGraphics(object);
            tile.setX(object.getLocation().getX() * scaleFactor + object.getSpriteOffsetX() * Configuration.ZOOM);
            tile.setY(object.getLocation().getY() * scaleFactor + object.getSpriteOffsetY() * Configuration.ZOOM);
            tile.setRotate(object.getRotation() + tile.getRotate());
            this.foregroundGroup.getChildren().add(tile);
        }
        // Render effects
        this.game.getCurrentLevel().getActiveEffects()
                .forEach((k, v) -> this.foregroundGroup.getChildren().add(k.getFrame(v)));
    }

    private void renderBackground() {
        Rectangle window = getWindow();
        Rectangle view = getView();

        double scaleFactor = Configuration.SCALE_FACTOR;
        double hiddenWidth = window.getWidth() - view.getWidth();
        double hiddenHeight = window.getHeight() - view.getHeight();

        this.backgroundGroup.getChildren().clear();
        Background background = this.game.getCurrentLevel().getBackground();
        if (background.usesBackgroundImage()) {
            scaleRectangle(window, scaleFactor);
            scaleRectangle(view, scaleFactor);
            hiddenWidth = window.getWidth() - view.getWidth();
            hiddenHeight = window.getHeight() - view.getHeight();

            ArrayList<String> backgroundFileNames = background.getBackgroundImageFileNames();
            ArrayList<Double> parallaxRatios = background.getParallaxRatios();

            for (int i = 0; i < backgroundFileNames.size(); i++) {

                String fileName = backgroundFileNames.get(i);
                double parallaxRatio = parallaxRatios.get(i);

                double width = Math.clamp(window.getWidth() - hiddenWidth * (1 - parallaxRatio), view.getWidth(),
                        window.getWidth());
                double height = Math.clamp(window.getHeight() - hiddenHeight * (1 - parallaxRatio), view.getHeight(),
                        window.getHeight());

                Image image = AssetManager.getBackgroundImage(fileName, width, height, true);

                double offsetX = view.getX() * parallaxRatio;
                double offsetY = view.getY() * parallaxRatio;
                double startX = Math.floor(offsetX / image.getWidth()) * image.getWidth() - offsetX;
                double startY = Math.floor(offsetY / image.getHeight()) * image.getHeight() - offsetY;

                for (double posX = startX; posX < view.getWidth(); posX += image.getWidth()) {
                    for (double posY = startY; posY < view.getHeight(); posY += image.getHeight()) {
                        ImageView backgroundImageView = new ImageView(image);
                        backgroundImageView.setTranslateX(posX);
                        backgroundImageView.setTranslateY(posY);
                        this.backgroundGroup.getChildren().add(backgroundImageView);
                    }
                }
            }
        } else {
            double playerX = this.game.getPlayer().getLocation().getX()
                    + this.game.getPlayer().getSpriteDimensions().getX() / 2;
            double playerY = this.game.getPlayer().getLocation().getY()
                    + this.game.getPlayer().getSpriteDimensions().getY() / 2;
            int startX = Math.clamp((int) view.getX(), 0, (int) hiddenWidth);
            int startY = Math.clamp((int) view.getY(), 0, (int) hiddenHeight);
            int endX = Math.clamp((int) (Math.ceil(playerX) + 1 + view.getWidth() / 2), (int) view.getWidth(),
                    (int) window.getWidth());
            int endY = Math.clamp((int) (Math.ceil(playerY) + 1 + view.getHeight() / 2), (int) view.getHeight(),
                    (int) window.getHeight());

            for (int i = startX; i < endX; i++) {
                for (int j = startY; j < endY; j++) {
                    SpriteGraphics tile = new SpriteGraphics(new BlankTile(i, j,
                            background.getBackgroundImageFileNames().get(0),
                            background.getGroundTileSpriteLocation()));
                    tile.setX(i * scaleFactor);
                    tile.setY(j * scaleFactor);
                    this.foregroundGroup.getChildren().add(tile);
                }
            }
        }
    }

    private Rectangle getWindow() {
        int width = this.game.getCurrentLevel().getWidth();
        int height = this.game.getCurrentLevel().getHeight();

        return new Rectangle(width, height);
    }

    private Rectangle getView() {
        int width = this.game.getCurrentLevel().getWidth();
        int height = this.game.getCurrentLevel().getHeight();
        int viewWidth = Math.min(this.game.getCurrentLevel().getViewWidth(), width);
        int viewHeight = Math.min(this.game.getCurrentLevel().getViewHeight(), height);

        double playerX = this.game.getPlayer().getLocation().getX()
                + this.game.getPlayer().getSpriteDimensions().getX() / 2;
        double playerY = this.game.getPlayer().getLocation().getY()
                + this.game.getPlayer().getSpriteDimensions().getY() / 2;

        double viewX = Math.clamp(playerX - viewWidth / 2, 0, width - viewWidth);
        double viewY = Math.clamp(playerY - viewHeight / 2, 0, height - viewHeight);

        return new Rectangle(viewX, viewY, viewWidth, viewHeight);
    }

    private boolean isInBounds(GameObject object, Rectangle view) {
        return object.getSpriteOrigin().getX() <= view.getX() + view.getWidth()
                && object.getSpriteOrigin().getX() + object.getSpriteDimensions().getX() >= view.getX()
                && object.getSpriteOrigin().getY() <= view.getY() + view.getHeight()
                && object.getSpriteOrigin().getY() + object.getSpriteDimensions().getY() >= view.getY();
    }

    private void scaleRectangle(Rectangle rect, double scaleFactor) {
        rect.setX(rect.getX() * scaleFactor);
        rect.setY(rect.getY() * scaleFactor);
        rect.setWidth(rect.getWidth() * scaleFactor);
        rect.setHeight(rect.getHeight() * scaleFactor);
    }

    public static void main(String[] args) {
        launch();
    }

}
