package com.comp2042.logic;

public final class Score {

    private static final int BASE_MULTIPLIER = 50;
    private int score = 0;

    /**
     * Returns the score.
     *
     * @return the result of the operation
     */
    public int getScore() {
        return score;
    }
    /**
     * Performs the add operation.
     *
     * @param value the value
     */
    public void add(int value){
        this.score += value;
    }

    /**
     * Performs the add lines cleared operation.
     *
     * @param lines the lines
     * @return the result of the operation
     */
    public int addLinesCleared(int lines) {
        if (lines > 0) {
            int points = BASE_MULTIPLIER * lines * lines;
            add(points);
            return points;
        }
        return 0;
    }

    /**
     * Performs the reset operation.
     */
    public void reset() {
        this.score = 0;
    }
}