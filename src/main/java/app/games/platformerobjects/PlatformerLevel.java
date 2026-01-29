package app.games.platformerobjects;

import app.display.common.Background;
import app.display.common.controller.PlatformerControls;
import app.gameengine.Game;
import app.gameengine.Level;
import app.gameengine.model.physics.PhysicsEngineWithGravity;

/**
 * A level for use in a platformer.
 * <p>
 * Uses physics which include gravity, and controls for a platformer. It's
 * player must be a {@link PlatformerPlayer}.
 * 
 * @see Level
 * @see PhysicsEngineWithGravity
 * @see PlatformerControls
 */
public class PlatformerLevel extends Level {

    public PlatformerLevel(Game game, int width, int height, String name) {
        super(game, new PhysicsEngineWithGravity(), width, height, name);
        this.keyboardControls = new PlatformerControls(game);
        this.background = new Background("nature/nature_4/full.png", 0.5);
    }

    @Override
    public PlatformerPlayer getPlayer() {
        return (PlatformerPlayer) super.getPlayer();
    }

    @Override
    public void load() {
        super.load();
        this.getPlayer().getOrientation().setY(0);
        this.getPlayer().revive();
    }

    @Override
    public void reset() {
        super.reset();
        this.getPlayer().getOrientation().setY(0);
        this.getPlayer().reset();
    }

    @Override
    public void update(double dt) {
        this.keyboardControls.processInput(dt);
        super.update(dt);
    }

}
