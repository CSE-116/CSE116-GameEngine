package app.games.pacman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import app.display.common.SpriteLocation;
import app.display.common.sound.AudioManager;
import app.gameengine.model.gameobjects.Player;

/**
 * Player in a game of Pacman
 * 
 * @see Player
 * @see PacmanGame
 * @see PacmanLevel
 * @see PacmanControls
 */
public class Pacman extends Player {

    private int lives = 3;

    public Pacman(double x, double y) {
        super(x, y, 1);
        this.spriteSheetFilename = "pacman/pacmanSprites.png";
        this.defaultSpriteLocation = new SpriteLocation(1, 0);
        this.getHitbox().setDimensions(0.8, 0.8);
        this.getHitbox().setOffset(0.1, 0.1);
    }

    public int getLives() {
        return this.lives;
    }

    @Override
    public void initAnimations() {
        this.animations = new HashMap<>();
        this.animationDuration = 0.05;
        this.animations.put("default_right", new ArrayList<>(Arrays.asList(new SpriteLocation(1, 1))));
        this.animations.put("default_left", new ArrayList<>(Arrays.asList(new SpriteLocation(1, 1, true, false))));
        this.animations.put("default_up", new ArrayList<>(Arrays.asList(new SpriteLocation(1, 1, -90))));
        this.animations.put("default_down", new ArrayList<>(Arrays.asList(new SpriteLocation(1, 1, 90))));
        this.animations.put("move_right", new ArrayList<>(Arrays.asList(
                new SpriteLocation(1, 0),
                new SpriteLocation(1, 1),
                new SpriteLocation(0, 1),
                new SpriteLocation(1, 1))));
        this.animations.put("move_left", new ArrayList<>(Arrays.asList(
                new SpriteLocation(1, 0, true, false),
                new SpriteLocation(1, 1, true, false),
                new SpriteLocation(0, 1, true, false),
                new SpriteLocation(1, 1, true, false))));
        this.animations.put("move_up", new ArrayList<>(Arrays.asList(
                new SpriteLocation(1, 0, -90),
                new SpriteLocation(1, 1, -90),
                new SpriteLocation(0, 1, -90),
                new SpriteLocation(1, 1, -90))));
        this.animations.put("move_down", new ArrayList<>(Arrays.asList(
                new SpriteLocation(1, 0, 90),
                new SpriteLocation(1, 1, 90),
                new SpriteLocation(0, 1, 90),
                new SpriteLocation(1, 1, 90))));
    }

    @Override
    public void onDestroy() {
        AudioManager.playSoundEffect("pacman/fail.wav");
    }

    @Override
    public void reset() {
        super.reset();
        this.setOrientation(-1, 0);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.lives--;
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
