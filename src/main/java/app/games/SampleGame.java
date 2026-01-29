package app.games;

import app.gameengine.Game;
import app.gameengine.LevelParser;
import app.games.topdownobjects.TopDownLevel;

/**
 * Sample game to show off some of the basic features of the engine.
 * <p>
 * Set of top-down levels showing some basic objects. Likely won't function at
 * all until level parsing and physics/collision are complete. Is hard-coded to
 * not rely on lists of levels.
 * <p>
 * Some of the objects in the level csv files may not exist yet, or ever. That's
 * fine.
 * 
 * @see Game
 * @see TopDownLevel
 * @see LevelParser
 */
public class SampleGame extends Game {

    private int level = 2;

    @Override
    public void init() {
        super.init();
        this.loadLevel(LevelParser.parseLevel(this, "sample/sample1.csv"));
    }

    @Override
    public void advanceLevel() {
        if (level > 5) {
            return;
        }
        this.loadLevel(LevelParser.parseLevel(this, "sample/sample" + level + ".csv"));
        level++;
    }

    @Override
    public void resetGame() {
        super.resetGame();
        this.loadLevel(LevelParser.parseLevel(this, "sample/sample1.csv"));
        this.level = 2;
    }

    @Override
    public String getName() {
        return "Sample CSE 116 Game";
    }

}
