package com.comp2042.ui;

import com.comp2042.logic.bricks.BrickType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class BrickColorMapper {
    public static Paint getColorFor(int brickTypeId) {
        BrickType type = BrickType.getById(brickTypeId);
        return switch (type) {
            case I -> Color.AQUA;
            case J -> Color.BLUEVIOLET;
            case L -> Color.DARKGREEN;
            case O -> Color.YELLOW;
            case S -> Color.RED;
            case T -> Color.BEIGE;
            case Z -> Color.BURLYWOOD;
            default -> Color.TRANSPARENT; // or White/Black
        };
    }
}