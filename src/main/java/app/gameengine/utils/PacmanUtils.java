package app.gameengine.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import app.gameengine.model.physics.Vector2D;
import app.games.pacman.Ghost;
import app.games.pacman.PacmanGame;
import app.games.pacman.PacmanLevel;

/**
 * Static class containing utilities for Pacman.
 * 
 * @see PacmanGame
 * @see PacmanLevel
 */
public class PacmanUtils {

    private static Set<Vector2D> directions = new HashSet<>(Set.of(
            new Vector2D(1, 0), new Vector2D(-1, 0), new Vector2D(0, 1), new Vector2D(0, -1)));

    /**
     * Prevent instantiation, as this class is intended to be static.
     */
    private PacmanUtils() {
    }

    /**
     * Returns whether a ghost is near the center of their current tile. This is
     * useful because ghosts can only act when they are near the center of their
     * tile.
     * 
     * @param ghost the ghost
     * @return whether the ghost is centered
     */
    public static boolean isNearTile(Ghost ghost, double dt) {
        if (Vector2D.euclideanDistance(ghost.getLocation(), Vector2D.round(ghost.getLocation())) > dt
                * ghost.getMovementSpeed()) {
            return false;
        }
        return true;
    }

    /**
     * Returns whether a ghost is allowed to take action given its current state.
     * Actions include chasing, scattering, fleeing, and returning home. A ghost may
     * act if two things are true:
     * <p>
     * 1) They are either near the center of their current tile, or they are not
     * moving, and
     * <p>
     * 2) The last whole tile they were on is either null or they are closer to
     * another tile than that tile.
     * 
     * @param ghost    the ghost
     * @param dt       the time elapsed since the last update, in seconds
     * @param lastTile the last whole tile the ghost was on
     * @return whether the ghost is allowed to act
     */
    public static boolean canAct(Ghost ghost, double dt, Vector2D lastTile) {
        return (PacmanUtils.isNearTile(ghost, dt) || ghost.getVelocity().equals(new Vector2D(0, 0)))
                && (lastTile == null || !Vector2D.round(ghost.getLocation()).equals(lastTile));
    }

    /**
     * Returns the vector within the level that this ghost is attempting to reach
     * when in chase mode, depending on their color.
     * 
     * @param level the level being played
     * @param ghost the ghost
     * @return the vector being targeted
     */
    public static Vector2D getChaseTarget(PacmanLevel level, Ghost ghost) {
        Vector2D target;
        if (Vector2D.euclideanDistance(ghost.getLocation(), level.getGhostHouse().getLocation()) < 3) {
            return Vector2D.add(level.getGhostHouse().getLocation(), new Vector2D(0, -4));
        }

        switch (ghost.getColor()) {
            default:
            case "Red":
                return level.getPlayer().getLocation();
            case "Pink":
                // Target 4 tiles ahead of player
                return Vector2D.add(level.getPlayer().getLocation(),
                        Vector2D.mul(level.getPlayer().getOrientation(), 4));
            case "Cyan":
                // Target the point twice the direction from Blinky to 2 tiles ahead of pacman
                target = Vector2D.add(level.getPlayer().getLocation(),
                        Vector2D.mul(level.getPlayer().getOrientation(), 2));
                return Vector2D.mul(Vector2D.sub(target, level.getGhosts().get("Red").getLocation()), 2);
            case "Orange":
                // if > 8 tiles, use blinky target, else use scatter target
                double dist = Vector2D.euclideanDistance(ghost.getLocation(), level.getPlayer().getLocation());
                return dist > 8 ? level.getPlayer().getLocation() : new Vector2D(1, level.getHeight() - 2);
        }
    }

    /**
     * Returns the vector within the level that this ghost is attempting to reach
     * when dead.
     * 
     * @param level the level being played
     * @return the vector being targeted
     */
    public static Vector2D getHomeTarget(PacmanLevel level) {
        return level.getGhostHouse().getLocation();
    }

    /**
     * Returns the vector within the level that this ghost is attempting to reach
     * when in scatter mode, depending on their color.
     * 
     * @param level the level being played
     * @param ghost the ghost
     * @return the vector being targeted
     */
    public static Vector2D getScatterTarget(PacmanLevel level, Ghost ghost) {
        if (Vector2D.euclideanDistance(ghost.getLocation(), level.getGhostHouse().getLocation()) < 3) {
            return Vector2D.add(level.getGhostHouse().getLocation(), new Vector2D(0, -4));
        }
        switch (ghost.getColor()) {
            default:
            case "Red":
                return new Vector2D(level.getWidth() - 2, 1);
            case "Pink":
                return new Vector2D(1, 1);
            case "Cyan":
                return new Vector2D(level.getWidth() - 2, level.getHeight() - 2);
            case "Orange":
                return new Vector2D(1, level.getHeight() - 2);
        }
    }

    /**
     * Returns a random direction from a list of directions. It is assumed that all
     * of these are valid directions, and no checks are performed. If the list is
     * empty, this method returns null. This method is useful for choosing a target
     * when a ghost is in frightened mode.
     * 
     * @param validDirs the list to choose from
     * @return a random element from that list
     */
    public static Vector2D getRandomDirection(ArrayList<Vector2D> validDirs) {
        if (validDirs.isEmpty()) {
            return null;
        }
        return Randomizer.randomSelect(validDirs);
    }

    /**
     * Returns the best direction from a list of directions, defined as the
     * direction that positions the ghost closest to their target. It is assumed
     * that all of these are valid directions, and no checks are performed. If the
     * list is empty, this method returns null. This method is useful for choosing a
     * direction when a ghost is in chase mode.
     * 
     * @param validDirs the list to choose from
     * @param location  the location of the ghost
     * @param target    the target of the ghost
     * @return the best direction to move in
     */
    public static Vector2D getBestDirection(ArrayList<Vector2D> validDirs, Vector2D location, Vector2D target) {
        if (validDirs.isEmpty()) {
            return null;
        }
        Vector2D bestDir = null;
        double bestDist = Double.MAX_VALUE;
        for (Vector2D dir : validDirs) {
            Vector2D neighbor = Vector2D.add(Vector2D.round(location), dir);
            double dist = Vector2D.euclideanDistance(neighbor, target);
            if (dist < bestDist) {
                bestDist = dist;
                bestDir = dir;
            }
        }
        return bestDir;
    }

    /**
     * Returns a list of valid directions for a ghost to move. It is assumed that a
     * ghost is centered on their tile. A direction is valid if it is in the bounds
     * of the level, not in a wall, and not backwards.
     * 
     * @param level the level being played
     * @param ghost the ghost
     * @return a list of valid directions
     */
    public static ArrayList<Vector2D> getValidDirs(PacmanLevel level, Ghost ghost) {
        ArrayList<Vector2D> validDirs = new ArrayList<>();
        Vector2D currentTile = Vector2D.round(ghost.getLocation());
        for (Vector2D dir : directions) {
            Vector2D neighbor = Vector2D.add(currentTile, dir);
            // If not backwards, in bounds, not in wall, and not solid
            if (!dir.equals(Vector2D.negate(ghost.getOrientation()))
                    && GameUtils.isInBounds(level, neighbor)
                    && (!level.getWalls().containsKey(neighbor)
                            || (level.getWalls().containsKey(neighbor) && !level.getWalls().get(neighbor).isSolid()))) {
                validDirs.add(dir);
            }
        }
        return validDirs;
    }

}
