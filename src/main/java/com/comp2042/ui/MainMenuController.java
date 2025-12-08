package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * MainMenuController acts as a controller, coordinating between the game model and the view.
 */
public class MainMenuController {

    @FXML
    private javafx.scene.control.Button playButton;

    @FXML
    private javafx.scene.control.Button controlsButton;

    @FXML
    private javafx.scene.control.Button highScoresButton;

    @FXML
    private javafx.scene.control.Button optionsButton;

    @FXML
    private javafx.scene.control.Button quitButton;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private StackPane rootStackPane;

    private Stage stage;
    private Runnable onPlayCallback;
    private Runnable onControlsCallback;
    private Runnable onHighScoresCallback;
    private Runnable onOptionsCallback;
    private Runnable onQuitCallback;

    @FXML
    /**
     * Performs the initialize operation.
     */
    private void initialize() {

        if (backgroundImage != null && rootStackPane != null) {
            backgroundImage.fitWidthProperty().bind(rootStackPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(rootStackPane.heightProperty());
        }
    }

    @FXML
    /**
     * Performs the play operation.
     */
    private void play() {
        if (onPlayCallback != null) {
            onPlayCallback.run();
        }
    }

    @FXML
    /**
     * Performs the controls operation.
     */
    private void controls() {
        if (onControlsCallback != null) {
            onControlsCallback.run();
        }
    }

    @FXML
    /**
     * Performs the high scores operation.
     */
    private void highScores() {
        if (onHighScoresCallback != null) {
            onHighScoresCallback.run();
        }
    }

    @FXML
    /**
     * Performs the options operation.
     */
    private void options() {
        if (onOptionsCallback != null) {
            onOptionsCallback.run();
        }
    }

    @FXML
    /**
     * Performs the quit operation.
     */
    private void quit() {
        if (onQuitCallback != null) {
            onQuitCallback.run();
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
     * Sets the on play callback.
     *
     * @param callback the callback
     */
    public void setOnPlayCallback(Runnable callback) {
        this.onPlayCallback = callback;
    }

    /**
     * Sets the on controls callback.
     *
     * @param callback the callback
     */
    public void setOnControlsCallback(Runnable callback) {
        this.onControlsCallback = callback;
    }

    /**
     * Sets the on high scores callback.
     *
     * @param callback the callback
     */
    public void setOnHighScoresCallback(Runnable callback) {
        this.onHighScoresCallback = callback;
    }

    /**
     * Sets the on options callback.
     *
     * @param callback the callback
     */
    public void setOnOptionsCallback(Runnable callback) {
        this.onOptionsCallback = callback;
    }

    /**
     * Sets the on quit callback.
     *
     * @param callback the callback
     */
    public void setOnQuitCallback(Runnable callback) {
        this.onQuitCallback = callback;
    }
}