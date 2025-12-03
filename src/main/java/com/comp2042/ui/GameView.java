package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.GameController;
import com.comp2042.event.*;
import com.comp2042.input.InputManager;
import com.comp2042.logic.bricks.BrickType;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GameView implements Initializable {

    @FXML
    private Label scoreLabel;

    @FXML
    private Button settingsButton;

    @FXML
    private GridPane gamePanel;

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

    private Rectangle[][] displayMatrix;

    private GameInputHandler eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    private InputManager inputManager;

    private NextPieceView nextPieceView;

    private SettingsView activeSettingsView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadResource();
        setupGamePanel();
        setupInputHandling();
        setupUIComponent();
    }
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    private void setupGamePanel() {
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
    }

    private void setupUIComponent() {
        gameOverPanel.setVisible(false);

        nextPieceView = new NextPieceView();
        nextPieceView.initialize(nextBrickPanel1,nextBrickPanel2,nextBrickPanel3);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);


    }

    private void setupInputHandling() {
        gamePanel.setOnKeyPressed(this::handleKeyInput);
    }

    private void handleKeyInput(KeyEvent keyEvent) {
        if (inputManager !=null) {
            inputManager.handleInput(keyEvent.getCode(), (GameController)eventListener);
            keyEvent.consume();
        }
    }

    private void loadResource() {
        URL fontUrl = getClass().getClassLoader().getResource("digital.ttf");
        if (fontUrl != null) {
            Font.loadFont(fontUrl.toExternalForm(), 38);
        } else{
            System.err.println("CRITICAL ERROR: The font 'digital.ttf' was not found.");
        }
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {

        setupBackgroundGrid(boardMatrix);

        setupFallingPieceGrid(brick);

        this.nextPieceView.initNextBrickPanels(brick);
        setupTimeline();
    }

    private void setupFallingPieceGrid(ViewData brick) {
        int[][] brickData = validateAndGetBrickData(brick);
        this.rectangles = new Rectangle[brickData.length][brickData[0].length];
        
        for (int i = 0; i < brickData.length; i++) {
            if (brickData[i] == null) {
                continue; // Skip null rows
            }
            for (int j = 0; j < brickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(BrickType.getColorById(brickData[i][j]));
                this.rectangles[i][j] = rectangle;
                this.brickPanel.add(rectangle, j, i);
            }
        }
        updateBrickLayout(brick);
    }


    private int[][] validateAndGetBrickData(ViewData brick) {
        if (brick == null) {
            throw new IllegalArgumentException("Brick ViewData cannot be null");
        }
        int[][] brickData = brick.getBrickData();
        if (brickData == null || brickData.length == 0) {
            throw new IllegalArgumentException("Brick data cannot be null or empty");
        }
        if (brickData[0] == null || brickData[0].length == 0) {
            throw new IllegalArgumentException("Brick data first row cannot be null or empty");
        }
        return brickData;
    }

    private void setupBackgroundGrid(int[][] boardMatrix) {
        validateBoardMatrix(boardMatrix);
        int maxRowLength = findMaxRowLength(boardMatrix);
        this.displayMatrix = new Rectangle[boardMatrix.length][maxRowLength];
        
        for (int i = 2; i < boardMatrix.length; i++) {
            if (boardMatrix[i] == null) {
                continue; // Skip null rows
            }
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                this.displayMatrix[i][j] = rectangle;
                this.gamePanel.add(rectangle, j, i - 2);
            }
        }
    }


    private void validateBoardMatrix(int[][] boardMatrix) {
        if (boardMatrix == null) {
            throw new IllegalArgumentException("Board matrix cannot be null");
        }
        if (boardMatrix.length == 0) {
            throw new IllegalArgumentException("Board matrix cannot be empty");
        }
        if (boardMatrix.length < 3) {
            throw new IllegalArgumentException("Board matrix must have at least 3 rows");
        }
    }


    private int findMaxRowLength(int[][] boardMatrix) {
        int maxLength = 0;
        for (int[] row : boardMatrix) {
            if (row != null && row.length > maxLength) {
                maxLength = row.length;
            }
        }
        if (maxLength == 0) {
            throw new IllegalArgumentException("Board matrix must have at least one non-empty row");
        }
        return maxLength;
    }

    private void updateBrickLayout(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * GameConstants.BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * GameConstants.BRICK_SIZE);
    }

    private void setupTimeline() {
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(GameConstants.GAME_SPEED_MILLIS),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public void refreshBrick(ViewData brick) {
        if (!this.isPause.getValue()) {
            updateBrickLayout(brick);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }

            nextPieceView.updateNextBrickPanels(brick);
        }
    }


    public void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(BrickType.getColorById(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void moveDown(MoveEvent event) {
        if (!this.isPause.getValue()) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(GameInputHandler eventListener) {

        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel !=null){
            scoreLabel.textProperty().bind(integerProperty.asString());
        }
    }

    public void gameOver() {
        if (timeLine != null) {
            timeLine.stop();
        }
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        if (timeLine != null){
            timeLine.stop();
        }
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        if (timeLine != null) {
            timeLine.play();
        }
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void showPauseMenu() {
        isPause.setValue(Boolean.TRUE);
        if (activeSettingsView == null && eventListener instanceof GameController) {
            activeSettingsView = new SettingsView((GameController) eventListener);
            activeSettingsView.show();
        }
    }

    public void closePauseMenu() {
        isPause.setValue(Boolean.FALSE);
        if (activeSettingsView != null) {
            activeSettingsView.close();
            activeSettingsView = null;
        }
        gamePanel.requestFocus();
    }

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }


    @FXML
    public void openSettings(ActionEvent event) {
        if (eventListener instanceof GameController) {
            GameController controller = (GameController) eventListener;
            controller.handlePauseToggle();
        }
        event.consume();
    }
}
