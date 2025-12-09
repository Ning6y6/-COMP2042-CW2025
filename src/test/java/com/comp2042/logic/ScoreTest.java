package com.comp2042.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void testInitialScoreIsZero() {
        assertEquals(0, score.getScore());
    }

    @Test
    void testAddIncrementsScore() {
        score.add(100);
        assertEquals(100, score.getScore());
        
        score.add(50);
        assertEquals(150, score.getScore());
    }

    @Test
    void testAddLinesClearedCalculatesCorrectPoints() {
        int points1 = score.addLinesCleared(1);
        assertEquals(50, points1);
        assertEquals(50, score.getScore());

        int points2 = score.addLinesCleared(2);
        assertEquals(200, points2);
        assertEquals(250, score.getScore());

        int points3 = score.addLinesCleared(3);
        assertEquals(450, points3);
        assertEquals(700, score.getScore());

        int points4 = score.addLinesCleared(4);
        assertEquals(800, points4);
        assertEquals(1500, score.getScore());
    }

    @Test
    void testAddLinesClearedWithZeroReturnsZero() {
        int points = score.addLinesCleared(0);
        assertEquals(0, points);
        assertEquals(0, score.getScore());
    }

    @Test
    void testAddLinesClearedWithNegativeReturnsZero() {
        score.add(100);
        int points = score.addLinesCleared(-5);
        assertEquals(0, points);
        assertEquals(100, score.getScore());
    }

    @Test
    void testResetSetsScoreToZero() {
        score.add(500);
        score.addLinesCleared(2);
        assertEquals(700, score.getScore());

        score.reset();
        assertEquals(0, score.getScore());
    }

    @Test
    void testScoreFormulaQuadratic() {
        score.reset();
        assertEquals(50 * 1 * 1, score.addLinesCleared(1));
        
        score.reset();
        assertEquals(50 * 2 * 2, score.addLinesCleared(2));
        
        score.reset();
        assertEquals(50 * 3 * 3, score.addLinesCleared(3));
        
        score.reset();
        assertEquals(50 * 4 * 4, score.addLinesCleared(4));
    }
}


