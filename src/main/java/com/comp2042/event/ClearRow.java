package com.comp2042.event;

import com.comp2042.logic.MatrixOperations;

public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Returns the lines removed.
     *
     * @return the result of the operation
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns the new matrix.
     *
     * @return the result of the operation
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Returns the score bonus.
     *
     * @return the result of the operation
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}