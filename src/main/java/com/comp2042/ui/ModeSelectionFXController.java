package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * FXML Controller for ModeSelectionView.
 * Acts as a mediator between the FXML view and navigation logic.
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
    private void initialize() {
        // Initialization if needed
    }

    @FXML
    private void handleEndlessMode() {
        if (onEndlessModeCallback != null) {
            onEndlessModeCallback.run();
        }
    }

    @FXML
    private void handleChallengeMode() {
        if (onChallengeModeCallback != null) {
            onChallengeModeCallback.run();
        }
    }

    @FXML
    private void handleBack() {
        if (onBackCallback != null) {
            onBackCallback.run();
        }
    }

    public void setOnEndlessModeCallback(Runnable callback) {
        this.onEndlessModeCallback = callback;
    }

    public void setOnChallengeModeCallback(Runnable callback) {
        this.onChallengeModeCallback = callback;
    }

    public void setOnBackCallback(Runnable callback) {
        this.onBackCallback = callback;
    }
}

