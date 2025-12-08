package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * ModeSelectionFXController acts as a controller, coordinating between the game model and the view.
 */
public class ModeSelectionFXController {

    @FXML
    private Button endlessButton;

    @FXML
    private Button challengeButton;

    @FXML
    private Button backButton;

    private Runnable onEndlessModeCallback;
    private Runnable onChallengeModeCallback;
    private Runnable onBackCallback;

    @FXML
    /**
     * Performs the initialize operation.
     */
    private void initialize() {

    }

    @FXML
    /**
     * Handles the endless mode.
     */
    private void handleEndlessMode() {
        if (onEndlessModeCallback != null) {
            onEndlessModeCallback.run();
        }
    }

    @FXML
    /**
     * Handles the challenge mode.
     */
    private void handleChallengeMode() {
        if (onChallengeModeCallback != null) {
            onChallengeModeCallback.run();
        }
    }

    @FXML
    /**
     * Handles the back.
     */
    private void handleBack() {
        if (onBackCallback != null) {
            onBackCallback.run();
        }
    }

    /**
     * Sets the on endless mode callback.
     *
     * @param callback the callback
     */
    public void setOnEndlessModeCallback(Runnable callback) {
        this.onEndlessModeCallback = callback;
    }

    /**
     * Sets the on challenge mode callback.
     *
     * @param callback the callback
     */
    public void setOnChallengeModeCallback(Runnable callback) {
        this.onChallengeModeCallback = callback;
    }

    /**
     * Sets the on back callback.
     *
     * @param callback the callback
     */
    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }
}