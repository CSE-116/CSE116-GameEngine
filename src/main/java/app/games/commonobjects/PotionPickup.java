package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.Game;
import app.gameengine.model.gameobjects.Collectible;

/**
 * A single-use collectible which allows the player to heal some amount of
 * damage.
 * 
 * @see Collectible
 */
public class PotionPickup extends Collectible {

    public PotionPickup(double x, double y, int heal, Game game) {
        super(x, y, game, "Health Potion");
        this.spriteSheetFilename = "MiniWorldSprites/User Interface/Icons-Essentials.png";
        this.defaultSpriteLocation = new SpriteLocation(3, 1);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
