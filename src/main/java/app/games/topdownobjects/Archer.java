package app.games.topdownobjects;

import app.display.common.SpriteLocation;

public class Archer extends Enemy {

    public Archer(double x, double y, int maxHP, int strength) {
        super(x, y, maxHP, strength);
        this.spriteSheetFilename = "MiniWorldSprites/Characters/Monsters/Orcs/ArcherGoblin.png";
        this.defaultSpriteLocation = new SpriteLocation(1, 2);
        this.animations.remove("attack_up");
        this.animations.remove("attack_down");
        this.animations.remove("attack_left");
        this.animations.remove("attack_right");
    }

    public Archer(double x, double y) {
        this(x, y, 100, 10);
    }

}
