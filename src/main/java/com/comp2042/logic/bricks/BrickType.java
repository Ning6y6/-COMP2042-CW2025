package com.comp2042.logic.bricks;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum BrickType {
    EMPTY(0, Color.TRANSPARENT),
    I(1, Color.AQUA),
    J(2, Color.BLUEVIOLET),
    L(3, Color.DARKGREEN),
    O(4, Color.YELLOW),
    S(5, Color.RED),
    T(6, Color.BEIGE),
    Z(7, Color.BURLYWOOD);

    private final int id;
    private final Color color;

    BrickType(int id, Color color) {
        this.id = id;
        this.color = color;
    }

    public static Paint getColorById(int id) {
        for (BrickType type : values()) {
            if (type.id == id) {
                return type.color;
            }
        }
        return Color.WHITE; // Default fallback
    }
}