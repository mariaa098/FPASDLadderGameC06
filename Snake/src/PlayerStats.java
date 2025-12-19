import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerStats {
    private static final String STATS_FILE = "player_stats.dat";

    // Map untuk menyimpan total wins per player
    private static Map<String, Integer> playerWins = new HashMap<>();

    // Map untuk menyimpan highest score per player
    private static Map<String, Integer> playerHighScores = new HashMap<>();

    static {
        loadStats();
    }

    public static void recordWin(String playerName, int finalScore) {
        // Update wins
        playerWins.put(playerName, playerWins.getOrDefault(playerName, 0) + 1);

        // Update high score
        int currentHigh = playerHighScores.getOrDefault(playerName, 0);
        if (finalScore > currentHigh) {
            playerHighScores.put(playerName, finalScore);
        }

        saveStats();
    }

    public static void recordScore(String playerName, int score) {
        int currentHigh = playerHighScores.getOrDefault(playerName, 0);
        if (score > currentHigh) {
            playerHighScores.put(playerName, score);
            saveStats();
        }
    }

    private static void saveStats() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STATS_FILE))) {
            oos.writeObject(playerWins);
            oos.writeObject(playerHighScores);
        } catch (IOException e) {
            System.err.println("Error saving stats: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadStats() {
        File file = new File(STATS_FILE);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STATS_FILE))) {
            playerWins = (Map<String, Integer>) ois.readObject();
            playerHighScores = (Map<String, Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading stats: " + e.getMessage());
            playerWins = new HashMap<>();
            playerHighScores = new HashMap<>();
        }
    }
}
