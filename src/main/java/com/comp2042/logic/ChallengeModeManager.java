package com.comp2042.logic;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class ChallengeModeManager {

    public enum ChallengeLevel {
        LEVEL_1(1, 30_000, false, 1.0),
        LEVEL_2(2, 30_000, false, 0.75),
        LEVEL_3(3, 30_000, true, 0.75),
        LEVEL_4(4, 60_000, false, 0.75);

        private final int levelNumber;
        private final long durationMillis;
        private final boolean keysReversed;
        private final double speedMultiplier;

        ChallengeLevel(int levelNumber, long durationMillis, boolean keysReversed, double speedMultiplier) {
            this.levelNumber = levelNumber;
            this.durationMillis = durationMillis;
            this.keysReversed = keysReversed;
            this.speedMultiplier = speedMultiplier;
        }

        public int getLevelNumber() { return levelNumber; }
        public long getDurationMillis() { return durationMillis; }
        public boolean isKeysReversed() { return keysReversed; }
        public double getSpeedMultiplier() { return speedMultiplier; }
    }

    private ChallengeLevel currentLevel;
    private long levelStartTime;
    private long levelStartScore;
    private final IntegerProperty currentLevelProperty;
    private final StringProperty timerDisplayProperty;
    private boolean isActive;
    private Runnable onLevelCompleteCallback;
    private Runnable onVictoryCallback;
    private Runnable onGameOverCallback;
    private Runnable onTimerUpdateCallback;

    public ChallengeModeManager() {
        this.currentLevelProperty = new SimpleIntegerProperty(1);
        this.timerDisplayProperty = new SimpleStringProperty("00:30");
        this.isActive = false;
    }

    public void startChallenge(int initialScore) {
        this.isActive = true;
        this.currentLevel = ChallengeLevel.LEVEL_1;
        this.levelStartTime = System.currentTimeMillis();
        this.levelStartScore = initialScore;
        this.currentLevelProperty.set(1);
        updateTimerDisplay();
    }

    public void update(long currentTimeMillis, int currentScore) {
        if (!isActive) return;

        long elapsed = currentTimeMillis - levelStartTime;
        long remaining = currentLevel.getDurationMillis() - elapsed;

        if (remaining <= 0) {

            handleTimeExpired(currentScore);
        } else {
            updateTimerDisplay(remaining);

            if (currentLevel == ChallengeLevel.LEVEL_4) {
                int scoreNeeded = (int)(levelStartScore + 500);
                if (currentScore >= scoreNeeded) {
                    handleVictory();
                    return;
                }
            }
        }
    }

    public void advanceToNextLevel(int currentScore) {
        if (!isActive) return;

        switch (currentLevel) {
            case LEVEL_1:
                currentLevel = ChallengeLevel.LEVEL_2;
                break;
            case LEVEL_2:
                currentLevel = ChallengeLevel.LEVEL_3;
                break;
            case LEVEL_3:
                currentLevel = ChallengeLevel.LEVEL_4;
                levelStartScore = currentScore;
                break;
            case LEVEL_4:

                return;
        }

        levelStartTime = System.currentTimeMillis();
        currentLevelProperty.set(currentLevel.getLevelNumber());
        updateTimerDisplay();

        if (onLevelCompleteCallback != null) {
            onLevelCompleteCallback.run();
        }
    }

    private void handleTimeExpired(int currentScore) {
        if (currentLevel == ChallengeLevel.LEVEL_4) {

            isActive = false;
            if (onGameOverCallback != null) {
                onGameOverCallback.run();
            }
        } else {

            advanceToNextLevel(currentScore);
        }
    }

    private void handleVictory() {
        isActive = false;
        if (onVictoryCallback != null) {
            onVictoryCallback.run();
        }
    }

    public void handlePlayerDeath() {
        if (!isActive) return;

        isActive = false;
        if (onGameOverCallback != null) {
            onGameOverCallback.run();
        }
    }

    private void updateTimerDisplay() {
        updateTimerDisplay(currentLevel.getDurationMillis());
    }

    private void updateTimerDisplay(long remainingMillis) {
        long totalSeconds = remainingMillis / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        String display = String.format("%02d:%02d", minutes, seconds);
        Platform.runLater(() -> timerDisplayProperty.set(display));

        if (onTimerUpdateCallback != null) {
            onTimerUpdateCallback.run();
        }
    }

    public ChallengeLevel getCurrentLevel() {
        return currentLevel;
    }

    public boolean isKeysReversed() {
        return currentLevel != null && currentLevel.isKeysReversed();
    }

    public double getSpeedMultiplier() {
        return currentLevel != null ? currentLevel.getSpeedMultiplier() : 1.0;
    }

    public boolean isActive() {
        return isActive;
    }

    public IntegerProperty currentLevelProperty() {
        return currentLevelProperty;
    }

    public StringProperty timerDisplayProperty() {
        return timerDisplayProperty;
    }

    public void setOnLevelCompleteCallback(Runnable callback) {
        this.onLevelCompleteCallback = callback;
    }

    public void setOnVictoryCallback(Runnable callback) {
        this.onVictoryCallback = callback;
    }

    public void setOnGameOverCallback(Runnable callback) {
        this.onGameOverCallback = callback;
    }

    public void setOnTimerUpdateCallback(Runnable callback) {
        this.onTimerUpdateCallback = callback;
    }

    public void reset() {
        this.isActive = false;
        this.currentLevel = null;
        this.levelStartTime = 0;
        this.levelStartScore = 0;
        this.currentLevelProperty.set(1);
        this.timerDisplayProperty.set("00:30");
    }
}
