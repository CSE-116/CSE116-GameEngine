package app.games.snake;

import java.util.HashMap;

import app.display.common.SpriteLocation;
import app.display.common.sound.AudioManager;
import app.gameengine.model.gameobjects.Player;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * The player of a game of Snake. Mostly just a colored rectangle.
 * 
 * @see SnakeGame
 * @see SnakeLevel
 */
public class Snake extends Player {

    public Snake(double x, double y) {
        super(x, y, 10);
        this.setOrientation(0, 1);
        this.spriteSheetFilename = "snake/snakeColors.png";
        this.defaultSpriteLocation = new SpriteLocation(0, 0);
        this.animations = new HashMap<>();
    }

    @Override
    public void collideWithStaticObject(StaticGameObject otherObject) {
        if (otherObject.getObjectType().equals("SnakeFood")) {
            AudioManager.playSoundEffect("collect.wav");
        } else {
            AudioManager.playSoundEffect("explosion_small.wav");
            this.destroy();
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.setOrientation(0, 1);
    }

}
