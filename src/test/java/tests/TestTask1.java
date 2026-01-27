package tests;
// package app.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import org.junit.Test;

import app.gameengine.model.gameobjects.Player;
import app.gameengine.model.physics.Hitbox;
import app.gameengine.model.physics.PhysicsEngine;
import app.gameengine.model.physics.Vector2D;
import app.games.commonobjects.Wall;
import app.games.topdownobjects.Demon;
import javafx.util.Pair;

public class TestTask1 {

    static final double EPSILON = 1e-5;

    @Test
    public void testGetOverlapOnEdges() {
        Hitbox hmid = new Hitbox(new Vector2D(0, 0), new Vector2D(1, 1));
        Hitbox hr = new Hitbox(new Vector2D(1, 0), new Vector2D(1, 1));
        Hitbox hl = new Hitbox(new Vector2D(-1, 0), new Vector2D(1, 1));
        Hitbox hb = new Hitbox(new Vector2D(0, 1), new Vector2D(1, 1));
        Hitbox ht = new Hitbox(new Vector2D(0, -1), new Vector2D(1, 1));

        PhysicsEngine engine = new PhysicsEngine();

        assertTrue(engine.getOverlap(hmid, hr) <= 0);
        assertTrue(engine.getOverlap(hr, hmid) <= 0);
        assertTrue(engine.getOverlap(hmid, hl) <= 0);
        assertTrue(engine.getOverlap(hl, hmid) <= 0);
        assertTrue(engine.getOverlap(hmid, hb) <= 0);
        assertTrue(engine.getOverlap(hb, hmid) <= 0);
        assertTrue(engine.getOverlap(hmid, ht) <= 0);
        assertTrue(engine.getOverlap(ht, hmid) <= 0);

        assertTrue(engine.getOverlap(hr, ht) <= 0);
        assertTrue(engine.getOverlap(ht, hr) <= 0);
        assertTrue(engine.getOverlap(hr, hb) <= 0);
        assertTrue(engine.getOverlap(hb, hr) <= 0);

        assertTrue(engine.getOverlap(hl, ht) <= 0);
        assertTrue(engine.getOverlap(ht, hl) <= 0);
        assertTrue(engine.getOverlap(hl, hb) <= 0);
        assertTrue(engine.getOverlap(hb, hl) <= 0);
    }

