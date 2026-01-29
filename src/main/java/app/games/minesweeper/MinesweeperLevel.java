package app.games.minesweeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import app.display.common.Background;
import app.display.common.controller.KeyboardControls;
import app.display.minesweeper.MinesweeperStyle;
import app.gameengine.Level;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.PhysicsEngine;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.statistics.GameStat;
import app.gameengine.utils.GameUtils;
import app.gameengine.utils.Randomizer;
import app.games.minesweeper.CoverTile.TileState;

/**
 * A level in a game of Minesweeper.
 * <p>
 * Almost all game logic in handled within this class, including revealing tiles
 * on a click and handling win and lose states. {@link MinesweeperGame}
 * primarily handles UI, and {@link MinesweeperControls} controls the visuals of
 * specific tiles and the face display.
 * 
 * @see MinesweeperGame
 * @see MinesweeperControls
 */
public class MinesweeperLevel extends Level {

    public enum GameState {
        WIN, LOSE, PLAYING, CLICK
    }

    private boolean initialized;
    private HashMap<Vector2D, CoverTile> hiddenTiles = new HashMap<>();
    private HashMap<Vector2D, Bomb> bombs = new HashMap<>();
    private HashMap<Vector2D, Integer> counts = new HashMap<>();
    private HashSet<Vector2D> flags = new HashSet<>();
    private int numBombs;
    private final int maxFlags;
    private GameState gameState = GameState.PLAYING;
    private double scoreMultiplier = 1;

    /**
     * Creates a level with the given preset difficulty, from {@code "trivial"},
     * {@code "beginner"}, {@code "intermediate"}, and {@code "expert"}. The
     * difficulty determines the size and number of bombs in the level.
     * 
     * @param game       the game of minesweeper
     * @param difficulty the difficulty preset
     */
    public MinesweeperLevel(MinesweeperGame game, String difficulty) {
        super(game, new PhysicsEngine(), 0, 0, difficulty);
        this.keyboardControls = new KeyboardControls(game);
        this.mouseControls = new MinesweeperControls(game);
        this.background = new Background(MinesweeperStyle.TILE_FILE, 0, 0);

        switch (difficulty.toLowerCase()) {
            case "expert":
                this.width = 30;
                this.height = 16;
                this.numBombs = 99;
                this.scoreMultiplier = 10_000;
                break;
            case "intermediate":
                this.width = 16;
                this.height = 16;
                this.numBombs = 40;
                this.scoreMultiplier = 1_000;
                break;
            case "trivial":
                this.width = 9;
                this.height = 9;
                this.numBombs = 3;
                this.scoreMultiplier = 1;
                break;
            case "beginner":
            default:
                this.width = 9;
                this.height = 9;
                this.numBombs = 10;
                this.scoreMultiplier = 100;
        }
        this.maxFlags = numBombs;
        this.init();
    }

    /**
     * Creates a level with a custom difficulty, with any size and number of bombs.
     * If the number of bombs is greater than {@code width * height - 1}, it will be
     * capped at that amount.
     * 
     * @param game   the game of minesweeper
     * @param width  the width of the level, in tiles
     * @param height the height of the level, in tiles
     * @param bombs  the number of bombs in the level
     */
    public MinesweeperLevel(MinesweeperGame game, int width, int height, int bombs) {
        super(game, new PhysicsEngine(), width, height, "Custom");
        this.keyboardControls = new KeyboardControls(game);
        this.mouseControls = new MinesweeperControls(game);
        this.background = new Background(MinesweeperStyle.TILE_FILE, 0, 0);
        this.numBombs = Math.min(bombs, width * height - 1);
        this.maxFlags = numBombs;
        this.init();
        this.setName(String.format("custom:%dx%d-%d", width, height, bombs));
    }

    /**
     * Returns a list of vectors that are immediately horizontally, vertically, or
     * diagonally adjacent to the input vector {@code v}. This should only return
     * vectors which are within the bounds of the level.
     * 
     * @param v the central vector
     * @return a list of adjacent vectors
     */
    public ArrayList<Vector2D> getAdjacentVectors(Vector2D v) {
        return new ArrayList<>();
    }

    /**
     * Returns a map of locations to their corresponding tiles. The locations are
     * guaranteed to be whole numbers. This only includes tiles that are currently
     * present in the level, i.e. tiles that have been removed are not in this map.
     * 
     * @return a map of hidden tiles
     */
    public HashMap<Vector2D, CoverTile> getHiddenTiles() {
        return this.hiddenTiles;
    }

    /**
     * Returns a map of locations to their corresponding numbers of nearby bombs.
     * The locations are guaranteed to be whole numbers. This only includes tiles
     * that have at least one nearby bomb which is immediately horizontally,
     * vertically, or diagonally adjacent. Note that this includes numbers that are
     * currently hidden underneath a cover tile.
     * 
     * @return a map of number tiles
     */
    public HashMap<Vector2D, Integer> getNumbers() {
        return this.counts;
    }

    /**
     * Returns a set of locations that are currently flagged. The locations are
     * guaranteed to be whole numbers. Note that tiles with a question mark are not
     * included in this set.
     * 
     * @return a set of flagged locations
     */
    public HashSet<Vector2D> getFlags() {
        return this.flags;
    }

