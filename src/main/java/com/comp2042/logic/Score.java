package com.comp2042.logic;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score {

    private static final int BASE_MULTIPLIER = 50;
    private final IntegerProperty score = new SimpleIntegerProperty(0);

    public IntegerProperty scoreProperty() {
        return score;
    }

    public void add(int value){
        score.set(score.get() + value);
    }

    public void addLinesCleared(int lines) {
        if (lines > 0) {
            int points = BASE_MULTIPLIER * lines * lines;
            add(points);
        }
    }

    public void reset() {
        score.setValue(0);
    }
}
