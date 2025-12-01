package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.event.ViewData;
import com.comp2042.logic.bricks.BrickType;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;


public class NextPieceView {

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
        if (brick == null) {
            throw new IllegalArgumentException("ViewData cannot be null");
        }
        
        nextBrickRectangles = new Rectangle[GameConstants.PREVIEW_COUNT][][];
        GridPane[] panels = {nextBrickPanel1, nextBrickPanel2, nextBrickPanel3};
        java.util.List<int[][]> nextThreeBricks = brick.getNextThreeBricksData();

        if (nextThreeBricks == null) {
            return;
        }

        for (int panelIndex = 0; panelIndex < GameConstants.PREVIEW_COUNT && panelIndex < nextThreeBricks.size(); panelIndex++) {
            int[][] brickData = nextThreeBricks.get(panelIndex);
            if (isValidBrickData(brickData)) {
                initializeBrickPanel(panelIndex, brickData, panels[panelIndex]);
            }
        }
    }

    private boolean isValidBrickData(int[][] brickData) {
        return brickData != null 
            && brickData.length > 0 
            && brickData[0] != null 
            && brickData[0].length > 0;
    }

    private void initializeBrickPanel(int panelIndex, int[][] brickData, GridPane panel) {
        nextBrickRectangles[panelIndex] = new Rectangle[brickData.length][brickData[0].length];
        for (int i = 0; i < brickData.length; i++) {
            if (brickData[i] == null) {
                continue;
            }
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE - 2, GameConstants.BRICK_SIZE - 2);
                rectangle.setFill(BrickType.getColorById(brickData[i][j]));
                rectangle.setArcHeight(7);
                rectangle.setArcWidth(7);
                nextBrickRectangles[panelIndex][i][j] = rectangle;
                panel.add(rectangle, j, i);
            }
        }
    }


    public void updateNextBrickPanels(ViewData brick) {
        if (brick == null) {
            return;
        }
        
        GridPane[] panels = {nextBrickPanel1, nextBrickPanel2, nextBrickPanel3};
        java.util.List<int[][]> nextThreeBricks = brick.getNextThreeBricksData();

        if (nextBrickRectangles == null) {
            nextBrickRectangles = new Rectangle[GameConstants.PREVIEW_COUNT][][];
        }

        if (nextThreeBricks == null) {
            return;
        }

        for (int panelIndex = 0; panelIndex < GameConstants.PREVIEW_COUNT && panelIndex < nextThreeBricks.size(); panelIndex++) {
            int[][] brickData = nextThreeBricks.get(panelIndex);
            if (isValidBrickData(brickData)) {
                updateBrickPanel(panelIndex, brickData, panels[panelIndex]);
            }
        }
    }

    private void updateBrickPanel(int panelIndex, int[][] brickData, GridPane panel) {
        panel.getChildren().clear();

        if (needsRectangleArrayRecreation(panelIndex, brickData)) {
            nextBrickRectangles[panelIndex] = new Rectangle[brickData.length][brickData[0].length];
        }

        if (nextBrickRectangles[panelIndex] == null) {
            nextBrickRectangles[panelIndex] = new Rectangle[brickData.length][brickData[0].length];
        }

        for (int i = 0; i < brickData.length; i++) {
            if (brickData[i] == null) {
                continue;
            }
            for (int j = 0; j < brickData[i].length; j++) {
                ensureRectangleExists(panelIndex, i, j);
                setRectangleData(brickData[i][j], nextBrickRectangles[panelIndex][i][j]);
                panel.add(nextBrickRectangles[panelIndex][i][j], j, i);
            }
        }
    }

    private boolean needsRectangleArrayRecreation(int panelIndex, int[][] brickData) {
        return nextBrickRectangles[panelIndex] == null
            || nextBrickRectangles[panelIndex].length != brickData.length
            || (nextBrickRectangles[panelIndex].length > 0 
                && nextBrickRectangles[panelIndex][0] != null
                && nextBrickRectangles[panelIndex][0].length != brickData[0].length);
    }

    private void ensureRectangleExists(int panelIndex, int i, int j) {
        if (nextBrickRectangles[panelIndex][i][j] == null) {
            Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE - 2, GameConstants.BRICK_SIZE - 2);
            rectangle.setArcHeight(7);
            rectangle.setArcWidth(7);
            nextBrickRectangles[panelIndex][i][j] = rectangle;
        }
    }


    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(BrickType.getColorById(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }
}

