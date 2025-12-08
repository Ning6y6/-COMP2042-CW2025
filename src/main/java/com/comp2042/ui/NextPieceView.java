package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.logic.bricks.BrickType;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import java.util.List;

public class NextPieceView {

    private GridPane[] panels;

    public void initialize(GridPane panel1, GridPane panel2, GridPane panel3) {
        this.panels = new GridPane[]{panel1, panel2, panel3};
    }

    public void updateNextBrickPanels(List<int[][]> nextPieces) {
        if (nextPieces == null) return;

        for (int i = 0; i < panels.length && i < nextPieces.size(); i++) {
            renderPanel(panels[i], nextPieces.get(i));
        }
    }

    private void renderPanel(GridPane panel, int[][] data) {
        panel.getChildren().clear();
        for (int r = 0; r < data.length; r++) {
            for (int c = 0; c < data[0].length; c++) {
                if (data[r][c] != 0) {
                    Rectangle rect = new Rectangle(15, 15);
                    rect.getStyleClass().addAll("cell", BrickType.getById(data[r][c]).getCssClass());
                    panel.add(rect, c, r);
                }
            }
        }
    }
}
