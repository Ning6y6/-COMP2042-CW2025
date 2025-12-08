package com.comp2042.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * GameOverView builds and displays the JavaFX view for the corresponding game screen.
 */
public class GameOverView {

    private Stage stage;
    private GameOverFXController fxController;
    private Runnable onRestartCallback;
    private Runnable onBackToMenuCallback;

    public GameOverView() {
    }

    /**
     * Sets the callbacks.
     *
     * @param onRestart the onRestart
     * @param onBackToMenu the onBackToMenu
     */
    public void setCallbacks(Runnable onRestart, Runnable onBackToMenu) {
        this.onRestartCallback = onRestart;
        this.onBackToMenuCallback = onBackToMenu;
    }

    /**
     * Performs the show operation.
     *
     * @param parentStage the parentStage
     * @param finalScore the finalScore
     */
    public void show(Stage parentStage, int finalScore) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameOverView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("GameOverView.fxml not found at /fxml/GameOverView.fxml");
            }
            Parent root = loader.load();

            fxController = loader.getController();
            if (fxController == null) {
                throw new IllegalStateException("GameOverFXController not found in FXML");
            }

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(parentStage);
            stage.setTitle("Game Over");

            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);

            fxController.setStage(stage);

            fxController.setScore(finalScore);

            fxController.setOnRestartCallback(onRestartCallback);
            fxController.setOnBackToMenuCallback(onBackToMenuCallback);

            stage.show();
            stage.toFront();

        } catch (IOException e) {
            System.err.println("Failed to load GameOverView.fxml: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load GameOverView.fxml", e);
        } catch (Exception e) {
            System.err.println("Error showing Game Over window: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to show Game Over window", e);
        }
    }

    /**
     * Performs the close operation.
     */
    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}