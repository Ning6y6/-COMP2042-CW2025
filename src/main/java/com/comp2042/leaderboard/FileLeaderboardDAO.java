package com.comp2042.leaderboard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class FileLeaderboardDAO implements LeaderboardDAO {
    private static final String FILE_NAME = "leaderboard.txt";
    private static final Logger LOGGER = Logger.getLogger(FileLeaderboardDAO.class.getName());

    @Override
    public void saveScore(LeaderboardEntry entry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(entry.toString());
            writer.newLine();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save score to " + FILE_NAME, e);
        }
    }

    @Override
    public List<LeaderboardEntry> loadScores() {
        List<LeaderboardEntry> scores = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return scores;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            scores = reader.lines()
                    .map(LeaderboardEntry::fromString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load scores from " + FILE_NAME, e);
        }
        return scores;
    }
}
