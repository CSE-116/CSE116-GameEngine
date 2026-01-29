package app.games.pacman;

import java.util.HashMap;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * Spawn point for {@link Ghost}s in Pacman.
 * 
 * @see PacmanGame
 * @see Ghost
 */
public class GhostHouse extends StaticGameObject {

    private double redOffsetX;
    private double redOffsetY;
    private HashMap<String, Ghost> ghosts = new HashMap<>();
    private PacmanGame game;

    public GhostHouse(double x, double y, PacmanGame game) {
        this(x, y, game, 0, -3);
        this.spriteSheetFilename = "pacman/pacmanWalls.png";
        this.defaultSpriteLocation = new SpriteLocation(4, 1);
    }

    public GhostHouse(double x, double y, PacmanGame game, double redOffsetX, double redOffsetY) {
        super(x, y);
        this.game = game;
        this.redOffsetX = redOffsetX;
        this.redOffsetY = redOffsetY;
        this.createGhosts(x, y);
    }

    private void createGhosts(double x, double y) {
        Ghost ghost = new Ghost(x + redOffsetX, y + redOffsetY, this.game, "Red");
        ghost.setOrientation(-1, 0);
        ghost.setState("Chase");
        this.ghosts.put(ghost.getColor(), ghost);

        ghost = new Ghost(x - 2, y, this.game, "Cyan");
        ghost.setOrientation(0, 1);
        this.ghosts.put(ghost.getColor(), ghost);

        ghost = new Ghost(x, y, this.game, "Pink");
        ghost.setOrientation(0, -1);
        this.ghosts.put(ghost.getColor(), ghost);

        ghost = new Ghost(x + 2, y, this.game, "Orange");
        ghost.setOrientation(0, 1);
        this.ghosts.put(ghost.getColor(), ghost);
    }

    public HashMap<String, Ghost> getGhosts() {
        return this.ghosts;
    }

    @Override
    public void reset() {
        super.reset();
        this.createGhosts(this.getLocation().getX(), this.getLocation().getY());
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        if (otherObject instanceof Ghost ghost && ghost.getState().equals("Dead")) {
            ghost.setState("Chase");
        }
    }

}
