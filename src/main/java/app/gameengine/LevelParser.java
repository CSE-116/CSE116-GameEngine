package app.gameengine;

import java.util.ArrayList;

import app.display.common.Background;
import app.gameengine.model.gameobjects.DynamicGameObject;
import app.gameengine.model.gameobjects.StaticGameObject;
import app.games.commonobjects.Goal;
import app.games.commonobjects.InfoNode;
import app.games.commonobjects.Wall;
import app.games.topdownobjects.Demon;
import app.games.topdownobjects.TopDownLevel;
import app.games.topdownobjects.Tower;

/**
 * Static class for creating levels from formatted csv files.
 */
public class LevelParser {

    /**
     * Parse the csv file at the given location within the levels directory, and
     * return the level which that file represents.
     * 
     * @param game the game the level will be part of
     * @param path the path within the levels directory to the level
     * @return the parsed level
     */
    public static Level parseLevel(Game game, String path) {
        return new TopDownLevel(game, 20, 20, "BlankLevel");
    }

    /**
     * Creates and returns a {@code DynamicGameObject} as defined by the input list
     * of properties. The format of the input {@code ArrayList} is:
     * <p>
     * "DynamicGameObject,SubType,x,y,..."
     * <p>
     * Where SubType is the name of the class, x and y are the location of the
     * object, and any following items are additional constructor parameters.
     * 
     * @param game  the game this object will be a member of
     * @param level the level this object will be a member of
     * @param split the split line from the csv file describing the object
     * @return the object that is described by {@code split}
     */
    public static DynamicGameObject readDynamicObject(Game game, Level level, ArrayList<String> split) {
        double x = Double.parseDouble(split.get(2));
        double y = Double.parseDouble(split.get(3));
        String type = split.get(1);
        if (type.equals("Demon")) {
            return new Demon(x, y, Integer.parseInt(split.get(4)), Integer.parseInt(split.get(5)));
        } else if (type.equals("Tower")) {
            return new Tower(x, y);
        } else {
            return null;
        }
    }

    /**
     * Creates and returns a {@code StaticGameObject} as defined by the input list
     * of properties. The format of the input {@code ArrayList} is:
     * <p>
     * "StaticGameObject,SubType,x,y,..."
     * <p>
     * Where SubType is the name of the class, x and y are the location of the
     * object, and any following items are additional constructor parameters.
     * 
     * @param game  the game this object will be a member of
     * @param level the level this object will be a member of
     * @param split the split line from the csv file describing the object
     * @return the object that is described by {@code split}
     */
    public static StaticGameObject readStaticObject(Game game, Level level, ArrayList<String> split) {
        double x = Double.parseDouble(split.get(2));
        double y = Double.parseDouble(split.get(3));
        String type = split.get(1);
        if (type.equals("Wall")) {
            return new Wall(x, y);
        } else if (type.equals("Goal")) {
            return new Goal(x, y, game);
        } else if (type.equals("InfoNode")) {
            return new InfoNode(x, y, split.get(4));
        } else {
            return null;
        }
    }

    /**
     * Creates and returns a {@code Background} object as defined by the input list
     * of properties. If the first item in {@code split} must be either
     * <i>BackgroundImage</i> or <i>BackgroundTile</i>.
     * <p>
     * If the line defines a <i>BackgroundImage</i>, the following entries must all
     * be strings specifying the filepaths of those backgrounds within the
     * background directory. If multiple images are specified, they will be layered
     * back to front with evenly spaced parallax ratios from 0.0 to 1.0.
     * <p>
     * If the line defines a <i>BackgroundTile</i>, the following entries must be a
     * string specifying the filepath of that tile sprite sheet within the sprites
     * directory and two ints for the column and row within that sprite sheet, in
     * that order.
     * 
     * @param split the split line from the csv file describing the object
     * @return the background that is described by {@code split}
     */
    public static Background readBackground(ArrayList<String> split) {
        if (split.get(0).equals("BackgroundImage")) {
            return new Background(new ArrayList<>(split.subList(1, split.size())));
        } else {
            int column = Integer.parseInt(split.get(2));
            int row = Integer.parseInt(split.get(3));
            return new Background(split.get(1), column, row);
        }
    }

}
