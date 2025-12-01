package com.comp2042.leaderboard;

import java.util.Collections;
import java.util.List;

public class LeaderboardService {
    private final LeaderboardDAO dao;
    private static final int MAX_ENTRIES = 10;

    // Dependency Injection via constructor (DIP)
    public LeaderboardService(LeaderboardDAO dao) {
        this.dao = dao;
    }

    public void addScore(String name, int score) {
        dao.saveScore(new LeaderboardEntry(name, score));
    }

    public List<LeaderboardEntry> getTopScores() {
        List<LeaderboardEntry> allScores = dao.loadScores();
        Collections.sort(allScores);
        // Limit to top 10
        if (allScores.size() > MAX_ENTRIES) {
            return allScores.subList(0, MAX_ENTRIES);
        }
        return allScores;
    }
}
