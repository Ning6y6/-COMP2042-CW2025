package com.comp2042.logic;

public final class Score {

    private static final int BASE_MULTIPLIER = 50;
    private int score = 0;

    public int getScore() {
        return score;
    }
    public void add(int value){
        this.score += value;
    }

    public int addLinesCleared(int lines) {
        if (lines > 0) {
            int points = BASE_MULTIPLIER * lines * lines;
            add(points);
            return points;
        }
        return 0;
    }

    public void reset() {
        this.score = 0;
    }
}