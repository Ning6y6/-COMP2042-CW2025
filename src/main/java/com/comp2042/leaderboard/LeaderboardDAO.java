package com.comp2042.leaderboard;

import java.util.List;

public interface LeaderboardDAO {
    void saveScore(LeaderboardEntry entry);
    List<LeaderboardEntry> loadScores();
}