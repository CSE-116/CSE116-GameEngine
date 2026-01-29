package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.display.common.effects.FancyTextEffect;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.gameengine.model.physics.Vector2D;

/**
 * A static object capable of displaying a message when in contact with the
 * player.
 * 
 * @see StaticGameObject
 * @see FancyTextEffect
 */
public class InfoNode extends StaticGameObject {

    private String message;
    private FancyTextEffect effect;
    private Vector2D offset;

    public InfoNode(double x, double y, String message) {
        super(x, y);
        this.message = message;
        this.animationDuration = 2.0;
        this.offset = new Vector2D(this.getSpriteDimensions().getX() / 2, 0);
        this.spriteSheetFilename = "MiniWorldSprites/User Interface/UiIcons.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 3);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        if (this.effect == null) {
            this.effect = new FancyTextEffect(this.offset, this.animationDuration, message);
        }
        if (this.effect.isFinished()) {
            this.effect.reset();
        }
        if (otherObject.isPlayer() && !this.getEffects().contains(this.effect)) {
            this.getEffects().add(this.effect);
        } else if (otherObject.isPlayer()) {
            this.effect.resetTime();
        }
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
