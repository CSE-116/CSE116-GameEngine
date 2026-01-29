package app.games.pacman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import app.display.common.SpriteLocation;
import app.display.common.sound.AudioManager;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.Vector2D;

/**
 * The ghost enemies in a game of Pacman.
 * <p>
 * Each ghost has one of four colors that determines certain aspects of their
 * behavior. Each ghost is at any time in one of five states which determines
 * how they pursue or flee from the player.
 * 
 * @see PacmanGame
 * @see PacmanLevel
 * @see Agent
 */
public class Ghost extends Agent {

    private PacmanGame game;
    private String color;
    private int spriteRow;
    private String state = "Spawner";

    public Ghost(double x, double y, PacmanGame game, String color) {
        super(x, y, 1);
        this.color = color;
        this.game = game;
        this.setState("Spawner");
        this.spriteRow = switch (color) {
            case "Pink":
                yield 1;
            case "Cyan":
                yield 2;
            case "Orange":
                yield 3;
            case "Red":
            default:
                yield 0;
        };
        this.spriteSheetFilename = "pacman/ghosts.png";
        this.defaultSpriteLocation = new SpriteLocation(0, this.spriteRow);
        this.initAnimations();
    }

    public String getColor() {
        return this.color;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.setPath(null);
        this.getVelocity().negate();
        if (state != this.state) {
            this.getOrientation().negate();
        }
        if (state.equals("Chase") || state.equals("Scatter")) {
            this.movementSpeed = 8;
        } else if (state.equals("Dead")) {
            this.setAnimationState("eyes_" + this.getDirection());
            this.movementSpeed = 15;
        } else if (state.equals("Frightened") || state.equals("Spawner")) {
            this.setAnimationState("frightened");
            this.movementSpeed = 6;
        }
        this.state = state;
    }

    @Override
    public void followPath(double dt) {
        Vector2D velocity = Vector2D.mul(this.getOrientation(), this.movementSpeed);
        this.setVelocity(velocity.getX(), velocity.getY());
        double dist = Vector2D.euclideanDistance(this.getLocation(), Vector2D.round(this.getLocation()));
        double threshold = dt * this.movementSpeed;
        if (dist < threshold && !this.state.equals("Spawner")) {
            this.getLocation().round();
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.state = "Spawner";
    }

    @Override
    public void revive() {
        super.revive();
        this.state = "Chase";
    }

    @Override
    public void setAnimationState(String newState) {
        if (this.state.equals("Frightened") && !(newState.equals("frightened") || newState.equals("frightened_end"))) {
            return;
        } else if (this.state.equals("Dead") && newState.startsWith("walk")) {
            super.setAnimationState("eyes_" + this.getDirection());
            return;
        }
        super.setAnimationState(newState);
    }

    @Override
    public void initAnimations() {
        this.animations = new HashMap<>();
        this.animationDuration = 0.2;
        this.animations.put("walk_right", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, this.spriteRow),
                new SpriteLocation(1, this.spriteRow))));
        this.animations.put("walk_left", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, this.spriteRow, true, false),
                new SpriteLocation(1, this.spriteRow, true, false))));
        this.animations.put("walk_up", new ArrayList<>(Arrays.asList(
                new SpriteLocation(4, this.spriteRow),
                new SpriteLocation(5, this.spriteRow))));
        this.animations.put("walk_down", new ArrayList<>(Arrays.asList(
                new SpriteLocation(2, this.spriteRow),
                new SpriteLocation(3, this.spriteRow))));
        this.animations.put("eyes_right", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 5))));
        this.animations.put("eyes_left", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 5, true, false))));
        this.animations.put("eyes_up", new ArrayList<>(Arrays.asList(
                new SpriteLocation(2, 5))));
        this.animations.put("eyes_down", new ArrayList<>(Arrays.asList(
                new SpriteLocation(1, 5))));
        this.animations.put("frightened", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 4),
                new SpriteLocation(1, 4))));
        this.animations.put("frightened_end", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 4),
                new SpriteLocation(1, 4),
                new SpriteLocation(2, 4),
                new SpriteLocation(3, 4))));
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        if (this.state.equals("Dead")) {
            return;
        } else if (this.state.equals("Frightened") && otherObject.isPlayer()) {
            this.setState("Dead");
            this.game.getCurrentLevel().eatGhost(this);
            this.onDestroy();
            return;
        } else if (otherObject.isPlayer()) {
            otherObject.takeDamage(1);
        }
    }

    @Override
    public void collideWithStaticObject(StaticGameObject otherObject) {
        super.collideWithStaticObject(otherObject);
        if (this.state.equals("Dead") && otherObject instanceof GhostHouse) {
            this.setState("Chase");
        }
    }

    @Override
    public void onDestroy() {
        AudioManager.playSoundEffect("pacman/eat_ghost.wav");
    }

    @Override
    public int getSpriteHeight() {
        return 32;
    }

    @Override
    public int getSpriteWidth() {
        return 32;
    }

    @Override
    public int getSpriteOffsetX() {
        return -8;
    }

    @Override
    public int getSpriteOffsetY() {
        return -8;
    }

}
