package app.games.pacman;

import app.display.common.SpriteLocation;
import app.display.common.sound.AudioManager;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A pellet in a game of Pacman.
 * <p>
 * Pellets increase score and are required to win the game. {@link PowerPellet}s
 * cause the player to enter a state where you can eat the ghosts for extra points.
 * 
 * @see PowerPellet
 * @see PacmanLevel
 * @see PacmanGame
 * @see Ghost
 * @see StaticGameObject
 */
public class Pellet extends StaticGameObject {

    private PacmanGame game;

    public Pellet(double x, double y, PacmanGame game) {
        super(x, y);
        this.game = game;
        this.spriteSheetFilename = "pacman/pacmanSprites.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        if (otherObject.isPlayer()) {
            this.game.getCurrentLevel().eatPellet(this.getObjectType().equals("PowerPellet"));
            this.destroy();
        }
    }

    @Override
    public void onDestroy() {
        AudioManager.playSoundEffect("pacman/eat.wav");
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
