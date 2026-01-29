package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import app.display.common.Background;
import app.gameengine.Level;
import app.gameengine.LevelParser;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.Vector2D;
import app.games.SampleGame;
import app.games.commonobjects.Goal;
import app.games.commonobjects.InfoNode;
import app.games.commonobjects.Wall;
import app.games.minesweeper.MinesweeperGame;
import app.games.minesweeper.MinesweeperLevel;
import app.games.topdownobjects.Demon;
import app.games.topdownobjects.TopDownLevel;
import app.games.topdownobjects.Tower;

public class TestTask0 {

    private static double EPSILON = 1e-5;

    private static void compareBackgrounds(Background bg1, Background bg2) {
        assertEquals(bg1.usesBackgroundImage(), bg2.usesBackgroundImage());
        assertEquals(bg1.getBackgroundImageFileNames(), bg2.getBackgroundImageFileNames());
        assertEquals(bg1.getGroundTileSpriteLocation(), bg2.getGroundTileSpriteLocation());
        assertEquals(bg1.getParallaxRatios(), bg2.getParallaxRatios());
    }

    private static HashMap<Vector2D, StaticGameObject> listToMapStatic(ArrayList<StaticGameObject> list) {
        HashMap<Vector2D, StaticGameObject> map = new HashMap<>();
        for (StaticGameObject object : list) {
            map.put(object.getLocation(), object);
        }
        return map;
    }

    private static HashMap<Vector2D, DynamicGameObject> listToMapDynamic(ArrayList<DynamicGameObject> list) {
        HashMap<Vector2D, DynamicGameObject> map = new HashMap<>();
        for (DynamicGameObject object : list) {
            map.put(object.getLocation(), object);
        }
        return map;
    }

    private static void compareListsOfStaticObjects(ArrayList<StaticGameObject> list1,
            ArrayList<StaticGameObject> list2) {
        HashMap<Vector2D, StaticGameObject> map1 = listToMapStatic(list1);
        HashMap<Vector2D, StaticGameObject> map2 = listToMapStatic(list2);
        assertEquals(map1.size(), map2.size());

        for (Vector2D location : map1.keySet()) {
            assertTrue("Object at location: " + location, map2.containsKey(location));
            StaticGameObject object1 = map1.get(location);
            StaticGameObject object2 = map2.get(location);
            assertEquals("Object at location: " + location, object1.getClass(), object2.getClass());
            if (object1 instanceof Goal goal1 && object2 instanceof Goal goal2) {
                assertEquals("Object at location: " + location, goal1.getGame(), goal2.getGame());
            } else if (object1 instanceof InfoNode info1 && object2 instanceof InfoNode info2) {
                assertEquals("Object at location: " + location, info1.getMessage(), info2.getMessage());
            }
        }
    }

    private static void compareListsOfDynamicObjects(ArrayList<DynamicGameObject> list1,
            ArrayList<DynamicGameObject> list2) {
        HashMap<Vector2D, DynamicGameObject> map1 = listToMapDynamic(list1);
        HashMap<Vector2D, DynamicGameObject> map2 = listToMapDynamic(list2);
        assertEquals(map1.size(), map2.size());

        for (Vector2D location : map1.keySet()) {
            assertTrue("Object at location: " + location, map2.containsKey(location));
            DynamicGameObject object1 = map1.get(location);
            DynamicGameObject object2 = map2.get(location);
            assertEquals("Object at location: " + location, object1.getClass(), object2.getClass());
            if (object1 instanceof Demon demon1 && object2 instanceof Demon demon2) {
                assertEquals("Object at location: " + location, demon1.getStrength(), demon2.getStrength());
                assertEquals("Object at location: " + location, demon1.getMaxHP(), demon2.getMaxHP());
            }
        }
    }

    @Test
    public void testParseLevelNoFile() {
        SampleGame game = new SampleGame();
        Level level = LevelParser.parseLevel(game, "_______file_does-not_exist_________");
        assertEquals(null, level);
    }

    @Test
    public void testParseLevelTiny() {
        SampleGame game = new SampleGame();
        Level level = LevelParser.parseLevel(game, "testing/small.csv");
        level.update(0); // To finish adding static/dynamic objects
        // Level properties
        assertNotNull(level);
        assertEquals(5, level.getWidth());
        assertEquals(2, level.getHeight());
        assertEquals("smallLevel", level.getName());
        assertTrue(level instanceof TopDownLevel);
        assertEquals(2, level.getPlayerStartLocation().getX(), EPSILON);
        assertEquals(3, level.getPlayerStartLocation().getY(), EPSILON);
        // Background
        compareBackgrounds(new Background("MiniWorldSprites/Ground/Cliff.png", 4, 6), level.getBackground());
        // Level objects
        assertTrue(level.getStaticObjects().isEmpty());
        assertTrue(level.getDynamicObjects().isEmpty());
    }

