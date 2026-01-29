package app.games.snake;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * Food for a game of snake. Being eaten causes the snake to increase in length.
 * Pretty much just a colored rectangle.
 * 
 * @see SnakeGame
 * @see SnakeLevel
 */
public class SnakeFood extends StaticGameObject {

    private SnakeLevel level;

    public SnakeFood(double x, double y, SnakeLevel level) {
        super(x, y);
        this.level = level;
        this.spriteSheetFilename = "snake/snakeColors.png";
        this.defaultSpriteLocation = new SpriteLocation(2, 0);
    }

    @Override
    public void collideWithDynamicObject(DynamicGameObject otherObject) {
        this.destroy();
        this.level.spawnFood();
        this.level.lengthenSnake();
    }

}
