package app.games.commonobjects;

import app.display.common.SpriteLocation;
import app.gameengine.model.gameobjects.StaticGameObject;

/**
 * A {@link StaticGameObject} which displays a semi-transparent box.
 * <p>
 * While this can be used for a variety of things, it is intended for showing
 * paths, for example within {@link Agent} to show their path.
 */
public class PathTile extends StaticGameObject {

    public PathTile(double x, double y) {
        this(x, y, "green");
    }

    public PathTile(double x, double y, String color) {
        super(x, y);
        this.spriteSheetFilename = "MiniWorldSprites/User Interface/Highlighted-Boxes.png";
        int col = switch (color.toLowerCase()) {
            default -> 0;
            case "white" -> 0;
            case "green" -> 1;
            case "blue" -> 2;
            case "red" -> 3;
            case "yellow" -> 4;
        };
        this.defaultSpriteLocation = new SpriteLocation(col, 0);
    }

    @Override
    public boolean isSolid() {
        return false;
    }

}