    @Test
    public void testParseLevelSmall() {
        SampleGame game = new SampleGame();
        Level level = LevelParser.parseLevel(game, "testing/medium.csv");
        level.update(0); // To finish adding static/dynamic objects
        // Level properties
        assertNotNull(level);
        assertEquals(12, level.getWidth());
        assertEquals(10, level.getHeight());
        assertEquals("mediumLevel", level.getName());
        assertTrue(level instanceof TopDownLevel);
        assertEquals(2, level.getPlayerStartLocation().getX(), EPSILON);
        assertEquals(2, level.getPlayerStartLocation().getY(), EPSILON);
        // Background
        compareBackgrounds(new Background("MiniWorldSprites/Ground/Grass.png", 2, 0), level.getBackground());
        // Level objects
        ArrayList<StaticGameObject> expectedStatic = new ArrayList<>(Arrays.asList(new Wall(4, 3)));
        ArrayList<StaticGameObject> actualStatic = level.getStaticObjects();
        assertFalse(actualStatic.contains(null));
        compareListsOfStaticObjects(expectedStatic, actualStatic);

        ArrayList<DynamicGameObject> expectedDynamic = new ArrayList<>(Arrays.asList(new Demon(1, 1, 40, 5)));
        ArrayList<DynamicGameObject> actualDynamic = level.getDynamicObjects();
        assertFalse(actualDynamic.contains(null));
        compareListsOfDynamicObjects(expectedDynamic, actualDynamic);
    }

    @Test
    public void testParseLevelBig() {
        SampleGame game = new SampleGame();
        Level level = LevelParser.parseLevel(game, "testing/large.csv");
        level.update(0); // To finish adding static/dynamic objects
        // Level properties
        assertNotNull(level);
        assertEquals(200, level.getWidth());
        assertEquals(300, level.getHeight());
        assertEquals("bigLevel", level.getName());
        assertTrue(level instanceof TopDownLevel);
        assertEquals(6.12, level.getPlayerStartLocation().getX(), EPSILON);
        assertEquals(5.54, level.getPlayerStartLocation().getY(), EPSILON);
        // Background
        compareBackgrounds(new Background("MiniWorldSprites/Animals/Boar.png", 0, 0), level.getBackground());
        // Level objects
        ArrayList<StaticGameObject> expectedStatic = new ArrayList<>(Arrays.asList(new Wall(3, 32),
                new Wall(22, 44), new Wall(39, 32), new Wall(25, 21), new Wall(55, 24), new Wall(55, 40),
                new Wall(41, 23), new Wall(45, 6), new Wall(63, 7), new Wall(69, 23), new Wall(47, 34),
                new Wall(17, 35), new Wall(32, 23), new Wall(25, 4), new Wall(14, 19), new Goal(49, 19, game),
                new Goal(27, 37, game), new Goal(14, 28, game), new Goal(12, 43, game), new Goal(49, 46, game),
                new Goal(66, 36, game), new InfoNode(38, 43, "Testing message"),
                new InfoNode(70, 27, "Testing message1"), new InfoNode(61, 22, "Testing message2"),
                new InfoNode(19, 50, "Testing message3"), new InfoNode(23, 26, "Testing message4")));
        ArrayList<StaticGameObject> actualStatic = level.getStaticObjects();
        assertFalse(actualStatic.contains(null));
        compareListsOfStaticObjects(expectedStatic, actualStatic);

        ArrayList<DynamicGameObject> expectedDynamic = new ArrayList<>(Arrays.asList(new Demon(23, 95, 25, 3),
                new Demon(26, 98, 10, 12), new Demon(74, 101, 6, 18), new Demon(76, 93, 4, 6),
                new Demon(50, 106, 10000, 1), new Demon(79, 45, 102, 35), new Tower(53, 32), new Tower(84, 16),
                new Tower(85, 17), new Tower(85, 34), new Tower(84, 35), new Tower(83, 37), new Tower(83, 38),
                new Tower(80, 41)));
        ArrayList<DynamicGameObject> actualDynamic = level.getDynamicObjects();
        assertFalse(actualDynamic.contains(null));
        compareListsOfDynamicObjects(expectedDynamic, actualDynamic);
    }

    @Test
    public void testParseLevelWithBadObjects() {
        SampleGame game = new SampleGame();
        Level level = LevelParser.parseLevel(game, "testing/bad.csv");
        level.update(0); // To finish adding static/dynamic objects
        // Level properties
        assertNotNull(level);
        assertEquals(50, level.getWidth());
        assertEquals(42, level.getHeight());
        assertEquals("badLevel", level.getName());
        assertTrue(level instanceof TopDownLevel);
        assertEquals(2, level.getPlayerStartLocation().getX(), EPSILON);
        assertEquals(2, level.getPlayerStartLocation().getY(), EPSILON);
        // Background
        compareBackgrounds(new Background("MiniWorldSprites/Ground/Grass.png", 2, 0), level.getBackground());
        // Level objects
        ArrayList<StaticGameObject> expectedStatic = new ArrayList<>(Arrays.asList(new Wall(4, 3)));
        ArrayList<StaticGameObject> actualStatic = level.getStaticObjects();
        assertFalse(actualStatic.contains(null));
        compareListsOfStaticObjects(expectedStatic, actualStatic);

        ArrayList<DynamicGameObject> expectedDynamic = new ArrayList<>(Arrays.asList(new Demon(1, 1, 40, 5)));
        ArrayList<DynamicGameObject> actualDynamic = level.getDynamicObjects();
        assertFalse(actualDynamic.contains(null));
        compareListsOfDynamicObjects(expectedDynamic, actualDynamic);
    }

