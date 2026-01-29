package app.gameengine.statistics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import app.gameengine.model.datastructures.BinaryTreeNode;
import app.gameengine.model.datastructures.Comparator;
import app.gameengine.model.datastructures.LinkedListNode;

/**
 * A scoreboard for sorting scores of specific games.
 */
public class Scoreboard {

    private static String STATS_DIRECTORY = "data/stats/";
    private static String STATS_POSTFIX = "_stats.csv";

    private String statsPath;
    private BinaryTreeNode<GameStat> root;

    public Scoreboard(String gameName, Comparator<GameStat> comparator) {
        this.statsPath = STATS_DIRECTORY + gameName.toLowerCase() + STATS_POSTFIX;
    }

    public void addScore(GameStat score) {

    }

    public LinkedListNode<GameStat> getScoreList() {
        return null;
    }

    public BinaryTreeNode<GameStat> getScoreTree() {
        return null;
    }

    public void loadStats() {

    }

    public void saveStats() {
        this.loadStats();
        if (this.root == null) {
            return;
        }
        System.out.println("** Saving statistics to " + this.statsPath + " **");
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.statsPath))) {
            for (LinkedListNode<GameStat> stats = this.getScoreList(); stats != null; stats = stats.getNext()) {
                writer.write(stats.getValue().toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("** Could not save statistics to " + this.statsPath + " **");
            e.printStackTrace();
        }
    }

}
