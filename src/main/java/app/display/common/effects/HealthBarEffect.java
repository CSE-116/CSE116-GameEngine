package app.display.common.effects;

import app.Configuration;
import app.display.common.JFXManager;
import app.display.common.PlaceholderNode;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.physics.Vector2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Displays a health bar above a dynamic game object.
 * <p>
 * The health bar visually represents the object's current health and updates as
 * the object takes damage or heals. It disappears when the object is destroyed.
 * 
 * @see DynamicGameObject
 * @see StaticEffect
 */
public class HealthBarEffect extends StaticEffect {

    private Vector2D offset;
    private DynamicGameObject object;
    private Rectangle foreground;

    public HealthBarEffect(DynamicGameObject object) {
        this(object, new Vector2D(0, 0));
    }

    /**
     * Constructs a health bar effect that visually tracks the given object's
     * current health compared to their maximum health.
     * 
     * @param object the object possessing the health bar
     */
    public HealthBarEffect(DynamicGameObject object, Vector2D offset) {
        super(new StackPane());
        if (!JFXManager.isInitialized()) {
            this.node = new PlaceholderNode();
            return;
        }
        this.object = object;
        this.offset = offset;

        double scaleFactor = Configuration.SCALE_FACTOR;
        double barWidth = object.getSpriteWidth() * Configuration.ZOOM;
        double barHeight = barWidth / 10;
        double health = Math.max(0, Math.min(1, (double) object.getHP() / object.getMaxHP()));
        double barX = scaleFactor * object.getLocation().getX() + (scaleFactor - barWidth) / 2;
        double barY = scaleFactor * object.getLocation().getY();

        Rectangle background = new Rectangle(barX, barY, barWidth, barHeight);
        background.setFill(Color.DARKRED);

        this.foreground = new Rectangle(barX, barY, barWidth * health, barHeight);
        this.foreground.setWidth(barY);
        this.foreground.setFill(Color.LIMEGREEN);

        StackPane healthBar = new StackPane(background, this.foreground);
        StackPane.setAlignment(this.foreground, Pos.CENTER_LEFT);
        StackPane.setAlignment(background, Pos.CENTER_LEFT);
        this.node = healthBar;
    }

    @Override
    public Node getFrame(Vector2D origin) {
        return super.getFrame(Vector2D.add(origin, offset));
    }

    @Override
    public void update(double dt) {
        if (this.foreground != null && this.object != null) {
            double barWidth = object.getSpriteWidth() * Configuration.ZOOM;
            double health = Math.clamp((double) object.getHP() / object.getMaxHP(), 0, 1);
            this.foreground.setWidth(barWidth * health);
        }
    }

    @Override
    public boolean isFinished() {
        return this.object == null || this.object.isDestroyed();
    }

}
