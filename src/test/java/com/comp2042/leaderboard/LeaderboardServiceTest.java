package com.comp2042.leaderboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LeaderboardServiceTest {

    private LeaderboardService service;
    private MockLeaderboardDAO mockDAO;

    @BeforeEach
    void setUp() {
        mockDAO = new MockLeaderboardDAO();
        service = new LeaderboardService(mockDAO);
    }

    @Test
    void testAddScore() {
        service.addScore("Player1", 1000);
        
        assertEquals(1, mockDAO.savedScores.size());
        assertEquals("Player1", mockDAO.savedScores.get(0).getName());
        assertEquals(1000, mockDAO.savedScores.get(0).getScore());
    }

    @Test
    void testGetTopScoresReturnsSortedDescending() {
        mockDAO.savedScores.add(new LeaderboardEntry("Player1", 500));
        mockDAO.savedScores.add(new LeaderboardEntry("Player2", 1000));
        mockDAO.savedScores.add(new LeaderboardEntry("Player3", 300));
        mockDAO.savedScores.add(new LeaderboardEntry("Player4", 800));
        
        List<LeaderboardEntry> topScores = service.getTopScores();
        
        assertEquals(4, topScores.size());
        assertEquals(1000, topScores.get(0).getScore());
        assertEquals(800, topScores.get(1).getScore());
        assertEquals(500, topScores.get(2).getScore());
        assertEquals(300, topScores.get(3).getScore());
    }

    @Test
    void testGetTopScoresLimitsToTen() {
        for (int i = 0; i < 15; i++) {
            mockDAO.savedScores.add(new LeaderboardEntry("Player" + i, 1000 - i));
        }
        
        List<LeaderboardEntry> topScores = service.getTopScores();
        
        assertEquals(10, topScores.size());
        assertEquals(1000, topScores.get(0).getScore());
        assertEquals(991, topScores.get(9).getScore());
    }

    @Test
    void testGetTopScoresEmptyList() {
        List<LeaderboardEntry> topScores = service.getTopScores();
        
        assertTrue(topScores.isEmpty());
    }

    @Test
    void testGetTopScoresExactTen() {
        for (int i = 0; i < 10; i++) {
            mockDAO.savedScores.add(new LeaderboardEntry("Player" + i, 1000 - i));
        }
        
        List<LeaderboardEntry> topScores = service.getTopScores();
        
        assertEquals(10, topScores.size());
    }

    private static class MockLeaderboardDAO implements LeaderboardDAO {
        List<LeaderboardEntry> savedScores = new ArrayList<>();

        @Override
        public void saveScore(LeaderboardEntry entry) {
            savedScores.add(entry);
        }

        @Override
        public List<LeaderboardEntry> loadScores() {
            return new ArrayList<>(savedScores);
        }
    }
}


