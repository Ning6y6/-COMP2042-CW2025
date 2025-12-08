package com.comp2042.logic;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * ChallengeModeManager manages and coordinates challenge mode behaviour.
 */
public class ChallengeModeManager {

    /**
     * ChallengeLevel is part of the logic package and encapsulates behaviour related to challenge level.
     */
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

        /**
         * Returns the level number.
         *
         * @return the result of the operation
         */
        public int getLevelNumber() { return levelNumber; }
        /**
         * Returns the duration millis.
         *
         * @return the result of the operation
         */
        public long getDurationMillis() { return durationMillis; }
        /**
         * Returns whether the keys reversed is true.
         *
         * @return the result of the operation
         */
        public boolean isKeysReversed() { return keysReversed; }
        /**
         * Returns the speed multiplier.
         *
         * @return the result of the operation
         */
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

    /**
     * Performs the start challenge operation.
     *
     * @param initialScore the initialScore
     */
    public void startChallenge(int initialScore) {
        this.isActive = true;
        this.currentLevel = ChallengeLevel.LEVEL_1;
        this.levelStartTime = System.currentTimeMillis();
        this.levelStartScore = initialScore;
        this.currentLevelProperty.set(1);
        updateTimerDisplay();
    }

    /**
     * Updates the state.
     *
     * @param currentTimeMillis the currentTimeMillis
     * @param currentScore the currentScore
     */
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

    /**
     * Performs the advance to next level operation.
     *
     * @param currentScore the currentScore
     */
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

    /**
     * Handles the time expired.
     *
     * @param currentScore the currentScore
     */
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

    /**
     * Handles the victory.
     */
    private void handleVictory() {
        isActive = false;
        if (onVictoryCallback != null) {
            onVictoryCallback.run();
        }
    }

    /**
     * Handles the player death.
     */
    public void handlePlayerDeath() {
        if (!isActive) return;

        isActive = false;
        if (onGameOverCallback != null) {
            onGameOverCallback.run();
        }
    }

    /**
     * Updates the timer display.
     */
    private void updateTimerDisplay() {
        updateTimerDisplay(currentLevel.getDurationMillis());
    }

    /**
     * Updates the timer display.
     *
     * @param remainingMillis the remainingMillis
     */
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

    /**
     * Returns the current level.
     *
     * @return the result of the operation
     */
    public ChallengeLevel getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Returns whether the keys reversed is true.
     *
     * @return the result of the operation
     */
    public boolean isKeysReversed() {
        return currentLevel != null && currentLevel.isKeysReversed();
    }

    /**
     * Returns the speed multiplier.
     *
     * @return the result of the operation
     */
    public double getSpeedMultiplier() {
        return currentLevel != null ? currentLevel.getSpeedMultiplier() : 1.0;
    }

    /**
     * Returns whether the active is true.
     *
     * @return the result of the operation
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Performs the current level property operation.
     *
     * @return the result of the operation
     */
    public IntegerProperty currentLevelProperty() {
        return currentLevelProperty;
    }

    /**
     * Performs the timer display property operation.
     *
     * @return the result of the operation
     */
    public StringProperty timerDisplayProperty() {
        return timerDisplayProperty;
    }

    /**
     * Sets the on level complete callback.
     *
     * @param callback the callback
     */
    public void setOnLevelCompleteCallback(Runnable callback) {
        this.onLevelCompleteCallback = callback;
    }

    /**
     * Sets the on victory callback.
     *
     * @param callback the callback
     */
    public void setOnVictoryCallback(Runnable callback) {
        this.onVictoryCallback = callback;
    }

    /**
     * Sets the on game over callback.
     *
     * @param callback the callback
     */
    public void setOnGameOverCallback(Runnable callback) {
        this.onGameOverCallback = callback;
    }

    /**
     * Sets the on timer update callback.
     *
     * @param callback the callback
     */
    public void setOnTimerUpdateCallback(Runnable callback) {
        this.onTimerUpdateCallback = callback;
    }

    /**
     * Performs the reset operation.
     */
    public void reset() {
        this.isActive = false;
        this.currentLevel = null;
        this.levelStartTime = 0;
        this.levelStartScore = 0;
        this.currentLevelProperty.set(1);
        this.timerDisplayProperty.set("00:30");
    }
}