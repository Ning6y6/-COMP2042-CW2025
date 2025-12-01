package com.comp2042.leaderboard;

import java.io.Serializable;
import java.time.LocalDate;


public class LeaderboardEntry implements Serializable, Comparable<LeaderboardEntry> {
    private final String name;
    private final int score;
    private final LocalDate date;

    public LeaderboardEntry(String name, int score) {
        this.name = name;
        this.score = score;
        this.date = LocalDate.now();
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public LocalDate getDate() { return date; }

    @Override
    public int compareTo(LeaderboardEntry other) {
        // Descending order
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return name + ":" + score + ":" + date;
    }

    // Simple parser for file storage
    public static LeaderboardEntry fromString(String line) {
        String[] parts = line.split(":");
        if (parts.length < 3) return null;
        return new LeaderboardEntry(parts[0], Integer.parseInt(parts[1]));
    }
}