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
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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

        int rows = boardMatrix.length;
        int col = boardMatrix[0].length;

        initBackgroundGrid(rows, col);
        initFallingBrickLayer(rows, col);

        refreshBoard(boardMatrix);
        updateNextPieces(initialBrick);
        updateHoldPiece(initialBrick);

    }

    public void updateFallingBrick(ViewData brickData) {
        if (isPaused.get()) {
            return;
        }

        clearFallingBrickLayer();
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

        for (int row = 2; row < boardMatrix.length; row++) {
            for (int col = 0; col < boardMatrix[row].length; col++) {
                int colorId = boardMatrix[row][col];
                renderCell(backgroundGrid[row][col], colorId);
            }
        }
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

    private void initBackgroundGrid(int rows, int cols) {
        gamePanel.getChildren().clear();

        backgroundGrid = new Rectangle[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Rectangle cell = createCell();
                backgroundGrid[row][col] = cell;
                if (row >= 2) {
                    gamePanel.add(cell, col, row - 2);
                }
            }
        }
    }

    private void initFallingBrickLayer(int rows, int cols) {
        brickPanel.getChildren().clear();

        fallingBrickGrid = new Rectangle[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Rectangle cell = createCell();
                cell.setVisible(false);
                fallingBrickGrid[row][col] = cell;

                if (row >= 2 && brickPanel != null) {
                    brickPanel.add(cell, col, row - 2);
                }
            }
        }
    }

    private Rectangle createCell() {
        Rectangle cell = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
        cell.setFill(Color.TRANSPARENT);
        return cell;
    }

    private void clearFallingBrickLayer() {
        for (Rectangle[] row : fallingBrickGrid) {
            for (Rectangle cell : row) {
                if (cell != null) cell.setVisible(false);
            }
        }
    }

    private void renderFallingBrick(ViewData brickData) {
        if (brickData == null || brickData.getBrickData() == null) {
            return;
        }

        int[][] brickMatrix = brickData.getBrickData();
        int xPos = brickData.getxPosition();
        int yPos = brickData.getyPosition();

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
        int displayY = gridY - 2;

        if (displayY < 0 || displayY >= fallingBrickGrid.length - 2) {
            return;
        }

        if (gridX < 0 || gridX >= fallingBrickGrid[0].length) {
            return;
        }

        Rectangle cell = fallingBrickGrid[gridY][gridX];

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
