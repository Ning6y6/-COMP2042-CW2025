package com.comp2042.viewmodel;

import com.comp2042.event.ViewData;
import com.comp2042.logic.GameMode;
import javafx.application.Platform;
import javafx.beans.property.*;
import java.util.List;

/**
 * GameViewModel is part of the viewmodel package and encapsulates behaviour related to game view model.
 */
public class GameViewModel {

    private final ObjectProperty<int[][]> boardGrid = new SimpleObjectProperty<>();

    private final ObjectProperty<ViewData> viewData = new SimpleObjectProperty<>();

    /**
     * Performs the simple string property operation.
     *
     * @param "0" the "0"
     * @return the result of the operation
     */
    private final StringProperty score = new SimpleStringProperty("0");
    /**
     * Performs the simple string property operation.
     *
     * @param "1" the "1"
     * @return the result of the operation
     */
    private final StringProperty level = new SimpleStringProperty("1");

    private final ObjectProperty<List<int[][]>> nextPieces = new SimpleObjectProperty<>();
    private final ObjectProperty<int[][]> holdPiece = new SimpleObjectProperty<>();
    /**
     * Performs the simple boolean property operation.
     *
     * @param true the true
     * @return the result of the operation
     */
    private final BooleanProperty holdAllowed = new SimpleBooleanProperty(true);

    /**
     * Performs the simple boolean property operation.
     *
     * @param false the false
     * @return the result of the operation
     */
    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    /**
     * Performs the simple boolean property operation.
     *
     * @param false the false
     * @return the result of the operation
     */
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);
    /**
     * Performs the simple string property operation.
     *
     * @param "" the ""
     * @return the result of the operation
     */
    private final StringProperty notificationMessage = new SimpleStringProperty("");

    private final ObjectProperty<GameMode> gameMode = new SimpleObjectProperty<>(GameMode.ENDLESS);

    /**
     * Performs the simple integer property operation.
     *
     * @param 1 the 1
     * @return the result of the operation
     */
    private final IntegerProperty challengeLevel = new SimpleIntegerProperty(1);
    /**
     * Performs the simple string property operation.
     *
     * @param "00:30" the "00:30"
     * @return the result of the operation
     */
    private final StringProperty challengeTimer = new SimpleStringProperty("00:30");
    /**
     * Performs the simple boolean property operation.
     *
     * @param false the false
     * @return the result of the operation
     */
    private final BooleanProperty isVictory = new SimpleBooleanProperty(false);

    /**
     * Performs the board grid property operation.
     *
     * @return the result of the operation
     */
    public ObjectProperty<int[][]> boardGridProperty() { return boardGrid; }
    /**
     * Performs the view data property operation.
     *
     * @return the result of the operation
     */
    public ObjectProperty<ViewData> viewDataProperty() { return viewData; }
    /**
     * Performs the score property operation.
     *
     * @return the result of the operation
     */
    public StringProperty scoreProperty() { return score; }
    /**
     * Performs the level property operation.
     *
     * @return the result of the operation
     */
    public StringProperty levelProperty() { return level; }
    /**
     * Performs the next pieces property operation.
     *
     * @return the result of the operation
     */
    public ObjectProperty<List<int[][]>> nextPiecesProperty() { return nextPieces; }
    /**
     * Performs the hold piece property operation.
     *
     * @return the result of the operation
     */
    public ObjectProperty<int[][]> holdPieceProperty() { return holdPiece; }
    /**
     * Performs the hold allowed property operation.
     *
     * @return the result of the operation
     */
    public BooleanProperty holdAllowedProperty() { return holdAllowed; }
    /**
     * Returns whether the paused property is true.
     *
     * @return the result of the operation
     */
    public BooleanProperty isPausedProperty() { return isPaused; }
    /**
     * Returns whether the game over property is true.
     *
     * @return the result of the operation
     */
    public BooleanProperty isGameOverProperty() { return isGameOver; }
    /**
     * Performs the notification message property operation.
     *
     * @return the result of the operation
     */
    public StringProperty notificationMessageProperty() { return notificationMessage; }

    /**
     * Performs the game mode property operation.
     *
     * @return the result of the operation
     */
    public ObjectProperty<GameMode> gameModeProperty() { return gameMode; }
    /**
     * Performs the challenge level property operation.
     *
     * @return the result of the operation
     */
    public IntegerProperty challengeLevelProperty() { return challengeLevel; }
    /**
     * Performs the challenge timer property operation.
     *
     * @return the result of the operation
     */
    public StringProperty challengeTimerProperty() { return challengeTimer; }
    /**
     * Returns whether the victory property is true.
     *
     * @return the result of the operation
     */
    public BooleanProperty isVictoryProperty() { return isVictory; }

    /**
     * Returns whether the challenge mode is true.
     *
     * @return the result of the operation
     */
    public boolean isChallengeMode() {
        return gameMode.get() == GameMode.CHALLENGE;
    }

    /**
     * Updates the board.
     *
     * @param grid the grid
     */
    public void updateBoard(int[][] grid) {
        Platform.runLater(() -> boardGrid.set(grid));
    }

    /**
     * Updates the view data.
     *
     * @param data the data
     */
    public void updateViewData(ViewData data) {
        Platform.runLater(() -> viewData.set(data));
    }

    /**
     * Updates the score.
     *
     * @param scoreVal the scoreVal
     */
    public void updateScore(int scoreVal) {
        Platform.runLater(() -> score.set(String.valueOf(scoreVal)));
    }

    /**
     * Updates the level.
     *
     * @param levelVal the levelVal
     */
    public void updateLevel(int levelVal) {
        Platform.runLater(() -> level.set(String.valueOf(levelVal)));
    }

    /**
     * Updates the next pieces.
     *
     * @param pieces the pieces
     */
    public void updateNextPieces(List<int[][]> pieces) {
        Platform.runLater(() -> nextPieces.set(pieces));
    }

    /**
     * Updates the hold piece.
     *
     * @param piece the piece
     * @param allowed the allowed
     */
    public void updateHoldPiece(int[][] piece, boolean allowed) {
        Platform.runLater(() -> {
            holdPiece.set(piece);
            holdAllowed.set(allowed);
        });
    }

    /**
     * Sets the paused.
     *
     * @param paused the paused
     */
    public void setPaused(boolean paused) {
        Platform.runLater(() -> isPaused.set(paused));
    }

    /**
     * Sets the game over.
     *
     * @param over the over
     */
    public void setGameOver(boolean over) {
        Platform.runLater(() -> isGameOver.set(over));
    }

    /**
     * Performs the push notification operation.
     *
     * @param message the message
     */
    public void pushNotification(String message) {
        Platform.runLater(() -> {
            notificationMessage.set("");

            Platform.runLater(() -> notificationMessage.set(message));
        });
    }

    /**
     * Sets the game mode.
     *
     * @param mode the mode
     */
    public void setGameMode(GameMode mode) {
        Platform.runLater(() -> gameMode.set(mode));
    }

    /**
     * Sets the challenge level.
     *
     * @param level the level
     */
    public void setChallengeLevel(int level) {

        if (Platform.isFxApplicationThread()) {
            challengeLevel.set(level);
        } else {
            Platform.runLater(() -> challengeLevel.set(level));
        }
    }

    /**
     * Sets the challenge timer.
     *
     * @param timer the timer
     */
    public void setChallengeTimer(String timer) {
        Platform.runLater(() -> challengeTimer.set(timer));
    }

    /**
     * Sets the victory.
     *
     * @param victory the victory
     */
    public void setVictory(boolean victory) {
        Platform.runLater(() -> isVictory.set(victory));
    }
}