    @Test
    public void testGetOverlapWithCompleteOverlap() {
        HashMap<Pair<Hitbox, Hitbox>, Double> testCases = new HashMap<>();
        Hitbox hb1 = new Hitbox(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Hitbox hb2 = new Hitbox(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Hitbox hb3 = new Hitbox(new Vector2D(1.0, 3.0), new Vector2D(1.0, 1.0));
        Hitbox hb4 = new Hitbox(new Vector2D(1.0, 3.0), new Vector2D(1.0, 1.0));
        Hitbox hb5 = new Hitbox(new Vector2D(4.0, 2.0), new Vector2D(1.0, 1.0));
        Hitbox hb6 = new Hitbox(new Vector2D(4.0, 2.0), new Vector2D(1.0, 1.0));

        testCases.put(new Pair<Hitbox, Hitbox>(hb1, hb1), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb2, hb2), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb3, hb3), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb5, hb5), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb1, hb2), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb3, hb4), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb5, hb6), 1.0);

        PhysicsEngine engine = new PhysicsEngine();

        for (Pair<Hitbox, Hitbox> hitboxes : testCases.keySet()) {
            String failString = String.format("Collision between hitboxes at %s and %s",
                    hitboxes.getKey().getLocation(), hitboxes.getValue().getLocation());
            assertEquals(failString, testCases.get(hitboxes),
                    engine.getOverlap(hitboxes.getKey(), hitboxes.getValue()), EPSILON);
            assertEquals(failString, testCases.get(hitboxes),
                    engine.getOverlap(hitboxes.getValue(), hitboxes.getKey()), EPSILON);
        }
    }

    @Test
    public void testGetOverlapWithPartialOverlap() {
        HashMap<Pair<Hitbox, Hitbox>, Double> testCases = new HashMap<>();
        Hitbox hb1 = new Hitbox(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Hitbox hb2 = new Hitbox(new Vector2D(1.0, 1.0), new Vector2D(4.0, 4.0));
        Hitbox hb3 = new Hitbox(new Vector2D(0.5, 4.5), new Vector2D(1.0, 1.0));
        Hitbox hb4 = new Hitbox(new Vector2D(4.0, 3.0), new Vector2D(1.0, 1.0));
        Hitbox hb5 = new Hitbox(new Vector2D(4.0, 3.0), new Vector2D(3.0, 3.0));
        Hitbox hb6 = new Hitbox(new Vector2D(3.0, 0.0), new Vector2D(2.0, 2.0));
        Hitbox hb7 = new Hitbox(new Vector2D(2.0, 3.0), new Vector2D(0.5, 0.5));
        Hitbox hb8 = new Hitbox(new Vector2D(3.0, 2.0), new Vector2D(2.0, 3.0));
        Hitbox hb9 = new Hitbox(new Vector2D(1.5, 4.0), new Vector2D(1.0, 1.0));
        Hitbox hb10 = new Hitbox(new Vector2D(1.0, 3.0), new Vector2D(2.0, 2.0));

        testCases.put(new Pair<Hitbox, Hitbox>(hb1, hb1), 2.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb2, hb2), 4.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb3, hb3), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb5, hb5), 3.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb2, hb1), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb2, hb4), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb2, hb6), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb4, hb5), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb5, hb2), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb3, hb2), 0.5);
        testCases.put(new Pair<Hitbox, Hitbox>(hb2, hb7), 1.5);
        testCases.put(new Pair<Hitbox, Hitbox>(hb2, hb8), 2.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb8, hb5), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb9, hb2), 1.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb2, hb10), 2.0);
        testCases.put(new Pair<Hitbox, Hitbox>(hb7, hb10), 0.5);
        testCases.put(new Pair<Hitbox, Hitbox>(hb3, hb10), 0.5);
        testCases.put(new Pair<Hitbox, Hitbox>(hb9, hb10), 1.0);

        PhysicsEngine engine = new PhysicsEngine();

        for (Pair<Hitbox, Hitbox> hitboxes : testCases.keySet()) {
            String failString = String.format("Collision between hitboxes at %s and %s",
                    hitboxes.getKey().getLocation(), hitboxes.getValue().getLocation());
            assertEquals(failString, testCases.get(hitboxes),
                    engine.getOverlap(hitboxes.getKey(), hitboxes.getValue()), EPSILON);
            assertEquals(failString, testCases.get(hitboxes),
                    engine.getOverlap(hitboxes.getValue(), hitboxes.getKey()), EPSILON);
        }
    }

    @Test
    public void testGetOverlapWithNoOverlap() {
        HashMap<Hitbox, Hitbox> testCases = new HashMap<>();
        Hitbox hb1 = new Hitbox(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Hitbox hb3 = new Hitbox(new Vector2D(0.5, 4.5), new Vector2D(1.0, 1.0));
        Hitbox hb4 = new Hitbox(new Vector2D(4.0, 3.0), new Vector2D(1.0, 1.0));
        Hitbox hb5 = new Hitbox(new Vector2D(4.0, 3.0), new Vector2D(3.0, 3.0));
        Hitbox hb6 = new Hitbox(new Vector2D(3.0, 0.0), new Vector2D(2.0, 2.0));

        testCases.put(hb1, hb3);
        testCases.put(hb1, hb4);
        testCases.put(hb1, hb5);
        testCases.put(hb1, hb6);
        testCases.put(hb3, hb4);
        testCases.put(hb3, hb5);
        testCases.put(hb3, hb6);
        testCases.put(hb4, hb6);
        testCases.put(hb5, hb6);

        PhysicsEngine engine = new PhysicsEngine();

        for (Entry<Hitbox, Hitbox> hitboxes : testCases.entrySet()) {
            String failString = String.format("Collision between hitboxes at %s and %s",
                    hitboxes.getKey().getLocation(), hitboxes.getValue().getLocation());
            assertTrue(failString, engine.getOverlap(hitboxes.getKey(), hitboxes.getValue()) <= 0);
            assertTrue(failString, engine.getOverlap(hitboxes.getValue(), hitboxes.getKey()) <= 0);
        }
    }

    @Test
    public void testWallCollisionsSimple() {
        Player player = new Player(0, 0, 10);
        player.getHitbox().setOffset(0.1, 0.1);
        player.getHitbox().setDimensions(0.8, 0.8);
        Wall w1 = new Wall(1, 0);
        w1.getHitbox().setOffset(0, 0);
        w1.getHitbox().setDimensions(1, 1);
        Wall w2 = new Wall(0, 1);
        w2.getHitbox().setOffset(0, 0);
        w2.getHitbox().setDimensions(1, 1);
        Wall w3 = new Wall(-1, 0);
        w3.getHitbox().setOffset(0, 0);
        w3.getHitbox().setDimensions(1, 1);
        Wall w4 = new Wall(0, -1);
        w4.getHitbox().setOffset(0, 0);
        w4.getHitbox().setDimensions(1, 1);

        // Move right
        player.getLocation().setX(0.5);
        player.getLocation().setY(0);
        w1.collideWithDynamicObject(player);
        assertEquals(0.1, player.getLocation().getX(), EPSILON);
        assertEquals(0.0, player.getLocation().getY(), EPSILON);

        // Move down
        player.getLocation().setX(0);
        player.getLocation().setY(0.5);
        w2.collideWithDynamicObject(player);
        assertEquals(0.0, player.getLocation().getX(), EPSILON);
        assertEquals(0.1, player.getLocation().getY(), EPSILON);

        // Move left
        player.getLocation().setX(-0.5);
        player.getLocation().setY(0);
        w3.collideWithDynamicObject(player);
        assertEquals(-0.1, player.getLocation().getX(), EPSILON);
        assertEquals(0.0, player.getLocation().getY(), EPSILON);

        // Move up
        player.getLocation().setX(0);
        player.getLocation().setY(-0.5);
        w4.collideWithDynamicObject(player);
        assertEquals(0.0, player.getLocation().getX(), EPSILON);
        assertEquals(-0.1, player.getLocation().getY(), EPSILON);
    }

    @Test
    public void testWallCollisionsComplex() {
        Player player = new Player(0.0, 0.0, 10);
        player.getHitbox().setOffset(0.1, 0.1);
        player.getHitbox().setDimensions(0.8, 0.8);
        Wall w = new Wall(5, 2);
        w.getHitbox().setOffset(0, 0);
        w.getHitbox().setDimensions(1, 1);

        player.getLocation().setX(4.5);
        player.getLocation().setY(1.2);
        player.getVelocity().setX(1.5);
        player.getVelocity().setY(-2.5);
        w.collideWithDynamicObject(player);
        assertEquals(4.5, player.getLocation().getX(), EPSILON);
        assertEquals(1.1, player.getLocation().getY(), EPSILON);
        assertEquals(1.5, player.getVelocity().getX(), EPSILON);
        assertEquals(0, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(5.0);
        player.getLocation().setY(1.2);
        player.getVelocity().setX(-142);
        player.getVelocity().setY(0.22);
        w.collideWithDynamicObject(player);
        assertEquals(5.0, player.getLocation().getX(), EPSILON);
        assertEquals(1.1, player.getLocation().getY(), EPSILON);
        assertEquals(-142, player.getVelocity().getX(), EPSILON);
        assertEquals(0, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(5.5);
        player.getLocation().setY(1.2);
        player.getVelocity().setX(0);
        player.getVelocity().setY(0);
        w.collideWithDynamicObject(player);
        assertEquals(5.5, player.getLocation().getX(), EPSILON);
        assertEquals(1.1, player.getLocation().getY(), EPSILON);
        assertEquals(0, player.getVelocity().getX(), EPSILON);
        assertEquals(0, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(5.8);
        player.getLocation().setY(1.3);
        player.getVelocity().setX(1);
        player.getVelocity().setY(1);
        w.collideWithDynamicObject(player);
        assertEquals(5.9, player.getLocation().getX(), EPSILON);
        assertEquals(1.3, player.getLocation().getY(), EPSILON);
        assertEquals(0, player.getVelocity().getX(), EPSILON);
        assertEquals(1, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(5.7);
        player.getLocation().setY(1.5);
        player.getVelocity().setX(1);
        player.getVelocity().setY(1);
        w.collideWithDynamicObject(player);
        assertEquals(5.9, player.getLocation().getX(), EPSILON);
        assertEquals(1.5, player.getLocation().getY(), EPSILON);
        assertEquals(0, player.getVelocity().getX(), EPSILON);
        assertEquals(1, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(5.7);
        player.getLocation().setY(2.5);
        player.getVelocity().setX(1);
        player.getVelocity().setY(1);
        w.collideWithDynamicObject(player);
        assertEquals(5.9, player.getLocation().getX(), EPSILON);
        assertEquals(2.5, player.getLocation().getY(), EPSILON);
        assertEquals(0, player.getVelocity().getX(), EPSILON);
        assertEquals(1, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(5.8);
        player.getLocation().setY(2.7);
        player.getVelocity().setX(1);
        player.getVelocity().setY(1);
        w.collideWithDynamicObject(player);
        assertEquals(5.9, player.getLocation().getX(), EPSILON);
        assertEquals(2.7, player.getLocation().getY(), EPSILON);
        assertEquals(0, player.getVelocity().getX(), EPSILON);
        assertEquals(1, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(5.2);
        player.getLocation().setY(2.8);
        player.getVelocity().setX(1);
        player.getVelocity().setY(1);
        w.collideWithDynamicObject(player);
        assertEquals(5.2, player.getLocation().getX(), EPSILON);
        assertEquals(2.9, player.getLocation().getY(), EPSILON);
        assertEquals(1, player.getVelocity().getX(), EPSILON);
        assertEquals(0, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(4.4);
        player.getLocation().setY(2.7);
        player.getVelocity().setX(1);
        player.getVelocity().setY(1);
        w.collideWithDynamicObject(player);
        assertEquals(4.4, player.getLocation().getX(), EPSILON);
        assertEquals(2.9, player.getLocation().getY(), EPSILON);
        assertEquals(1, player.getVelocity().getX(), EPSILON);
        assertEquals(0, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(4.2);
        player.getLocation().setY(2.0);
        player.getVelocity().setX(1);
        player.getVelocity().setY(1);
        w.collideWithDynamicObject(player);
        assertEquals(4.1, player.getLocation().getX(), EPSILON);
        assertEquals(2.0, player.getLocation().getY(), EPSILON);
        assertEquals(0, player.getVelocity().getX(), EPSILON);
        assertEquals(1, player.getVelocity().getY(), EPSILON);

        player.getLocation().setX(4.2);
        player.getLocation().setY(1.5);
        player.getVelocity().setX(1);
        player.getVelocity().setY(1);
        w.collideWithDynamicObject(player);
        assertEquals(4.1, player.getLocation().getX(), EPSILON);
        assertEquals(1.5, player.getLocation().getY(), EPSILON);
        assertEquals(0, player.getVelocity().getX(), EPSILON);
        assertEquals(1, player.getVelocity().getY(), EPSILON);
    }

    @Test
    public void testWallCollisionsCorners() {
        Player player = new Player(0, 0, 10);
        player.getHitbox().setOffset(0.1, 0.1);
        player.getHitbox().setDimensions(0.8, 0.8);
        Wall w = new Wall(2, 5);
        w.getHitbox().setOffset(0, 0);
        w.getHitbox().setDimensions(1, 1);

        // Upper left
        Vector2D shifted = new Vector2D(1.1, 4.1);
        ArrayList<Vector2D> positions = new ArrayList<>(
                Arrays.asList(new Vector2D(1.2, 4.2), new Vector2D(1.5, 4.5), new Vector2D(1.8, 4.8)));
        for (Vector2D pos : positions) {
            player.setLocation(pos.getX(), pos.getY());
            w.collideWithDynamicObject(player);
            String failString = "Collision with Player position of " + pos;
            assertTrue(failString, new Vector2D(pos.getX(), shifted.getY()).equals(player.getLocation())
                    || new Vector2D(shifted.getX(), pos.getY()).equals(player.getLocation()));
        }
        // Upper right
        shifted = new Vector2D(2.9, 4.1);
        positions = new ArrayList<>(
                Arrays.asList(new Vector2D(2.8, 4.2), new Vector2D(2.5, 4.5), new Vector2D(2.2, 4.8)));
        for (Vector2D pos : positions) {
            player.setLocation(pos.getX(), pos.getY());
            w.collideWithDynamicObject(player);
            String failString = "Collision with Player position of " + pos;
            assertTrue(failString, new Vector2D(pos.getX(), shifted.getY()).equals(player.getLocation())
                    || new Vector2D(shifted.getX(), pos.getY()).equals(player.getLocation()));
        }
        // Lower right
        shifted = new Vector2D(2.9, 5.9);
        positions = new ArrayList<>(
                Arrays.asList(new Vector2D(2.8, 5.8), new Vector2D(2.5, 5.5), new Vector2D(2.2, 5.2)));
        for (Vector2D pos : positions) {
            player.setLocation(pos.getX(), pos.getY());
            w.collideWithDynamicObject(player);
            String failString = "Collision with Player position of " + pos;
            assertTrue(failString, new Vector2D(pos.getX(), shifted.getY()).equals(player.getLocation())
                    || new Vector2D(shifted.getX(), pos.getY()).equals(player.getLocation()));
        }
        // Lower left
        shifted = new Vector2D(1.1, 5.9);
        positions = new ArrayList<>(
                Arrays.asList(new Vector2D(1.2, 5.8), new Vector2D(1.5, 5.5), new Vector2D(1.8, 5.2)));
        for (Vector2D pos : positions) {
            player.setLocation(pos.getX(), pos.getY());
            w.collideWithDynamicObject(player);
            String failString = "Collision with Player position of " + pos;
            assertTrue(failString, new Vector2D(pos.getX(), shifted.getY()).equals(player.getLocation())
                    || new Vector2D(shifted.getX(), pos.getY()).equals(player.getLocation()));
        }
    }

    @Test
    public void testWallCollisionsCompleteOverlap() {
        Player player = new Player(1, 1, 10);
        player.getHitbox().setOffset(0.1, 0.1);
        player.getHitbox().setDimensions(0.8, 0.8);
        Wall w = new Wall(1, 1);
        w.getHitbox().setOffset(0, 0);
        w.getHitbox().setDimensions(1, 1);

        w.collideWithDynamicObject(player);
        assertTrue(new Vector2D(1.0, 0.1).equals(player.getLocation())
                || new Vector2D(1.9, 1.0).equals(player.getLocation())
                || new Vector2D(1.0, 1.9).equals(player.getLocation())
                || new Vector2D(0.1, 1.0).equals(player.getLocation()));

        Demon demon = new Demon(1, 1);
        demon.getHitbox().setOffset(0, 0);
        demon.getHitbox().setDimensions(1, 1);
        w.collideWithDynamicObject(demon);
        assertTrue(new Vector2D(1.0, 0.0).equals(demon.getLocation())
                || new Vector2D(2.0, 1.0).equals(demon.getLocation())
                || new Vector2D(1.0, 2.0).equals(demon.getLocation())
                || new Vector2D(0.0, 1.0).equals(demon.getLocation()));
    }
    
}
