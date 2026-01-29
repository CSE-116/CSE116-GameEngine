package app.games.mario;

import app.display.common.Background;
import app.display.common.sound.AudioManager;
import app.gameengine.Game;
import app.gameengine.model.physics.Vector2D;
import app.gameengine.utils.GameUtils;
import app.games.platformerobjects.PlatformerLevel;

/**
 * A level in a game of Mario.
 * 
 * @see MarioGame
 */
public class MarioLevel extends PlatformerLevel {

    public MarioLevel(Game game, int width, int height, String name) {
        super(game, width, height, name);
        this.keyboardControls = new MarioControls(this.game);
        this.background = new Background("mario/smb_background.png", 1.0);
    }

    @Override
    public int getViewWidth() {
        return Math.min(this.getHeight(), 16);
    }

    @Override
    public int getViewHeight() {
        return Math.min(this.getHeight(), 16);
    }

    @Override
    public void onStart() {
        AudioManager.playMusic("mario/GroundTheme.wav");
    }

    @Override
    public void update(double dt) {
        if (!isInBounds(this.getPlayer().getLocation())) {
            this.getPlayer().destroy();
        }
        super.update(dt);
    }

    @Override
    public String getUIString() {
        return "Level: " + this.getName();
    }

    /**
     * Checks if the given vector is within the bounds of this level. This is
     * necessary because it is more lax than {@link GameUtils#isInBounds}, in that
     * the location must be further out of bounds to be counted, and being above the
     * level does not count as out of bounds.
     * 
     * @param v the vector
     * @return whether it is in bounds
     */
    private boolean isInBounds(Vector2D v) {
        return v.getX() >= -1 && v.getX() < this.getWidth() && v.getY() < this.getHeight();
    }

}
