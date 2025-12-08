package com.comp2042.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {

    @Test
    void testConstructorAndGetters() {
        int[][] newMatrix = {
            {0, 0, 0},
            {1, 0, 1},
            {1, 1, 1}
        };

        ClearRow clearRow = new ClearRow(2, newMatrix, 200);

        assertEquals(2, clearRow.getLinesRemoved());
        assertEquals(200, clearRow.getScoreBonus());
    }

    @Test
    void testGetNewMatrixReturnsCopy() {
        int[][] newMatrix = {
            {0, 0, 0},
            {1, 1, 1}
        };

        ClearRow clearRow = new ClearRow(1, newMatrix, 50);
        int[][] returnedMatrix = clearRow.getNewMatrix();

        returnedMatrix[0][0] = 99;

        assertNotSame(newMatrix, returnedMatrix);
        assertEquals(0, newMatrix[0][0]);
    }

    @Test
    void testZeroLinesRemoved() {
        int[][] newMatrix = {
            {0, 1, 0},
            {1, 0, 1}
        };

        ClearRow clearRow = new ClearRow(0, newMatrix, 0);

        assertEquals(0, clearRow.getLinesRemoved());
        assertEquals(0, clearRow.getScoreBonus());
    }

    @Test
    void testSingleLineRemoved() {
        int[][] newMatrix = {{0, 0, 0}};

        ClearRow clearRow = new ClearRow(1, newMatrix, 50);

        assertEquals(1, clearRow.getLinesRemoved());
        assertEquals(50, clearRow.getScoreBonus());
    }

    @Test
    void testTetrisFourLinesRemoved() {
        int[][] newMatrix = new int[20][10];

        ClearRow clearRow = new ClearRow(4, newMatrix, 800);

        assertEquals(4, clearRow.getLinesRemoved());
        assertEquals(800, clearRow.getScoreBonus());
    }

    @Test
    void testMatrixDimensionsPreserved() {
        int[][] newMatrix = new int[25][10];
        for (int y = 20; y < 25; y++) {
            for (int x = 0; x < 10; x++) {
                newMatrix[y][x] = 1;
            }
        }

        ClearRow clearRow = new ClearRow(2, newMatrix, 200);
        int[][] returnedMatrix = clearRow.getNewMatrix();

        assertEquals(25, returnedMatrix.length);
        assertEquals(10, returnedMatrix[0].length);
    }

    @Test
    void testMatrixContentPreserved() {
        int[][] newMatrix = {
            {0, 0, 0},
            {1, 2, 3},
            {4, 5, 6}
        };

        ClearRow clearRow = new ClearRow(1, newMatrix, 50);
        int[][] returnedMatrix = clearRow.getNewMatrix();

        assertArrayEquals(new int[]{1, 2, 3}, returnedMatrix[1]);
        assertArrayEquals(new int[]{4, 5, 6}, returnedMatrix[2]);
    }
}
