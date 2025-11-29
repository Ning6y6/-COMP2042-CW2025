package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.event.*;
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
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
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


    private NextPieceView nextPieceView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadResource();
        setupGamePanel();
        setupInputHandling();
        setupUIComponent();
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
        if (isGameActive()) {
            KeyCode code = keyEvent.getCode();
            if (code == KeyCode.LEFT || code == KeyCode.A) {
                refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
            } else if (code == KeyCode.RIGHT || code == KeyCode.D) {
                refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
            } else if (code == KeyCode.UP || code == KeyCode.W) {
                refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
            } else if (code == KeyCode.DOWN || code == KeyCode.S) {
                moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
            }
            keyEvent.consume();
        }
        if (keyEvent.getCode() == KeyCode.N) {
            newGame(null);
        }
    }

    private boolean isGameActive() {
        return !this.isPause.getValue() && !this.isGameOver.getValue();
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
        this.rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rectangle.setFill(BrickType.getColorById(brick.getBrickData()[i][j]));
                this.rectangles[i][j] = rectangle;
                this.brickPanel.add(rectangle, j, i);
            }
        }
        updateBrickLayout(brick);
    }

    private void setupBackgroundGrid(int[][] boardMatrix) {
        this.displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(GameConstants.BRICK_SIZE ,GameConstants.BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                this.displayMatrix[i][j] = rectangle;
                this.gamePanel.add(rectangle, j, i - 2);
            }
        }
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

    public void pauseGame(ActionEvent actionEvent) {
        gamePanel.requestFocus();
    }
}
