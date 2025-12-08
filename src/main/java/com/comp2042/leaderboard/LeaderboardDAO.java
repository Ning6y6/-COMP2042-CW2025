package com.comp2042.leaderboard;

import java.util.List;

/**
 * LeaderboardDAO provides data access operations for storing and loading game data.
 */
public interface LeaderboardDAO {
    /**
     * Performs the save score operation.
     *
     * @param entry the entry
     */
    void saveScore(LeaderboardEntry entry);
    /**
     * Performs the load scores operation.
     *
     * @return the result of the operation
     */
    List<LeaderboardEntry> loadScores();
}