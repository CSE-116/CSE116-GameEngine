package app.games.pacman;

import app.display.common.SpriteLocation;
import app.display.common.effects.HitboxOutline;
import app.games.commonobjects.Wall;
import javafx.scene.paint.Color;

/**
 * A wall for use in Pacman.
 * <p>
 * This wall has many different sprites for the various kinds of walls and
 * corners in the level. It would have been smarter to just use a background
 * image.
 * 
 * @see Wall
 * @see PacmanGame
 */
public class PacmanWall extends Wall {

    public PacmanWall(double x, double y, String type) {
        super(x, y);
        this.spriteSheetFilename = "pacman/pacmanWalls.png";
        this.outlineEffect = new HitboxOutline(this, Color.RED);
        this.defaultSpriteLocation = switch (type) {
            case "UpperLeftOuter":
                yield new SpriteLocation(0, 0);
            case "UpperRightOuter":
                yield new SpriteLocation(0, 0, true, false);
            case "LowerLeftOuter":
                yield new SpriteLocation(0, 0, false, true);
            case "LowerRightOuter":
                yield new SpriteLocation(0, 0, true, true);
            case "UpperLeftInner":
                yield new SpriteLocation(0, 1);
            case "UpperRightInner":
                yield new SpriteLocation(0, 1, true, false);
            case "LowerLeftInner":
                yield new SpriteLocation(0, 1, false, true);
            case "LowerRightInner":
                yield new SpriteLocation(0, 1, true, true);
            case "UpperLeftSharp":
                yield new SpriteLocation(3, 0);
            case "UpperRightSharp":
                yield new SpriteLocation(3, 0, true, false);
            case "LowerLeftSharp":
                yield new SpriteLocation(3, 0, false, true);
            case "LowerRightSharp":
                yield new SpriteLocation(3, 0, true, true);
            case "UpperLeftCorner":
                yield new SpriteLocation(2, 1, true, false);
            case "UpperRightCorner":
                yield new SpriteLocation(2, 1);
            case "LowerLeftCorner":
                yield new SpriteLocation(2, 1, true, true);
            case "LowerRightCorner":
                yield new SpriteLocation(2, 1, false, true);
            case "UpperOuter":
                yield new SpriteLocation(1, 0);
            case "LowerOuter":
                yield new SpriteLocation(1, 0, false, true);
            case "LeftOuter":
                yield new SpriteLocation(1, 0, -90);
            case "RightOuter":
                yield new SpriteLocation(1, 0, 90);
            case "UpperInner":
                yield new SpriteLocation(1, 1);
            case "LowerInner":
                yield new SpriteLocation(1, 1, false, true);
            case "LeftInner":
                yield new SpriteLocation(1, 1, -90);
            case "RightInner":
                yield new SpriteLocation(1, 1, 90);
            case "UpperLeftFork":
                yield new SpriteLocation(2, 0);
            case "UpperRightFork":
                yield new SpriteLocation(2, 0, true, false);
            case "LowerLeftFork":
                yield new SpriteLocation(2, 0, false, true);
            case "LowerRightFork":
                yield new SpriteLocation(2, 0, true, true);
            case "LeftUpperFork":
                yield new SpriteLocation(2, 0, -90, true, false);
            case "LeftLowerFork":
                yield new SpriteLocation(2, 0, -90);
            case "RightUpperFork":
                yield new SpriteLocation(2, 0, 90);
            case "RightLowerFork":
                yield new SpriteLocation(2, 0, 90, true, false);
            case "Gate":
                yield new SpriteLocation(4, 0);
            case "Inner":
            default:
                yield new SpriteLocation(4, 1);
        };
    }

}
