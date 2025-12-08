package com.comp2042.leaderboard;

import java.io.Serializable;
import java.time.LocalDate;

public class LeaderboardEntry implements Serializable, Comparable<LeaderboardEntry> {
    private final String name;
    private final int score;
    private final LocalDate date;

    public LeaderboardEntry(String name, int score) {
        this(name, score, LocalDate.now());
    }

    public LeaderboardEntry(String name, int score, LocalDate date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public LocalDate getDate() { return date; }

    @Override
    public int compareTo(LeaderboardEntry other) {

        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return name + ":" + score + ":" + date;
    }

}