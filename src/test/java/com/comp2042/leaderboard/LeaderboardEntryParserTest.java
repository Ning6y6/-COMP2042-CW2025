package com.comp2042.leaderboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LeaderboardEntryParserTest {

    private LeaderboardEntryParser parser;

    @BeforeEach
    void setUp() {
        parser = new LeaderboardEntryParser();
    }

    @Test
    void testParseValidLine() {
        String line = "Player1:1000:2025-01-15";

        LeaderboardEntry entry = parser.parse(line);

        assertNotNull(entry);
        assertEquals("Player1", entry.getName());
        assertEquals(1000, entry.getScore());
        assertEquals(LocalDate.of(2025, 1, 15), entry.getDate());
    }

    @Test
    void testParseNullLine() {
        LeaderboardEntry entry = parser.parse(null);

        assertNull(entry);
    }

    @Test
    void testParseEmptyLine() {
        LeaderboardEntry entry = parser.parse("");

        assertNull(entry);
    }

    @Test
    void testParseWhitespaceLine() {
        LeaderboardEntry entry = parser.parse("   ");

        assertNull(entry);
    }

    @Test
    void testParseMalformedLineTooFewParts() {
        LeaderboardEntry entry = parser.parse("Player1:1000");

        assertNull(entry);
    }

    @Test
    void testParseInvalidScore() {
        String line = "Player1:notanumber:2025-01-15";

        LeaderboardEntry entry = parser.parse(line);

        assertNull(entry);
    }

    @Test
    void testParseInvalidDate() {
        String line = "Player1:1000:not-a-date";

        LeaderboardEntry entry = parser.parse(line);

        assertNull(entry);
    }

    @Test
    void testParseInvalidDateFormat() {
        String line = "Player1:1000:15-01-2025";

        LeaderboardEntry entry = parser.parse(line);

        assertNull(entry);
    }

    @Test
    void testParseZeroScore() {
        String line = "Player1:0:2025-01-15";

        LeaderboardEntry entry = parser.parse(line);

        assertNotNull(entry);
        assertEquals(0, entry.getScore());
    }

    @Test
    void testParseNegativeScore() {
        String line = "Player1:-500:2025-01-15";

        LeaderboardEntry entry = parser.parse(line);

        assertNotNull(entry);
        assertEquals(-500, entry.getScore());
    }

    @Test
    void testParsePlayerNameWithSpaces() {
        String line = "Player One:1000:2025-01-15";

        LeaderboardEntry entry = parser.parse(line);

        assertNotNull(entry);
        assertEquals("Player One", entry.getName());
    }

    @Test
    void testParseExtraColonsInLine() {
        // When there are extra colons, the parser treats second part as score
        // "Player:Name:1000:2025-01-15" -> name="Player", score="Name" (invalid)
        String line = "Player:Name:1000:2025-01-15";

        LeaderboardEntry entry = parser.parse(line);

        // This returns null because "Name" cannot be parsed as an integer
        assertNull(entry);
    }

    @Test
    void testParseLargeScore() {
        String line = "TopPlayer:999999999:2025-06-01";

        LeaderboardEntry entry = parser.parse(line);

        assertNotNull(entry);
        assertEquals(999999999, entry.getScore());
    }
}
