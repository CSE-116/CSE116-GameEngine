package app.games.roguelikeobjects;

import app.gameengine.Game;
import app.gameengine.LevelParser;
import app.gameengine.utils.Randomizer;

/**
 * Factory class for creating {@link RoguelikeLevel}s for the
 * {@link RoguelikeGame}
 * 
 * @see RoguelikeLevel
 * @see RoguelikeGame
 */
public class RoguelikeLevelFactory {

    private static String[] safeLevelNames = { "DoubleLoot", "MazeLoot", "Nothing", "SingleLoot", "SingleLootWalls" };
    private static String[] dangerousLevelNames = { "DoubleEnemySpikes", "PlusDanger", "ScatteredSpikes", "SingleEnemy",
            "SingleEnemyWalls" };
    private static String[] bossLevelNames = { "LargeDanger" };

    private static RoguelikeLevel getLevel(Game game, String directory) {
        if (LevelParser.parseLevel(game, directory) instanceof RoguelikeLevel level)
            return level;
        System.err.println("** Level file " + directory + " could not be found or was not a RoguelikeLevel **");
        return null;
    }

    public static RoguelikeLevel getSafeLevel(Game game, String name) {
        String directory = "roguelike/Safe/" + name + ".csv";
        return getLevel(game, directory);
    }

    public static RoguelikeLevel getDangerousLevel(Game game, String name) {
        String directory = "roguelike/Dangerous/" + name + ".csv";
        return getLevel(game, directory);
    }

    public static RoguelikeLevel getRandomLevel(Game game) {
        if (Randomizer.randomBoolean()) {
            return getSafeLevel(game, Randomizer.randomSelect(safeLevelNames));
        }
        return getDangerousLevel(game, Randomizer.randomSelect(dangerousLevelNames));
    }

    public static RoguelikeLevel getBossLevel(Game game) {
        String directory = "roguelike/Dangerous/" + Randomizer.randomSelect(bossLevelNames) + ".csv";
        return getLevel(game, directory);
    }

    public static RoguelikeLevel getStartingLevel(Game game) {
        return getLevel(game, "roguelike/Home.csv");
    }
}
