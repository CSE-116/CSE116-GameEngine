package app.display.common.effects;

import java.util.function.Supplier;

import app.Settings;
import javafx.scene.paint.Color;

/**
 * Visual effect indicating some kind of player powerup status.
 * <p>
 * This effect overlays a colored tiles tile to show that the effect is active.
 * It also takes a condition to determine when to disable the effect. This
 * should usually be one of the options in {@link Settings}
 * 
 * @see TileEffect
 * @see Settings
 */
public class PowerupEffect extends TileEffect {

    private Supplier<Boolean> condition;

    /**
     * Constructs a cyan tile effect that disappears when god mode is disabled.
     */
    public PowerupEffect(Color fillColor, Color outlineColor, Supplier<Boolean> condition) {
        super(fillColor, outlineColor);
        this.condition = condition;
    }

    @Override
    public boolean isFinished() {
        return !this.condition.get();
    }

}