    @Test
    public void testGetAdjacentVectors() {
        MinesweeperGame game = new MinesweeperGame();
        MinesweeperLevel level = new MinesweeperLevel(game, 20, 20, 1);

        Vector2D target = new Vector2D(5, 5);
        HashSet<Vector2D> expected = new HashSet<>(Set.of(new Vector2D(4, 4), new Vector2D(4, 5),
                new Vector2D(4, 6), new Vector2D(5, 4), new Vector2D(5, 6), new Vector2D(6, 4), new Vector2D(6, 5),
                new Vector2D(6, 6)));
        HashSet<Vector2D> actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);

        target = new Vector2D(10, 3);
        expected = new HashSet<>(Set.of(new Vector2D(9, 2), new Vector2D(9, 3), new Vector2D(9, 4),
                new Vector2D(10, 2), new Vector2D(10, 4), new Vector2D(11, 2), new Vector2D(11, 3),
                new Vector2D(11, 4)));
        actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);

        target = new Vector2D(1, 1);
        expected = new HashSet<>(Set.of(new Vector2D(0, 0), new Vector2D(0, 1), new Vector2D(0, 2),
                new Vector2D(1, 0), new Vector2D(1, 2), new Vector2D(2, 0), new Vector2D(2, 1), new Vector2D(2, 2)));
        actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAdjacentVectorsEdge() {
        MinesweeperGame game = new MinesweeperGame();
        MinesweeperLevel level = new MinesweeperLevel(game, 20, 20, 1);

        // Use sets to ignore order
        Vector2D target = new Vector2D(5, 0);
        HashSet<Vector2D> expected = new HashSet<>(Set.of(new Vector2D(4, 0), new Vector2D(4, 1),
                new Vector2D(5, 1), new Vector2D(6, 0), new Vector2D(6, 1)));
        HashSet<Vector2D> actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);

        target = new Vector2D(19, 3);
        expected = new HashSet<>(Set.of(new Vector2D(18, 2), new Vector2D(18, 3), new Vector2D(18, 4),
                new Vector2D(19, 2), new Vector2D(19, 4)));
        actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);

        target = new Vector2D(10, 19);
        expected = new HashSet<>(Set.of(new Vector2D(9, 18), new Vector2D(9, 19), new Vector2D(10, 18),
                new Vector2D(11, 18), new Vector2D(11, 19)));
        actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);

        target = new Vector2D(0, 8);
        expected = new HashSet<>(Set.of(new Vector2D(0, 7), new Vector2D(0, 9), new Vector2D(1, 7),
                new Vector2D(1, 8), new Vector2D(1, 9)));
        actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAdjacentVectorsCorner() {
        MinesweeperGame game = new MinesweeperGame();
        MinesweeperLevel level = new MinesweeperLevel(game, 10, 10, 1);

        // Use sets to ignore order
        Vector2D target = new Vector2D(0, 0);
        HashSet<Vector2D> expected = new HashSet<>(Set.of(new Vector2D(0, 1), new Vector2D(1, 0), new Vector2D(1, 1)));
        HashSet<Vector2D> actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);

        target = new Vector2D(9, 0);
        expected = new HashSet<>(Set.of(new Vector2D(8, 0), new Vector2D(8, 1), new Vector2D(9, 1)));
        actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);

        target = new Vector2D(9, 9);
        expected = new HashSet<>(Set.of(new Vector2D(8, 8), new Vector2D(8, 9), new Vector2D(9, 8)));
        actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);

        target = new Vector2D(0, 9);
        expected = new HashSet<>(Set.of(new Vector2D(0, 8), new Vector2D(1, 8), new Vector2D(1, 9)));
        actual = new HashSet<>(level.getAdjacentVectors(target));
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAdjacentVectorsInvalidInput() {
        MinesweeperGame game = new MinesweeperGame();
        MinesweeperLevel level = new MinesweeperLevel(game, 10, 10, 1);

        for (int i = -1; i < 11; i++) {
            for (int j = -1; j < 11; j++) {
                if (i == -1 || i == 10 || j == -1 || j == 10) {
                    Vector2D target = new Vector2D(i, j);
                    ArrayList<Vector2D> actual = level.getAdjacentVectors(target);
                    assertEquals("Vectors adjacent to: " + target, new ArrayList<>(), actual);
                }
            }
        }
    }

}
