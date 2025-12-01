package com.comp2042.leaderboard;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LeaderboardEntryParser {

    private static final String SEPARATOR = ":";
    private static final int EXPECTED_PARTS = 3;

    public LeaderboardEntry parse(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split(SEPARATOR);

        if (parts.length < EXPECTED_PARTS) {
            System.err.println("Skipping malformed line: " + line);
            return null;
        }

        try {
            String name = parts[0];
            int score = Integer.parseInt(parts[1]);
            LocalDate date = LocalDate.parse(parts[2]);

            return new LeaderboardEntry(name, score, date);

        } catch (NumberFormatException | DateTimeParseException e) {
            System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            return null;
        }
    }
}