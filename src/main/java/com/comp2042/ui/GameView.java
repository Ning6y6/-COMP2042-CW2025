package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.GameController;
import com.comp2042.event.*;
import com.comp2042.input.InputManager;
import com.comp2042.logic.bricks.BrickType;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable {

    private static final double STACK_OFFSET = 35 ;


    @FXML
    private GridPane gamePanel;

    @FXML
    private Label scoreLabel;

    @FXML
    private Button settingsButton;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private GridPane nextBrickPanel1;

    @FXML
    private GridPane nextBrickPanel2;

    @FXML
    private GridPane nextBrickPanel3;

    @FXML
    private GridPane holdPane;

    private Rectangle[][] backgroundGrid;
    private Rectangle[][] fallingBrickGrid;
    private Rectangle[][] ghostBrickGrid;

    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);

    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private GameInputHandler eventListener;

    private InputManager inputManager;

    private NextPieceView nextPieceView;

    private SettingsView activeSettingsView;

    private HoldView holdView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadResource();
        setupUIComponent();
        setupInputHandling();
        setupFocusManagement();
    }

    private void setupFocusManagement() {
        gamePanel.setFocusTraversable(true);
        requestFocus();
    }

    private void setupUIComponent() {
        if(groupNotification != null)groupNotification.setVisible(false);
        if(gameOverPanel != null) gameOverPanel.setVisible(false);

        nextPieceView = new NextPieceView();
        nextPieceView.initialize(nextBrickPanel1, nextBrickPanel2, nextBrickPanel3);

        holdView = new HoldView();
        holdView.initialize(holdPane);

    }

    private void setupInputHandling() {
        if(gamePanel != null) {
            gamePanel.setOnKeyPressed(this::delegateInputToController);
        }
    }


    private void loadResource() {
        URL fontUrl = getClass().getClassLoader().getResource("digital.ttf");
        if (fontUrl != null) {
            Font.loadFont(fontUrl.toExternalForm(), 38);
        } else {
            System.err.println("CRITICAL ERROR: The font 'digital.ttf' was not found.");
        }
    }

    public void initGameView(int[][] boardMatrix, ViewData initialBrick) {
        validateBoardMatrix(boardMatrix);

        gamePanel.getColumnConstraints().clear();
        gamePanel.getRowConstraints().clear();
        brickPanel.getColumnConstraints().clear();
        brickPanel.getRowConstraints().clear();

        for (int i = 0; i < GameConstants.BOARD_WIDTH; i++) {
            ColumnConstraints colConst = new ColumnConstraints(GameConstants.BRICK_SIZE);
            gamePanel.getColumnConstraints().add(colConst);
            brickPanel.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < GameConstants.VISIBLE_ROWS; i++) {
            RowConstraints rowConst = new RowConstraints(GameConstants.BRICK_SIZE);
            gamePanel.getRowConstraints().add(rowConst);
            brickPanel.getRowConstraints().add(rowConst);
        }

        double totalWidth = GameConstants.BOARD_WIDTH * GameConstants.BRICK_SIZE + (GameConstants.BOARD_WIDTH - 1); // + gaps
        double totalHeight = GameConstants.VISIBLE_ROWS * GameConstants.BRICK_SIZE + (GameConstants.VISIBLE_ROWS - 1);

        gamePanel.setPrefSize(totalWidth, totalHeight);
        gamePanel.setMaxSize(totalWidth, totalHeight);
        brickPanel.setPrefSize(totalWidth, totalHeight);
        brickPanel.setMaxSize(totalWidth, totalHeight);

        initBackgroundGrid();
        initDynamicBrickLayers();

        refreshBoard(boardMatrix);
        updateNextPieces(initialBrick);
        updateHoldPiece(initialBrick);

        System.out.printf("GameView Init: Cols=%d, VisibleRows=%d, BrickSize=%d%n",
                GameConstants.BOARD_WIDTH, GameConstants.VISIBLE_ROWS, GameConstants.BRICK_SIZE);
    }

    public void updateFallingBrick(ViewData brickData) {
        if (isPaused.get()) {
            return;
        }

        clearDynamicLayer();
        renderGhostBrick(brickData);
        renderFallingBrick(brickData);
        updateNextPieces(brickData);
    }

    public void updateHoldPiece(ViewData holdData) {
        if (holdView != null) {
            holdView.updateHoldPanel(holdData);
        }
    }

    public void updateNextPieces(ViewData nextData) {
        if (nextPieceView != null) {
            nextPieceView.updateNextBrickPanels(nextData);
        }
    }

    public void refreshBoard(int[][] boardMatrix) {
        validateBoardMatrix(boardMatrix);

        for (int row = GameConstants.HIDDEN_ROWS; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {
                int colorId = boardMatrix[row][col];
                renderCell(backgroundGrid[toUIRow(row)][col], colorId);
            }
        }
    }

    private int toUIRow(int boardY) {
        return boardY - GameConstants.HIDDEN_ROWS;
    }
    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(integerProperty.asString());
        }
    }

    public void showGameOver() {

        if(groupNotification !=null){
            groupNotification.setVisible(true);
            groupNotification.toFront();
        }
        if(gameOverPanel != null){
            gameOverPanel.setVisible(true);
            gameOverPanel.toFront();
        }
        isGameOver.set(true);
    }
    
    public void showLinesClearedNotification(int scoreBonus) {
        if (groupNotification == null)return;

        NotificationPanel notification = new NotificationPanel("+" + scoreBonus);
        stackNotifications(notification);
        groupNotification.getChildren().add(notification);
        groupNotification.setVisible(true);
        groupNotification.toFront();

    }
    private void stackNotifications(NotificationPanel newNotification) {


        for(var node : groupNotification.getChildren()) {
            TranslateTransition shift = new TranslateTransition(Duration.millis(250), node);
            shift.setByY(-STACK_OFFSET);
            shift.play();
        }

        newNotification.setTranslateX(0);
        newNotification.setTranslateY(0);
    }

    public void setInputManager(InputManager inputManager) {

        this.inputManager = inputManager;
    }

    public void setEventListener(GameInputHandler eventListener) {//

        this.eventListener = eventListener;
    }

    private void delegateInputToController(KeyEvent keyEvent) {
        if (inputManager != null && eventListener instanceof GameController controller) {
            inputManager.handleInput(keyEvent.getCode(),controller);
            keyEvent.consume();
        }
    }

    private void notifyControllerOfTimerTick() {
        if (isPaused.get() || eventListener == null) {
            return;
        }

        MoveEvent moveEvent = new MoveEvent(EventType.DOWN, EventSource.THREAD);
        DownData downData = eventListener.onDownEvent(moveEvent);

        handleDownResult(downData);
        requestFocus();
    }

    private void handleDownResult(DownData downData) {
        if (hasLinesClear(downData)) {
            int scoreBonus = downData.getClearRow().getScoreBonus();
            showLinesClearedNotification(scoreBonus);
        }

        updateFallingBrick(downData.getViewData());
    }

    private boolean hasLinesClear(DownData downData) {
        return downData.getClearRow() != null &&
                downData.getClearRow().getLinesRemoved() > 0;
    }

    private void initBackgroundGrid() {
        gamePanel.getChildren().clear();
        int cols = GameConstants.BOARD_WIDTH;
        int visibleRows = GameConstants.VISIBLE_ROWS;
        backgroundGrid = new Rectangle[visibleRows][cols];

        for (int row = 0; row < visibleRows; row++) {
            for (int col = 0; col < cols; col++) {
                Rectangle cell = createCell();
                backgroundGrid[row][col] = cell;
                gamePanel.add(cell, col, row );

            }
        }
    }

    private void initDynamicBrickLayers() {
        brickPanel.getChildren().clear();
        int cols = GameConstants.BOARD_WIDTH;
        int visibleRows = GameConstants.VISIBLE_ROWS;

        fallingBrickGrid = new Rectangle[visibleRows][cols];
        ghostBrickGrid = new Rectangle[visibleRows][cols];

        for (int row = 0; row < visibleRows; row++) {
            for (int col = 0; col < cols; col++) {

                Rectangle ghostCell = createCell();
                ghostCell.setVisible(false);
                ghostCell.setMouseTransparent(true);
                ghostCell.setStrokeType(StrokeType.INSIDE);
                ghostBrickGrid[row][col] = ghostCell;
                
                Rectangle brickCell = createCell();
                brickCell.setVisible(false);
                brickCell.setMouseTransparent(true);
                brickCell.setStrokeType(StrokeType.INSIDE);
                fallingBrickGrid[row][col] = brickCell;

                brickPanel.add(ghostCell, col, row );
                brickPanel.add(brickCell, col, row );

            }
        }
    }

    private Rectangle createCell() {
        Rectangle cell = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
        cell.setFill(Color.TRANSPARENT);
        return cell;
    }

    private void clearDynamicLayer() {
        for (int row = 0; row < fallingBrickGrid.length; row++) {
            for (int col = 0; col < fallingBrickGrid[row].length; col++) {
                if (fallingBrickGrid[row][col] != null) fallingBrickGrid[row][col].setVisible(false);
                if (ghostBrickGrid[row][col] != null) ghostBrickGrid[row][col].setVisible(false);
            }
        }
    }

    private void renderGhostBrick(ViewData brickData) {
        if (brickData == null || brickData.getBrickData() == null) {
            return;
        }
        int[][] brickMatrix = brickData.getBrickData();
        int xPos = brickData.getXPosition();
        int yPos = brickData.getGhostYPosition();

        for (int row = 0; row < brickMatrix.length; row++) {
            for (int col = 0; col < brickMatrix[row].length; col++) {
                int colorId = brickMatrix[row][col];
                if (colorId != 0) {
                    renderGhostCell(xPos + col, yPos + row, colorId);
                }
            }
        }
    }

    private void renderGhostCell(int gridX, int gridY, int colorId) {
        int uiRow = toUIRow(gridY);
        if (uiRow < 0 || uiRow >= ghostBrickGrid.length ) return;
        if (gridX < 0 || gridX >= ghostBrickGrid[0].length) return;

        Rectangle cell = ghostBrickGrid[uiRow][gridX];

        Paint basePaint = BrickType.getColorById(colorId);
        if (basePaint instanceof Color) {
            Color c = (Color) basePaint;
            cell.setFill(Color.rgb((int)(c.getRed()*255), (int)(c.getGreen()*255), (int)(c.getBlue()*255), 0.15));
            cell.setStroke(c);
            cell.setStrokeWidth(1.5);

        } else {
            cell.setFill(Color.TRANSPARENT);
            cell.setStroke(Color.WHITE);
        }

        cell.setArcWidth(9);
        cell.setArcHeight(9);
        cell.setVisible(true);
    }

    private void renderFallingBrick(ViewData brickData) {
        if (brickData == null || brickData.getBrickData() == null) {
            return;
        }

        int[][] brickMatrix = brickData.getBrickData();
        int xPos = brickData.getXPosition();
        int yPos = brickData.getYPosition();

        for (int row = 0; row < brickMatrix.length; row++) {
            for (int col = 0; col < brickMatrix[row].length; col++) {
                int colorId = brickMatrix[row][col];

                if (colorId != 0) {
                    renderBrickCell(xPos + col, yPos + row, colorId);
                }
            }
        }
    }

    private void renderBrickCell(int gridX, int gridY, int colorId) {
        int uiRow = toUIRow(gridY);

        if (uiRow < 0 || uiRow >= fallingBrickGrid.length) {
            return;
        }

        if (gridX < 0 || gridX >= fallingBrickGrid[0].length) {
            return;
        }

        Rectangle cell = fallingBrickGrid[uiRow][gridX];

        cell.setStroke(Color.TRANSPARENT);
        renderCell(cell, colorId);
        cell.setVisible(true);
    }

    private void renderCell(Rectangle cell, int colorId) {
        cell.setFill(BrickType.getColorById(colorId));
        cell.setArcWidth(9);
        cell.setArcHeight(9);
    }

    @FXML
    public void openSettings(ActionEvent event) {
        if (eventListener instanceof GameController controller) {
            controller.handlePauseToggle();
        }
        event.consume();
    }

    @FXML
    public void newGame(ActionEvent actionEvent) {

        if (eventListener != null) {
            eventListener.createNewGame();
        }
        requestFocus();
    }

    public void showPauseMenu() {
        isPaused.set(true);
        if (activeSettingsView == null && eventListener instanceof GameController controller) {
            activeSettingsView = new SettingsView(controller);
            activeSettingsView.show();
        }
    }

    public void closePauseMenu() {
        isPaused.set(false);
        if (activeSettingsView != null) {
            activeSettingsView.close();
            activeSettingsView = null;
        }
        requestFocus();
    }

    public void requestFocus() {
        if (gamePanel != null) {
            gamePanel.requestFocus();
        }
    }

    private void validateBoardMatrix(int[][] boardMatrix) {
        if (boardMatrix == null) {
            throw new IllegalArgumentException("Board matrix cannot be null");
        }
        if (boardMatrix[0] == null || boardMatrix[0].length == 0) {
            throw new IllegalArgumentException("Board matrix must have at least 3 rows");
        }
        if (boardMatrix.length < 3) {
            throw new IllegalArgumentException("Board matrix must have valid columns");
        }
    }
}
