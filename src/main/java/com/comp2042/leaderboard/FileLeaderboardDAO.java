package com.comp2042.leaderboard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * FileLeaderboardDAO provides data access operations for storing and loading game data.
 */
public class FileLeaderboardDAO implements LeaderboardDAO {
    private static final String FILE_NAME = "leaderboard.txt";
    private static final Logger LOGGER = Logger.getLogger(FileLeaderboardDAO.class.getName());

    private final LeaderboardEntryParser parser;

    public FileLeaderboardDAO() {
        this.parser = new LeaderboardEntryParser();
    }

    @Override
    /**
     * Performs the save score operation.
     *
     * @param entry the entry
     */
    public void saveScore(LeaderboardEntry entry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(entry.toString());
            writer.newLine();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save score to " + FILE_NAME, e);
        }
    }

    @Override
    /**
     * Performs the load scores operation.
     *
     * @return the result of the operation
     */
    public List<LeaderboardEntry> loadScores() {
        List<LeaderboardEntry> scores = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return scores;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            scores = reader.lines()
                    .map(parser::parse)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load scores from " + FILE_NAME, e);
        }
        return scores;
    }
}