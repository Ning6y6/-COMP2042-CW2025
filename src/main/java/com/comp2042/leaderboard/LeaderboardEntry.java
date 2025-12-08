package com.comp2042.leaderboard;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * LeaderboardEntry is part of the leaderboard package and encapsulates behaviour related to leaderboard entry.
 */
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

    /**
     * Returns the name.
     *
     * @return the result of the operation
     */
    public String getName() { return name; }
    /**
     * Returns the score.
     *
     * @return the result of the operation
     */
    public int getScore() { return score; }
    /**
     * Returns the date.
     *
     * @return the result of the operation
     */
    public LocalDate getDate() { return date; }

    @Override
    /**
     * Performs the compare to operation.
     *
     * @param other the other
     * @return the result of the operation
     */
    public int compareTo(LeaderboardEntry other) {

        return Integer.compare(other.score, this.score);
    }

    @Override
    /**
     * Performs the to string operation.
     *
     * @return the result of the operation
     */
    public String toString() {
        return name + ":" + score + ":" + date;
    }

}