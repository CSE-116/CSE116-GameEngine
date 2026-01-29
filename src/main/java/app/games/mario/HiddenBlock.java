package app.games.mario;

import app.display.common.SpriteLocation;

/**
 * A block in Mario which is invisible. Is supposed to sometimes give powerups,
 * but does not.
 * 
 * @see QuestionBlock
 * @see MarioLevel
 * @see MarioGame
 */
public class HiddenBlock extends QuestionBlock {

    public HiddenBlock(double x, double y) {
        super(x, y);
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
    }

    @Override
    public void initAnimations() {

    }

}
