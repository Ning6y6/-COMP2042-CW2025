package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.logic.bricks.BrickType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HoldView {
    private GridPane holdPane;

    public void initialize(GridPane holdPane) {
        this.holdPane = holdPane;
    }

    public void updateHoldPanel(int[][] brickData, boolean allowed) {
        holdPane.getChildren().clear();
        if(brickData == null)return;

        for (int r = 0; r < brickData.length; r++) {
            for (int c = 0; c < brickData[r].length; c++) {
                if (brickData[r][c] != 0) {
                    Rectangle rect = new Rectangle(15, 15);
                    String cssClass = BrickType.getById(brickData[r][c]).getCssClass();
                    rect.getStyleClass().addAll("cell", cssClass);

                    if (!allowed) {
                        rect.getStyleClass().add("dimmed");
                    }
                    holdPane.add(rect, c, r);
                }
            }
        }
    }
}