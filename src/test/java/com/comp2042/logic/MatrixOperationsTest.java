package com.comp2042.logic;

import com.comp2042.event.ClearRow;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testCopyCreatesDeepCopy() {
        int[][] original = {
            {1, 2, 3},
            {4, 5, 6}
        };
        
        int[][] copy = MatrixOperations.copy(original);
        
        assertNotSame(original, copy);
        assertArrayEquals(original, copy);
        
        copy[0][0] = 99;
        assertEquals(1, original[0][0]);
    }

    @Test
    void testCopyThrowsExceptionOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            MatrixOperations.copy(null);
        });
    }

    @Test
    void testCopyEmptyMatrix() {
        int[][] empty = new int[0][];
        int[][] copy = MatrixOperations.copy(empty);
        assertArrayEquals(empty, copy);
    }

    @Test
    void testCopyWithNullRows() {
        int[][] original = {
            {1, 2},
            null,
            {3, 4}
        };
        
        int[][] copy = MatrixOperations.copy(original);
        assertNull(copy[1]);
        assertArrayEquals(new int[]{1, 2}, copy[0]);
        assertArrayEquals(new int[]{3, 4}, copy[2]);
    }

    @Test
    void testIntersectDetectsCollision() {
        int[][] board = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        
        int[][] brick = {
            {1, 1}
        };
        
        assertTrue(MatrixOperations.intersect(board, brick, 1, 1));
    }

    @Test
    void testIntersectNoCollision() {
        int[][] board = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        
        int[][] brick = {
            {1, 1}
        };
        
        assertFalse(MatrixOperations.intersect(board, brick, 0, 0));
    }

    @Test
    void testIntersectOutOfBounds() {
        int[][] board = {
            {0, 0},
            {0, 0}
        };
        
        int[][] brick = {
            {1, 1}
        };
        
        assertTrue(MatrixOperations.intersect(board, brick, -1, 0));
        assertTrue(MatrixOperations.intersect(board, brick, 1, 0));
        assertTrue(MatrixOperations.intersect(board, brick, 0, -1));
        assertTrue(MatrixOperations.intersect(board, brick, 0, 2));
    }

    @Test
    void testIntersectThrowsExceptionOnNullMatrix() {
        int[][] brick = {{1}};
        assertThrows(IllegalArgumentException.class, () -> {
            MatrixOperations.intersect(null, brick, 0, 0);
        });
    }

    @Test
    void testIntersectThrowsExceptionOnNullBrick() {
        int[][] board = {{0}};
        assertThrows(IllegalArgumentException.class, () -> {
            MatrixOperations.intersect(board, null, 0, 0);
        });
    }

    @Test
    void testMergeCombinesBrickIntoBoard() {
        int[][] board = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        
        int[][] brick = {
            {1, 2}
        };
        
        int[][] result = MatrixOperations.merge(board, brick, 1, 1);
        
        assertEquals(0, result[0][1]);
        assertEquals(1, result[1][1]);
        assertEquals(2, result[1][2]);
        assertEquals(0, result[2][1]);
    }

    @Test
    void testMergeDoesNotModifyOriginal() {
        int[][] board = {
            {0, 0},
            {0, 0}
        };
        
        int[][] brick = {{1}};
        int originalFirstValue = board[0][0];
        
        MatrixOperations.merge(board, brick, 0, 0);
        
        assertEquals(0, board[0][0]);
        assertEquals(originalFirstValue, board[0][0]);
    }

    @Test
    void testCheckRemovingIdentifiesCompleteRows() {
        int[][] board = {
            {0, 0, 0},
            {1, 1, 1},
            {0, 1, 0},
            {1, 1, 1}
        };
        
        ClearRow result = MatrixOperations.checkRemoving(board);
        
        assertEquals(2, result.getLinesRemoved());
        int[][] newMatrix = result.getNewMatrix();
        assertEquals(board.length, newMatrix.length);
    }

    @Test
    void testCheckRemovingNoCompleteRows() {
        int[][] board = {
            {0, 0, 0},
            {0, 1, 0},
            {1, 0, 1}
        };
        
        ClearRow result = MatrixOperations.checkRemoving(board);
        
        assertEquals(0, result.getLinesRemoved());
    }

    @Test
    void testCheckRemovingAllRowsComplete() {
        int[][] board = {
            {1, 1},
            {1, 1}
        };
        
        ClearRow result = MatrixOperations.checkRemoving(board);
        
        assertEquals(2, result.getLinesRemoved());
    }

    @Test
    void testCheckRemovingEmptyMatrix() {
        int[][] board = new int[0][];
        
        ClearRow result = MatrixOperations.checkRemoving(board);
        
        assertEquals(0, result.getLinesRemoved());
    }

    @Test
    void testDeepCopyList() {
        int[][] matrix1 = {{1, 2}};
        int[][] matrix2 = {{3, 4}};
        java.util.List<int[][]> original = new java.util.ArrayList<>();
        original.add(matrix1);
        original.add(matrix2);
        
        java.util.List<int[][]> copy = MatrixOperations.deepCopyList(original);
        
        assertEquals(2, copy.size());
        assertNotSame(matrix1, copy.get(0));
        assertArrayEquals(matrix1, copy.get(0));
        
        copy.get(0)[0][0] = 99;
        assertEquals(1, matrix1[0][0]);
    }

    @Test
    void testDeepCopyListThrowsExceptionOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            MatrixOperations.deepCopyList(null);
        });
    }
}

