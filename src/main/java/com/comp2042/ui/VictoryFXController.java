package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * VictoryFXController acts as a controller, coordinating between the game model and the view.
 */
public class VictoryFXController {

    @FXML
    private Button playAgainButton;

    @FXML
    private Button backToMenuButton;

    @FXML
    private Label scoreLabel;

    private Stage stage;
    private Runnable onPlayAgainCallback;
    private Runnable onBackToMenuCallback;

    @FXML
    /**
     * Performs the initialize operation.
     */
    private void initialize() {

    }

    @FXML
    /**
     * Handles the play again.
     */
    private void handlePlayAgain() {
        if (onPlayAgainCallback != null) {
            onPlayAgainCallback.run();
        }
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    /**
     * Handles the back to menu.
     */
    private void handleBackToMenu() {
        if (onBackToMenuCallback != null) {
            onBackToMenuCallback.run();
        }
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Sets the stage.
     *
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the score.
     *
     * @param score the score
     */
    public void setScore(int score) {
        if (scoreLabel != null) {
            scoreLabel.setText("Score: " + score);
        }
    }

    /**
     * Sets the on play again callback.
     *
     * @param callback the callback
     */
    public void setOnPlayAgainCallback(Runnable callback) {
        this.onPlayAgainCallback = callback;
    }

    /**
     * Sets the on back to menu callback.
     *
     * @param callback the callback
     */
    public void setOnBackToMenuCallback(Runnable callback) {
        this.onBackToMenuCallback = callback;
    }
}