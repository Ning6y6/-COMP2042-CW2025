package com.comp2042.ui;

import com.comp2042.event.ViewData;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class NextPieceView {
    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane nextBrickPanel1;

    @FXML
    private GridPane nextBrickPanel2;

    @FXML
    private GridPane nextBrickPanel3;

    private Rectangle[][][] nextBrickRectangles;

    // Must be called from GameView to initialize the FXML fields
    public void initialize(GridPane panel1, GridPane panel2, GridPane panel3) {
        this.nextBrickPanel1 = panel1;
        this.nextBrickPanel2 = panel2;
        this.nextBrickPanel3 = panel3;
    }

    public void initNextBrickPanels(ViewData brick) {
        nextBrickRectangles = new Rectangle[3][][];
        GridPane[] panels = {nextBrickPanel1, nextBrickPanel2, nextBrickPanel3};
        java.util.List<int[][]> nextThreeBricks = brick.getNextThreeBricksData();

        for (int panelIndex = 0; panelIndex < 3 && panelIndex < nextThreeBricks.size(); panelIndex++) {
            int[][] brickData = nextThreeBricks.get(panelIndex);
            if (brickData != null && brickData.length > 0) {
                nextBrickRectangles[panelIndex] = new Rectangle[brickData.length][brickData[0].length];
                for (int i = 0; i < brickData.length; i++) {
                    for (int j = 0; j < brickData[i].length; j++) {
                        Rectangle rectangle = new Rectangle(BRICK_SIZE - 2, BRICK_SIZE - 2);
                        rectangle.setFill(getFillColor(brickData[i][j]));
                        rectangle.setArcHeight(7);
                        rectangle.setArcWidth(7);
                        nextBrickRectangles[panelIndex][i][j] = rectangle;
                        panels[panelIndex].add(rectangle, j, i);
                    }
                }
            }
        }
    }

    public void updateNextBrickPanels(ViewData brick) {
        GridPane[] panels = {nextBrickPanel1, nextBrickPanel2, nextBrickPanel3};
        java.util.List<int[][]> nextThreeBricks = brick.getNextThreeBricksData();

        if (nextBrickRectangles == null) {
            nextBrickRectangles = new Rectangle[3][][];
        }

        for (int panelIndex = 0; panelIndex < 3 && panelIndex < nextThreeBricks.size(); panelIndex++) {
            int[][] brickData = nextThreeBricks.get(panelIndex);
            if (brickData != null && brickData.length > 0) {

                panels[panelIndex].getChildren().clear();

                if (nextBrickRectangles[panelIndex] == null ||
                        nextBrickRectangles[panelIndex].length != brickData.length ||
                        nextBrickRectangles[panelIndex][0].length != brickData[0].length) {

                    nextBrickRectangles[panelIndex] = new Rectangle[brickData.length][brickData[0].length];
                }

                for (int i = 0; i < brickData.length; i++) {
                    for (int j = 0; j < brickData[i].length; j++) {
                        if (nextBrickRectangles[panelIndex][i][j] == null) {
                            Rectangle rectangle = new Rectangle(BRICK_SIZE - 2, BRICK_SIZE - 2);
                            rectangle.setArcHeight(7);
                            rectangle.setArcWidth(7);
                            nextBrickRectangles[panelIndex][i][j] = rectangle;
                        }
                        setRectangleData(brickData[i][j], nextBrickRectangles[panelIndex][i][j]);
                        panels[panelIndex].add(nextBrickRectangles[panelIndex][i][j], j, i);
                    }
                }
            }
        }
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }

    public void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }
}

