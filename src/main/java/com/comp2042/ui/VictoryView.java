package com.comp2042.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * VictoryView builds and displays the JavaFX view for the corresponding game screen.
 */
public class VictoryView {

    private Stage stage;
    private VictoryFXController fxController;
    private Runnable onPlayAgainCallback;
    private Runnable onBackToMenuCallback;

    public VictoryView() {
    }

    /**
     * Sets the callbacks.
     *
     * @param onPlayAgain the onPlayAgain
     * @param onBackToMenu the onBackToMenu
     */
    public void setCallbacks(Runnable onPlayAgain, Runnable onBackToMenu) {
        this.onPlayAgainCallback = onPlayAgain;
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VictoryView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("VictoryView.fxml not found at /fxml/VictoryView.fxml");
            }
            Parent root = loader.load();

            fxController = loader.getController();
            if (fxController == null) {
                throw new IllegalStateException("VictoryFXController not found in FXML");
            }

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(parentStage);
            stage.setTitle("Victory!");

            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);

            fxController.setStage(stage);

            fxController.setScore(finalScore);

            fxController.setOnPlayAgainCallback(onPlayAgainCallback);
            fxController.setOnBackToMenuCallback(onBackToMenuCallback);

            stage.show();
            stage.toFront();

        } catch (IOException e) {
            System.err.println("Failed to load VictoryView.fxml: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load VictoryView.fxml", e);
        } catch (Exception e) {
            System.err.println("Error showing Victory window: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to show Victory window", e);
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