package app.games.topdownobjects;

import java.util.ArrayList;
import java.util.Arrays;

import app.display.common.SpriteLocation;
import app.display.common.effects.DeathEffect;
import app.display.common.effects.HealthBarEffect;
import app.gameengine.model.gameobjects.Agent;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.physics.Vector2D;

/**
 * An {@link Agent} capable of dealing melee damage, with a health bar and
 * preset animations.
 * <p>
 * The animations for this object work well for many sprite sheets withing the
 * MiniWorldSprites directory, but probably nothing else.
 * 
 * @see Agent
 * @see DynamicGameObject
 */
public abstract class Enemy extends Agent {

    private int strength;

    public Enemy(double x, double y, int maxHP, int strength) {
        super(x, y, maxHP);
        this.strength = strength;
        this.getEffects().add(new HealthBarEffect(this));
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        super.collideWithDynamicObject(otherObject);
        if (otherObject.isPlayer()) {
            otherObject.takeDamage(this.strength);
            String anim = "attack_" + this.getDirection();
            this.setAnimationState(anim);
        }
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public void onDestroy() {
        this.getEffects().add(new DeathEffect(this));
    }

    @Override
    public void initAnimations() {
        this.spriteSheetFilename = "MiniWorldSprites/Characters/Monsters/Demons/ArmouredRedDemon.png";
        this.animations.put("walk_down", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 0),
                new SpriteLocation(1, 0),
                new SpriteLocation(2, 0),
                new SpriteLocation(3, 0),
                new SpriteLocation(4, 0))));
        this.animations.put("walk_up", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 1),
                new SpriteLocation(1, 1),
                new SpriteLocation(2, 1),
                new SpriteLocation(3, 1),
                new SpriteLocation(4, 1))));
        this.animations.put("walk_right", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 2),
                new SpriteLocation(1, 2),
                new SpriteLocation(2, 2),
                new SpriteLocation(3, 2),
                new SpriteLocation(4, 2))));
        this.animations.put("walk_left", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 3),
                new SpriteLocation(1, 3),
                new SpriteLocation(2, 3),
                new SpriteLocation(3, 3),
                new SpriteLocation(4, 3))));
        this.animations.put("attack_down", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 4),
                new SpriteLocation(1, 4),
                new SpriteLocation(2, 4),
                new SpriteLocation(3, 4),
                new SpriteLocation(4, 4),
                new SpriteLocation(5, 4))));
        this.animations.put("attack_up", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 5),
                new SpriteLocation(1, 5),
                new SpriteLocation(2, 5),
                new SpriteLocation(3, 5),
                new SpriteLocation(4, 5),
                new SpriteLocation(5, 5))));
        this.animations.put("attack_right", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 6),
                new SpriteLocation(1, 6),
                new SpriteLocation(2, 6),
                new SpriteLocation(3, 6),
                new SpriteLocation(4, 6),
                new SpriteLocation(5, 6))));
        this.animations.put("attack_left", new ArrayList<>(Arrays.asList(
                new SpriteLocation(0, 7),
                new SpriteLocation(1, 7),
                new SpriteLocation(2, 7),
                new SpriteLocation(3, 7),
                new SpriteLocation(4, 7),
                new SpriteLocation(5, 7))));
    }

}
