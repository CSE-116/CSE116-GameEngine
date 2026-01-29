package app.games.roguelikeobjects;

import static app.games.roguelikeobjects.RoguelikeGame.DOWN_VECTOR;
import static app.games.roguelikeobjects.RoguelikeGame.LEFT_VECTOR;
import static app.games.roguelikeobjects.RoguelikeGame.RIGHT_VECTOR;
import static app.games.roguelikeobjects.RoguelikeGame.UP_VECTOR;

import java.util.ArrayList;

import app.gameengine.Game;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.Vector2D;
import app.games.topdownobjects.TopDownLevel;

public class RoguelikeLevel extends TopDownLevel {

    private static final String ENEMY_MARKER_ID = "Enemy";
    private static final String LOOT_MARKER_ID = "Loot";
    private static final String DOOR_MARKER_ID = "Door";

    private ArrayList<LevelDoor> levelDoors = new ArrayList<>();
    private Vector2D levelLocation;
    private LevelDoor upDoor;
    private LevelDoor rightDoor;
    private LevelDoor downDoor;
    private LevelDoor leftDoor;

    /**
     * Constructs a Roguelikelevel associated with the given game and with the given
     * width, height, and name.
     *
     * @param game   the game this level is a part of
     * @param width  the width of the level, in tiles
     * @param height the height of the level, in tiles
     * @param name   the name of the level
     */
    public RoguelikeLevel(Game game, int width, int height, String name) {
        super(game, width, height, name);
    }

    public void initialize(Vector2D levelLocation) {
        this.levelLocation = levelLocation;
        this.setName(levelLocation.toString());
        addEnemies();
        addLoot();
        addDoors();
    }

    public ArrayList<LevelDoor> getLevelDoors() {
        return this.levelDoors;
    }

    private void addDoors() {
        ArrayList<Marker> markers = this.extractAndRemoveMarkers(DOOR_MARKER_ID);
        for (Marker marker : markers) {
            LevelDoor door = new LevelDoor(
                    marker.getLocation().getX(),
                    marker.getLocation().getY(),
                    game,
                    this,
                    "");
            door.setNextLevelName(levelLocation);
            Vector2D doorDirection = door.getDoorDirection();
            if (doorDirection.equals(UP_VECTOR)) {
                upDoor = door;
            } else if (doorDirection.equals(RIGHT_VECTOR)) {
                rightDoor = door;
            } else if (doorDirection.equals(DOWN_VECTOR)) {
                downDoor = door;
            } else if (doorDirection.equals(LEFT_VECTOR)) {
                leftDoor = door;
            }
            door.setActive(false);
            this.levelDoors.add(door);
            this.staticObjects.add(door);
        }
    }

    /**
     * Opens the door to the adjacentLevel if it exists.
     * 
     * @param adjacentLevel the adjacentLevel the door will lead to.
     */
    public void openDoor(RoguelikeLevel adjacentLevel) {
        Vector2D difference = Vector2D.sub(adjacentLevel.levelLocation, this.levelLocation);
        LevelDoor doorToOpen = null;
        if (difference.equals(UP_VECTOR)) {
            doorToOpen = upDoor;
        } else if (difference.equals(RIGHT_VECTOR)) {
            doorToOpen = rightDoor;
        } else if (difference.equals(DOWN_VECTOR)) {
            doorToOpen = downDoor;
        } else if (difference.equals(LEFT_VECTOR)) {
            doorToOpen = leftDoor;
        }

        if (doorToOpen != null) {
            doorToOpen.setActive(true);
        } else {
            System.err.println("** LevelDoor could not open to an adjacent level! **");
        }

    }

    private void addEnemies() {
        ArrayList<Marker> markers = this.extractAndRemoveMarkers(ENEMY_MARKER_ID);
        for (Marker marker : markers) {
            DynamicGameObject randomEnemy = EnemyFactory.getRandomEnemy(marker.getLocation());
            this.addDynamicObject(randomEnemy);
        }
    }

    private void addLoot() {
        ArrayList<Marker> markers = this.extractAndRemoveMarkers(LOOT_MARKER_ID);

        for (Marker marker : markers) {
            StaticGameObject randomLoot = CollectibleFactory.getRandomLoot(marker.getLocation(), game);
            this.addStaticObject(randomLoot);
        }
    }

    public Vector2D getLevelLocation() {
        return this.levelLocation;
    }

    public ArrayList<Marker> extractAndRemoveMarkers(String markerID) {
        ArrayList<Marker> markers = new ArrayList<>();
        ArrayList<StaticGameObject> levelStaticGameObjects = this.getStaticObjects();

        for (StaticGameObject staticGameObject : levelStaticGameObjects) {
            if (staticGameObject instanceof Marker marker && marker.getMarkerID().equals(markerID)) {
                markers.add(marker);
            }
        }

        for (Marker marker : markers) {
            levelStaticGameObjects.remove(marker);
        }

        return markers;
    }

    @Override
    public void load() {
        if (game instanceof RoguelikeGame roguelikeGame && roguelikeGame.getPreviousLevel() != null) {
            Vector2D difference = Vector2D.sub(roguelikeGame.getPreviousLevel().levelLocation, this.levelLocation);
            Vector2D originalPos = new Vector2D(0, 0);
            if (difference.equals(UP_VECTOR)) {
                originalPos = upDoor.getLocation();
            } else if (difference.equals(RIGHT_VECTOR)) {
                originalPos = rightDoor.getLocation();
            } else if (difference.equals(DOWN_VECTOR)) {
                originalPos = downDoor.getLocation();
            } else if (difference.equals(LEFT_VECTOR)) {
                originalPos = leftDoor.getLocation();
            }
            playerStartLocation = Vector2D.add(originalPos, Vector2D.negate(difference));
        } else {
            playerStartLocation = new Vector2D((getWidth() - 1) / 2.0, (getHeight() - 1) / 2.0);
        }
        super.load();
    }
}
