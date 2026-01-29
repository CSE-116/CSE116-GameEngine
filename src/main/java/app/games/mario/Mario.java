package app.games.mario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import app.display.common.SpriteLocation;
import app.display.common.sound.AudioManager;
import app.games.platformerobjects.PlatformerPlayer;

/**
 * The player in a game of Mario. Some kind of plumber or something.
 * 
 * @see PlatformerPlayer
 * @see Player
 * @see MarioLevel
 * @see MarioGame
 */
public class Mario extends PlatformerPlayer {

    public Mario(double x, double y) {
        super(x, y, 10);
        this.getHitbox().setOffset(0, 0);
        this.getHitbox().setDimensions(1, 1);
        this.collider.setDimensions(1, 0.2);
        this.collider.setOffset(0.0, 0.81);
        this.maxCoyoteTime = 0;
        this.spriteSheetFilename = "mario/smb_mario_sheet.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
    }

    @Override
    public void jump() {
        super.jump();
        AudioManager.playSoundEffect("mario/jump.wav", 0.1);
    }

    @Override
    public void initAnimations() {
        this.animations = new HashMap<>();
        this.animations.put("default", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 0))));
        this.animations.put("death", new ArrayList<>(Arrays.asList(
                new SpriteLocation(6, 0))));
        this.animations.put("jump_right", new ArrayList<>(Arrays.asList(
                new SpriteLocation(5, 0))));
        this.animations.put("jump_left", new ArrayList<>(Arrays.asList(
                new SpriteLocation(5, 0, true, false))));
        this.animations.put("skid_right", new ArrayList<>(Arrays.asList(
                new SpriteLocation(4, 0))));
        this.animations.put("skid_left", new ArrayList<>(Arrays.asList(
                new SpriteLocation(4, 0, true, false))));
        this.animations.put("walk_right", new ArrayList<>(Arrays.asList(
                new SpriteLocation(1, 0),
                new SpriteLocation(2, 0),
                new SpriteLocation(3, 0))));
        this.animations.put("walk_left", new ArrayList<>(Arrays.asList(
                new SpriteLocation(1, 0, true, false),
                new SpriteLocation(2, 0, true, false),
                new SpriteLocation(3, 0, true, false))));
    }

    @Override
    public void freezeAnimations() {
        super.freezeAnimations();
        this.setAnimationState("default");
        this.animations.get("default").get(0).setReflectedHorizontally(this.getOrientation().getX() < 0);
    }

}
