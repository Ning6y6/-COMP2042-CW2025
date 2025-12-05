package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.event.ViewData;
import com.comp2042.logic.bricks.BrickType;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HoldView {

    @FXML
    private GridPane holdPane;

    private Rectangle[][] holdRectangles;

    public void initialize(GridPane holdPane) {
        this.holdPane = holdPane;
    }

    public void updateHoldPanel(ViewData data) {
        holdPane.getChildren().clear();

        int[][] brickData = data.getHeldBrickData();
        if (brickData == null) return;

        holdRectangles = new Rectangle[brickData.length][brickData[0].length];

        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                if (brickData[i][j] != 0) {
                    Rectangle rect = new Rectangle(GameConstants.BRICK_SIZE - 2, GameConstants.BRICK_SIZE - 2);

                    Color baseColor = (Color) BrickType.getColorById(brickData[i][j]);
                    if (!data.isHoldAllowed()) {
                        baseColor = baseColor.darker().darker();
                    }

                    rect.setFill(baseColor);
                    rect.setArcHeight(7);
                    rect.setArcWidth(7);
                    holdPane.add(rect, j, i);
                }
            }
        }
    }
}