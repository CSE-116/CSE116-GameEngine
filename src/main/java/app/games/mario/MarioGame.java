package app.games.mario;

import app.gameengine.LevelParser;
import app.gameengine.LinearGame;
import app.gameengine.Level;

/**
 * A game of Mario.
 * <p>
 * This class does nothing complicated, and really just stores levels.
 */
public class MarioGame extends LinearGame {

    public MarioGame() {
        super();
        this.setPlayer(new Mario(0, 0));
        this.setIconPath("mario.png");
    }

    @Override
    public String getName() {
        return "Mario";
    }

    @Override
    public void init() {
        super.init();
        Level start = LevelParser.parseLevel(this, "mario/mario1.csv");
        this.addLevel(start);
        this.addLevel(LevelParser.parseLevel(this, "mario/mario2.csv"));
        this.addLevel(LevelParser.parseLevel(this, "mario/Mario_1_1.csv"));
        this.loadLevel(start);
    }

}
