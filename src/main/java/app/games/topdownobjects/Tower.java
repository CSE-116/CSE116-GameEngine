package app.games.topdownobjects;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.gameobjects.DynamicGameObject;

public class Tower extends Agent {

    public final int damage = 5;

    public Tower(double x, double y) {
        super(x, y, 10);
        this.spriteSheetFilename = "MiniWorldSprites/Buildings/Lime/LimeTower.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 1);
    }

    @Override
    public void takeDamage(int damage) {
        // Tower is indestructible
    }

    @Override
    public void setHP(int hp) {
        // Tower is indestructible
    }

    @Override
    public void setLocation(double x, double y) {
        // Tower cannot move
    }

    @Override
    public void setVelocity(double x, double y) {
        // Tower cannot move
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        if (otherObject.isPlayer()) {
            otherObject.takeDamage(5);
        }
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
