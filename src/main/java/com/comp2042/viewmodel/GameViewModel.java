package com.comp2042.viewmodel;

import com.comp2042.event.ViewData;
import com.comp2042.logic.GameMode;
import javafx.application.Platform;
import javafx.beans.property.*;
import java.util.List;

public class GameViewModel {

    private final ObjectProperty<int[][]> boardGrid = new SimpleObjectProperty<>();

    private final ObjectProperty<ViewData> viewData = new SimpleObjectProperty<>();

    private final StringProperty score = new SimpleStringProperty("0");
    private final StringProperty level = new SimpleStringProperty("1");

    private final ObjectProperty<List<int[][]>> nextPieces = new SimpleObjectProperty<>();
    private final ObjectProperty<int[][]> holdPiece = new SimpleObjectProperty<>();
    private final BooleanProperty holdAllowed = new SimpleBooleanProperty(true);

    private final BooleanProperty isPaused = new SimpleBooleanProperty(false);
    private final BooleanProperty isGameOver = new SimpleBooleanProperty(false);
    private final StringProperty notificationMessage = new SimpleStringProperty("");

    private final ObjectProperty<GameMode> gameMode = new SimpleObjectProperty<>(GameMode.ENDLESS);

    private final IntegerProperty challengeLevel = new SimpleIntegerProperty(1);
    private final StringProperty challengeTimer = new SimpleStringProperty("00:30");
    private final BooleanProperty isVictory = new SimpleBooleanProperty(false);

    public ObjectProperty<int[][]> boardGridProperty() { return boardGrid; }
    public ObjectProperty<ViewData> viewDataProperty() { return viewData; }
    public StringProperty scoreProperty() { return score; }
    public StringProperty levelProperty() { return level; }
    public ObjectProperty<List<int[][]>> nextPiecesProperty() { return nextPieces; }
    public ObjectProperty<int[][]> holdPieceProperty() { return holdPiece; }
    public BooleanProperty holdAllowedProperty() { return holdAllowed; }
    public BooleanProperty isPausedProperty() { return isPaused; }
    public BooleanProperty isGameOverProperty() { return isGameOver; }
    public StringProperty notificationMessageProperty() { return notificationMessage; }

    public ObjectProperty<GameMode> gameModeProperty() { return gameMode; }
    public IntegerProperty challengeLevelProperty() { return challengeLevel; }
    public StringProperty challengeTimerProperty() { return challengeTimer; }
    public BooleanProperty isVictoryProperty() { return isVictory; }

    public boolean isChallengeMode() {
        return gameMode.get() == GameMode.CHALLENGE;
    }

    public void updateBoard(int[][] grid) {
        Platform.runLater(() -> boardGrid.set(grid));
    }

    public void updateViewData(ViewData data) {
        Platform.runLater(() -> viewData.set(data));
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

    public void setGameMode(GameMode mode) {
        Platform.runLater(() -> gameMode.set(mode));
    }

    public void setChallengeLevel(int level) {

        if (Platform.isFxApplicationThread()) {
            challengeLevel.set(level);
        } else {
            Platform.runLater(() -> challengeLevel.set(level));
        }
    }

    public void setChallengeTimer(String timer) {
        Platform.runLater(() -> challengeTimer.set(timer));
    }

    public void setVictory(boolean victory) {
        Platform.runLater(() -> isVictory.set(victory));
    }
}