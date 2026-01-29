package app.games;

import app.Configuration;
import app.StartGame;
import app.gameengine.Game;
import app.games.mario.MarioGame;
import app.games.minesweeper.MinesweeperGame;
import app.games.pacman.PacmanGame;
import app.games.roguelikeobjects.RoguelikeGame;
import app.games.snake.SnakeGame;
import javafx.application.Platform;

/**
 * Factory class for creating {@code Game} objects.
 * <p>
 * Primarily used by {@link StartGame} to create the game being played. Which
 * game that is is defined within {@link Configuration}.
 * 
 * @see StartGame
 * @see Configuration
 */
public class GameFactory {

    /**
     * Prevent instantiation, as this class is intended to be static.
     */
    private GameFactory() {
    }

    /**
     * Depending on the input string, return a {@code Game} of that type.
     * 
     * @param gameName the name of the game to be created
     * @return the game associated with that name
     */
    public static Game getGame(String gameName) {
        Game game = null;
        switch (gameName.toLowerCase()) {
            case "minesweeper":
                game = new MinesweeperGame();
                break;
            case "sample game":
                game = new SampleGame();
                break;
            case "snake":
                game = new SnakeGame();
                break;
            case "mario":
                game = new MarioGame();
                break;
            case "pacman":
                game = new PacmanGame();
                break;
            case "roguelike":
                game = new RoguelikeGame();
                break;
            default:
                System.err.printf("*** No such game \"%s\". Exiting ***\n", gameName);
                Platform.exit();
                return game;
        }
        game.init();
        return game;
    }

}
