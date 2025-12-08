package com.comp2042.logic;

import com.comp2042.event.ClearRow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

public class MatrixOperations {

    private MatrixOperations(){
        throw new AssertionError("MatrixOperations is a utility class and cannot be instantiated");
    }

    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) {
        validateMatrix(matrix, "matrix");
        validateMatrix(brick, "brick");

        for (int row = 0; row < brick.length; row++) {
            if (brick[row] == null) {
                continue;
            }
            for (int col = 0; col < brick[row].length; col++) {
                int targetX = x + col;
                int targetY = y + row;
                if (brick[row][col] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) {
        if (targetY < 0 || targetY >= matrix.length) {
            return true;
        }
        if (targetY >= 0 && targetY < matrix.length && matrix[targetY] != null) {
            return targetX < 0 || targetX >= matrix[targetY].length;
        }
        return true;
    }

    public static int[][] copy(int[][] original) {
        if (original == null) {
            throw new IllegalArgumentException("Cannot copy null matrix");
        }
        if (original.length == 0) {
            return new int[0][];
        }

        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            if (original[i] == null) {
                myInt[i] = null;
            } else {
                int aLength = original[i].length;
                myInt[i] = new int[aLength];
                System.arraycopy(original[i], 0, myInt[i], 0, aLength);
            }
        }
        return myInt;
    }

    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) {
        validateMatrix(filledFields, "filledFields");
        validateMatrix(brick, "brick");

        int[][] copy = copy(filledFields);
        for (int row = 0; row < brick.length; row++) {
            if (brick[row] == null) {
                continue;
            }
            for (int col = 0; col < brick[row].length; col++) {
                int targetX = x + col;
                int targetY = y + row;
                if (brick[row][col] != 0) {
                    if (!checkOutOfBound(copy, targetX, targetY)) {
                        copy[targetY][targetX] = brick[row][col];
                    }
                }
            }
        }
        return copy;
    }

    public static ClearRow checkRemoving(final int[][] matrix) {
        validateMatrix(matrix, "matrix");
        if (matrix.length == 0) {
            return new ClearRow(0, new int[0][], 0);
        }

        int maxRowLength = findMaxRowLength(matrix);
        if (maxRowLength == 0) {
            return new ClearRow(0, copy(matrix), 0);
        }

        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] == null) {
                continue;
            }
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = isRowComplete(matrix[i]);

            System.arraycopy(matrix[i], 0, tmpRow, 0, matrix[i].length);

            if (rowToClear) {
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow);
            }
        }

        int[][] tmp = buildNewMatrix(matrix.length, maxRowLength, newRows);

        return new ClearRow(clearedRows.size(), tmp, 0);
    }

    private static int findMaxRowLength(int[][] matrix) {
        int maxLength = 0;
        for (int[] row : matrix) {
            if (row != null && row.length > maxLength) {
                maxLength = row.length;
            }
        }
        return maxLength;
    }

    private static boolean isRowComplete(int[] row) {
        if (row == null || row.length == 0) {
            return false;
        }
        for (int cell : row) {
            if (cell == 0) {
                return false;
            }
        }
        return true;
    }

    private static int[][] buildNewMatrix(int originalHeight, int rowLength, Deque<int[]> newRows) {
        int[][] tmp = new int[originalHeight][rowLength];
        for (int i = originalHeight - 1; i >= 0; i--) {
            int[] row = newRows.pollLast();
            if (row != null) {

                if (row.length != rowLength) {
                    int[] adjustedRow = new int[rowLength];
                    System.arraycopy(row, 0, adjustedRow, 0, Math.min(row.length, rowLength));
                    tmp[i] = adjustedRow;
                } else {
                    tmp[i] = row;
                }
            } else {

                tmp[i] = new int[rowLength];
            }
        }
        return tmp;
    }

    private static void validateMatrix(int[][] matrix, String paramName) {
        if (matrix == null) {
            throw new IllegalArgumentException(paramName + " cannot be null");
        }
    }

    public static List<int[][]> deepCopyList(List<int[][]> list){
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null");
        }
        return list.stream()
                .map(MatrixOperations::copy)
                .collect(Collectors.toList());
    }
}