package com.comp2042.leaderboard;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class LeaderboardEntryTest {

    @Test
    void testConstructorWithCurrentDate() {
        LeaderboardEntry entry = new LeaderboardEntry("Player1", 1000);
        
        assertEquals("Player1", entry.getName());
        assertEquals(1000, entry.getScore());
        assertNotNull(entry.getDate());
        assertEquals(LocalDate.now(), entry.getDate());
    }

    @Test
    void testConstructorWithSpecificDate() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        LeaderboardEntry entry = new LeaderboardEntry("Player1", 1000, date);
        
        assertEquals("Player1", entry.getName());
        assertEquals(1000, entry.getScore());
        assertEquals(date, entry.getDate());
    }

    @Test
    void testCompareToDescendingOrder() {
        LeaderboardEntry entry1 = new LeaderboardEntry("Player1", 1000);
        LeaderboardEntry entry2 = new LeaderboardEntry("Player2", 500);
        LeaderboardEntry entry3 = new LeaderboardEntry("Player3", 1500);
        
        assertTrue(entry3.compareTo(entry1) < 0);
        assertTrue(entry1.compareTo(entry2) < 0);
        assertTrue(entry2.compareTo(entry3) > 0);
    }

    @Test
    void testCompareToEqualScores() {
        LeaderboardEntry entry1 = new LeaderboardEntry("Player1", 1000);
        LeaderboardEntry entry2 = new LeaderboardEntry("Player2", 1000);
        
        assertEquals(0, entry1.compareTo(entry2));
        assertEquals(0, entry2.compareTo(entry1));
    }

    @Test
    void testToStringFormat() {
        LocalDate date = LocalDate.of(2025, 1, 15);
        LeaderboardEntry entry = new LeaderboardEntry("Player1", 1000, date);
        
        String result = entry.toString();
        assertEquals("Player1:1000:2025-01-15", result);
    }

    @Test
    void testSortingInCollection() {
        java.util.List<LeaderboardEntry> entries = new java.util.ArrayList<>();
        entries.add(new LeaderboardEntry("A", 300));
        entries.add(new LeaderboardEntry("B", 100));
        entries.add(new LeaderboardEntry("C", 500));
        entries.add(new LeaderboardEntry("D", 200));
        
        java.util.Collections.sort(entries);
        
        assertEquals(500, entries.get(0).getScore());
        assertEquals(300, entries.get(1).getScore());
        assertEquals(200, entries.get(2).getScore());
        assertEquals(100, entries.get(3).getScore());
    }
}

