package com.comp2042.viewmodel;

import javafx.application.Platform;
import javafx.beans.property.*;
import java.util.List;

/**
 * ViewModel for the Tetris Game.
 * Holds the presentation state and data observed by the View.
 */
public class GameViewModel {

    // Board State
    private final ObjectProperty<int[][]> boardGrid = new SimpleObjectProperty<>();

    // Game Stats
    private final StringProperty score = new SimpleStringProperty("0");
    private final StringProperty level = new SimpleStringProperty("1");

    // Future Pieces (Data Only)
    private final ObjectProperty<List<int[][]>> nextPieces = new SimpleObjectProperty<>();
    private final ObjectProperty<int[][]> holdPiece = new SimpleObjectProperty<>();
    private final BooleanProperty holdAllowed = new SimpleBooleanProperty(true);

    // Game Status
    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);
    private final StringProperty notificationMessage = new SimpleStringProperty("");

    // Property Getters for Binding
    public ObjectProperty<int[][]> boardGridProperty() { return boardGrid; }
    public StringProperty scoreProperty() { return score; }
    public StringProperty levelProperty() { return level; }
    public ObjectProperty<List<int[][]>> nextPiecesProperty() { return nextPieces; }
    public ObjectProperty<int[][]> holdPieceProperty() { return holdPiece; }
    public BooleanProperty holdAllowedProperty() { return holdAllowed; }
    public BooleanProperty isPausedProperty() { return isPaused; }
    public BooleanProperty isGameOverProperty() { return isGameOver; }
    public StringProperty notificationMessageProperty() { return notificationMessage; }

    // State Update Methods (Called by Controller)
    public void updateBoard(int[][] grid) {
        Platform.runLater(() -> boardGrid.set(grid));
    }

    public void updateScore(int scoreVal) {
        Platform.runLater(() -> score.set(String.valueOf(scoreVal)));
    }

    public void updateLevel(int levelVal) {
        Platform.runLater(() -> level.set(String.valueOf(levelVal)));
    }

    public void updateNextPieces(List<int[][]> pieces) {
        Platform.runLater(() -> nextPieces.set(pieces));
    }

    public void updateHoldPiece(int[][] piece, boolean allowed) {
        Platform.runLater(() -> {
            holdPiece.set(piece);
            holdAllowed.set(allowed);
        });
    }

    public void setPaused(boolean paused) {
        Platform.runLater(() -> isPaused.set(paused));
    }

    public void setGameOver(boolean over) {
        Platform.runLater(() -> isGameOver.set(over));
    }

    public void pushNotification(String message) {
        Platform.runLater(() -> {
            notificationMessage.set("");
            
            Platform.runLater(() -> notificationMessage.set(message));
        });
    }
}