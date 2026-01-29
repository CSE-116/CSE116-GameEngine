package app.games.pacman;

import java.util.ArrayList;
import java.util.HashMap;

import app.Configuration;
import app.display.common.Background;
import app.display.common.FontManager;
import app.display.common.effects.SimpleTextEffect;
import app.display.common.sound.AudioManager;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.statistics.GameStat;
import app.gameengine.utils.Timer;
import app.games.topdownobjects.TopDownLevel;
import javafx.scene.paint.Color;

/**
 * A level within a game of Pacman.
 * <p>
 * Controls main game logic, including global ghost states and pellet and score
 * tracking.
 * 
 * @see PacmanGame
 * @see Ghost
 * @see TopDownLevel
 * @see Level
 */
public class PacmanLevel extends TopDownLevel {

    public enum GameState {
        CHASE, SCATTER, FRIGHTENED
    }

    private HashMap<Vector2D, StaticGameObject> walls = new HashMap<>();

    private Timer chaseTimer = new Timer(15);
    private Timer scatterTimer = new Timer(5);
    private Timer frightenedTimer = new Timer(10);
    private GameState gameState = GameState.CHASE;

    private GhostHouse ghostHouse;
    private int pelletsLeft;
    private int pelletsEaten;
    private int scoreMultiplier = 1;

    private boolean firstFrame = true;

    public PacmanLevel(PacmanGame game, int width, int height, String name) {
        super(game, width, height, name);
        this.keyboardControls = new PacmanControls(game);
        this.background = new Background("pacman/pacmanWalls.png", 3, 1);
    }

    public HashMap<Vector2D, StaticGameObject> getWalls() {
        return this.walls;
    }

    public void eatPellet(boolean powerPellet) {
        this.pelletsEaten++;
        this.pelletsLeft--;
        this.score += powerPellet ? 50 : 10;
        if (this.pelletsEaten == 20) {
            this.ghostHouse.getGhosts().get("Pink").setState("Chase");
        } else if (this.pelletsEaten == 40) {
            this.ghostHouse.getGhosts().get("Cyan").setState("Chase");
        } else if (this.pelletsEaten == 70) {
            // Spawn fruit
        } else if (this.pelletsEaten == 100) {
            this.ghostHouse.getGhosts().get("Orange").setState("Chase");
        } else if (this.pelletsEaten == 170) {
            // Spawn fruit
        }
        if (powerPellet) {
            this.scoreMultiplier = 1;
            this.frightenedTimer.reset();
            this.gameState = GameState.FRIGHTENED;
            this.ghostHouse.getGhosts().forEach((k, v) -> {
                if (v.getState().equals("Chase") || v.getState().equals("Scatter")
                        || v.getState().equals("Frightened")) {
                    v.setState("Frightened");
                }
            });
        }
        if (this.pelletsLeft == 0) {
            this.game.getScoreboard().addScore(new GameStat(this.getName(), playtime, score));
            this.game.resetGame();
        }
    }

    public void eatGhost(Ghost ghost) {
        double scoreIncrease = 200 * scoreMultiplier;
        this.getActiveEffects()
                .put(new SimpleTextEffect(String.format("%.0f", scoreIncrease), 2,
                        FontManager.getFont("Minecraft.ttf", Configuration.TEXT_SCALE * 15), Color.CYAN),
                        ghost.getLocation().copy());

        this.score += scoreIncrease;
        this.scoreMultiplier *= 2;
    }

    public int getPelletsLeft() {
        return pelletsLeft;
    }

    public void setPelletsLeft(int pelletsLeft) {
        this.pelletsLeft = pelletsLeft;
    }

    public GhostHouse getGhostHouse() {
        return this.ghostHouse;
    }

    public void setGhostHouse(GhostHouse ghosthouse) {
        this.ghostHouse = ghosthouse;
    }

    public HashMap<String, Ghost> getGhosts() {
        return this.ghostHouse.getGhosts();
    }

    @Override
    public Pacman getPlayer() {
        return (Pacman) super.getPlayer();
    }

    @Override
    public void load() {
        super.load();
        this.dynamicObjects.addAll(this.ghostHouse.getGhosts().values());
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        if (firstFrame) {
            firstFrame = false;
            this.game.pause();
        }
        if (this.gameState == GameState.FRIGHTENED && this.frightenedTimer.tick(dt) > 0) {
            // If frightened time is over, chase
            this.gameState = GameState.CHASE;
            this.ghostHouse.getGhosts().forEach((k, v) -> {
                if (v.getState().equals("Frightened")) {
                    v.setState("Chase");
                }
            });
        } else if (this.gameState == GameState.FRIGHTENED
                && (this.frightenedTimer.getElapsedTime() / this.frightenedTimer.getCooldown()) > 0.6) {
            // If frightened time is almost over, enter end animation
            this.ghostHouse.getGhosts().forEach((k, v) -> {
                if (v.getState().equals("Frightened")) {
                    v.setAnimationState("frightened_end");
                }
            });
        } else if (this.gameState == GameState.CHASE && this.chaseTimer.tick(dt) > 0) {
            // If chase time is over, scatter
            this.gameState = GameState.SCATTER;
            this.ghostHouse.getGhosts().forEach((k, v) -> {
                if (v.getState().equals("Chase")) {
                    v.setState("Scatter");
                }
            });
        } else if (this.gameState == GameState.SCATTER && this.scatterTimer.tick(dt) > 0) {
            // If scatter time is over, chase
            this.gameState = GameState.CHASE;
            this.ghostHouse.getGhosts().forEach((k, v) -> {
                if (v.getState().equals("Scatter")) {
                    v.setState("Chase");
                }
            });
        }

        this.keyboardControls.processInput(dt);
        this.teleport();
    }

    private void teleport() {
        ArrayList<Vector2D> locs = new ArrayList<>();
        locs.add(this.getPlayer().getLocation());
        this.ghostHouse.getGhosts().forEach((k, v) -> locs.add(v.getLocation()));
        for (Vector2D loc : locs) {
            if (loc.getX() < -1) {
                loc.setX(this.width);
            } else if (loc.getX() > this.width) {
                loc.setX(-1);
            } else if (loc.getY() < -1) {
                loc.setY(this.height);
            } else if (loc.getY() > this.height) {
                loc.setY(-1);
            }
        }
    }

    @Override
    public void onStart() {
        AudioManager.playMusic("pacman/ghost.wav", 0.2);
    }

}