    /**
     * Returns the number of flags that are still available to be placed. If every
     * flag that has been placed yet correctly marks a bomb, this is the same as the
     * number of remaining unflagged bombs.
     * 
     * @return the number of available flags
     */
    public int getAvailableFlags() {
        return this.maxFlags - this.flags.size();
    }

    /**
     * Returns the current state of the game, namely whether it is won, lost, or
     * still in progress.
     * 
     * @return the game state
     */
    public GameState getState() {
        return this.gameState;
    }

    /**
     * Sets the state of the game, namely when it is won or lost.
     * 
     * @param state the state to set
     */
    public void setState(GameState state) {
        this.gameState = state;
        if (state == GameState.WIN) {
            this.game.getScoreboard().addScore(new GameStat(this.getName(), this.playtime, this.getScore()));
        }
    }

    private void init() {
        for (int i = 0; i < this.getWidth(); i++) {
            for (int j = 0; j < this.getHeight(); j++) {
                this.hiddenTiles.put(new Vector2D(i, j), new CoverTile(i, j));
            }
        }
        this.getStaticObjects().addAll(this.hiddenTiles.values());
    }

    private void firstClick(Vector2D clickLoc) {
        Vector2D bounds = new Vector2D(this.width, this.height);
        this.bombs.put(clickLoc, null);
        for (int i = 0; i < this.numBombs; i++) {
            Vector2D bombLoc = Randomizer.randomIntVector2D(bounds, new ArrayList<>(this.bombs.keySet()));
            this.bombs.put(bombLoc, new Bomb(bombLoc.getX(), bombLoc.getY()));
            for (Vector2D loc : getAdjacentVectors(bombLoc)) {
                this.counts.put(loc, this.counts.getOrDefault(loc, 0) + 1);
            }
        }
        this.bombs.remove(clickLoc);
        this.counts.entrySet().removeIf(e -> this.bombs.keySet().contains(e.getKey()));
        this.bombs.values().forEach(this.getStaticObjects()::addFirst);
        this.counts.forEach(
                (loc, count) -> this.getStaticObjects().addFirst(new NumberTile(loc.getX(), loc.getY(), count)));
    }

    @Override
    public double getScore() {
        return this.scoreMultiplier / this.playtime;
    }

    @Override
    public void reset() {
        this.playtime = 0;
        this.initialized = false;
        this.getStaticObjects().forEach(StaticGameObject::destroy);
        this.getStaticObjects().clear();
        this.setState(GameState.PLAYING);
        this.bombs.clear();
        this.hiddenTiles.clear();
        this.counts.clear();
        this.flags.clear();
        this.init();
    }

    private void uncoverTiles(Vector2D location) {
        if (this.flags.contains(location)) {
            this.flags.remove(location);
        }
        this.hiddenTiles.remove(location).destroy();
        if (!counts.containsKey(location)) {
            for (Vector2D neighbor : getAdjacentVectors(location)) {
                if (this.hiddenTiles.containsKey(neighbor)) {
                    this.uncoverTiles(neighbor);
                }
            }
        }
    }

    @Override
    public void handleLeftClick(Vector2D location) {
        if (!this.initialized) {
            this.firstClick(location);
            this.initialized = true;
        }
        if (this.gameState == GameState.WIN || this.gameState == GameState.LOSE) {
            return;
        }
        if (!this.hiddenTiles.containsKey(location) || this.flags.contains(location)) {
            return;
        }
        // Lose
        if (bombs.containsKey(location)) {
            bombs.get(location).detonate();
            this.setState(GameState.LOSE);
            for (Vector2D bombLoc : this.bombs.keySet()) {
                if (this.hiddenTiles.containsKey(bombLoc) && !this.flags.contains(bombLoc)) {
                    this.hiddenTiles.remove(bombLoc).destroy();
                }
            }
            for (Vector2D tileLoc : this.hiddenTiles.keySet()) {
                if (this.flags.contains(tileLoc) && !this.bombs.containsKey(tileLoc)) {
                    this.hiddenTiles.get(tileLoc).setState(TileState.FLAGGEDWRONG);
                }
            }
            return;
        }
        this.uncoverTiles(location);
        // Win
        if (this.hiddenTiles.size() <= this.bombs.size()) {
            this.setState(GameState.WIN);
            for (CoverTile tile : this.hiddenTiles.values()) {
                if (!tile.isFlagged()) {
                    this.flags.add(tile.getLocation());
                    tile.setState(TileState.FLAGGED);
                }
            }
        }
    }

    @Override
    public void handleRightClick(Vector2D location) {
        if (!this.initialized) {
            this.firstClick(location);
            this.initialized = true;
        }
        if (this.gameState == GameState.WIN || this.gameState == GameState.LOSE) {
            return;
        }
        if (this.hiddenTiles.containsKey(location)) {
            if (this.flags.contains(location)) {
                this.hiddenTiles.get(location).setState(TileState.QUESTION);
                this.flags.remove(location);
            } else if (this.hiddenTiles.get(location).getTileState() == TileState.QUESTION) {
                this.hiddenTiles.get(location).setState(TileState.COVER);
            } else if (this.getAvailableFlags() > 0) {
                this.hiddenTiles.get(location).setState(TileState.FLAGGED);
                this.flags.add(location);
            } else {
                this.hiddenTiles.get(location).setState(TileState.QUESTION);
            }
        }
    }

    @Override
    public void update(double dt) {
        if (this.initialized) {
            super.update(dt);
        } else {
            this.getPlayer().update(dt, this);
        }
    }

}
