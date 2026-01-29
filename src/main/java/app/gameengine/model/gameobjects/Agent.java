package app.gameengine.model.gameobjects;

import java.util.ArrayList;

import app.Settings;
import app.gameengine.Level;
import app.gameengine.model.datastructures.LinkedListNode;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.Timer;
import app.games.commonobjects.PathTile;

/**
 * A {@link DynamicGameObject} capable of thinking.
 * <p>
 * This class adds onto its parent's abilities by defining methods that allow it
 * to choose and move along paths.
 * 
 * @see DynamicGameObject
 * @see LinkedListNode
 * @see Vector2D
 */
public abstract class Agent extends DynamicGameObject {

    protected double movementSpeed = 1.0;
    private Vector2D lastOrientation = this.getOrientation().copy();
    private LinkedListNode<Vector2D> path;

    private Timer timer = new Timer(1);
    private ArrayList<PathTile> tiles = new ArrayList<>();

    /**
     * Constructs an agent with the given location and max HP
     * 
     * @param x     the x location of the object
     * @param y     the y location of the object
     * @param maxHP the maximum HP of the object
     */
    public Agent(double x, double y, int maxHP) {
        super(x, y, maxHP);
    }

    public LinkedListNode<Vector2D> getPath() {
        return path;
    }

    public void setPath(LinkedListNode<Vector2D> path) {
        this.path = path;
    }

    /**
     * Returns the movement speed of the {@code Agent}. The movement speed is the
     * number of tiles the {@code Agent} can move each second.
     * 
     * @return the movement speed
     */
    public double getMovementSpeed() {
        return this.movementSpeed;
    }

    /**
     * Sets the movement speed of the {@code Agent} to the input value. The movement
     * speed is the number of tiles the {@code Agent} can move each second.
     * 
     * @param speed the movement speed
     */
    public void setMovementSpeed(double speed) {
        this.movementSpeed = speed;
    }

    /**
     * Returns a {@code String} representing the current direction this agent is
     * facing, based on the orientation. This direction will either be
     * {@code "left"}, {@code "right"}, {@code "up"}, or {@code "down"}, whichever
     * cardinal direction the orientation is closest to. This is mostly used for
     * animations.
     * 
     * @return the direction the agent is facing
     */
    protected String getDirection() {
        Vector2D orientation = this.getOrientation().copy();
        if (Math.abs(orientation.getX()) > Math.abs(orientation.getY())) {
            return orientation.getX() > 0 ? "right" : "left";
        }
        return orientation.getY() > 0 ? "down" : "up";
    }

    /**
     * Advances the {@code Agent} along its path if it has one. If it is near a
     * tile, it is snapped to that exact position, to avoid getting stuck on
     * corners. Otherwise, only the velocity and orientation are set to follow the
     * path.
     * 
     * @param dt the amount of time since the last update
     */
    public void followPath(double dt) {

    }

    @Override
    public void update(double dt, Level level) {
        super.update(dt, level);
        if (Settings.showPaths() && timer.tick(dt) > 0) {
            this.tiles.forEach(PathTile::destroy);
            tiles.clear();
            for (LinkedListNode<Vector2D> node = this.path; node != null; node = node.getNext()) {
                PathTile tile = new PathTile(node.getValue().getX(), node.getValue().getY());
                level.getStaticObjects().add(tile);
                tiles.add(tile);
            }
        } else if (!Settings.showPaths()) {
            this.tiles.forEach(PathTile::destroy);
        }
        if (!this.getOrientation().equals(this.lastOrientation)) {
            this.lastOrientation = this.getOrientation().copy();
            String anim = "walk_" + this.getDirection();
            this.setAnimationState(anim);
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.lastOrientation = this.getOrientation().copy();
        this.path = null;
        this.tiles.forEach(PathTile::destroy);
        this.tiles.clear();
    }

    @Override
    public void destroy() {
        super.destroy();
        this.tiles.forEach(PathTile::destroy);
    }

}
