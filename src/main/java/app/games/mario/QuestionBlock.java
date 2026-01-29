package app.games.mario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import app.display.common.SpriteLocation;
import app.games.commonobjects.Wall;

/**
 * A block in Mario which gives powerups when jumped into from below.
 * <p>
 * Note that this block does not actually give powerups, and is
 * indistinguishable from the other kinds of blocks except in appearance.
 * 
 * @see Wall
 * @see MarioLevel
 * @see MarioGame
 */
public class QuestionBlock extends Wall {

    public QuestionBlock(double x, double y) {
        super(x, y);
        this.spriteSheetFilename = "mario/smb_blocks.png";
        this.defaultSpriteLocation = new SpriteLocation(5, 7);
    }

    @Override
    public void initAnimations() {
        this.animationDuration = 2.0 / 15;
        this.animations = new HashMap<>();
        this.animations.put("default", new ArrayList<>(Arrays.asList(
                new SpriteLocation(5, 7),
                new SpriteLocation(5, 7),
                new SpriteLocation(5, 7),
                new SpriteLocation(5, 7),
                new SpriteLocation(6, 7),
                new SpriteLocation(7, 7))));
    }

}
