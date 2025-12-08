package com.comp2042.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * OptionsView builds and displays the JavaFX view for the corresponding game screen.
 */
public class OptionsView {

    private Stage stage;
    private OptionsFXController fxController;

    public OptionsView() {
    }

    /**
     * Performs the show operation.
     *
     * @param parentStage the parentStage
     */
    public void show(Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OptionsView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("OptionsView.fxml not found at /fxml/OptionsView.fxml");
            }
            Parent root = loader.load();

            fxController = loader.getController();
            if (fxController == null) {
                throw new IllegalStateException("OptionsFXController not found in FXML");
            }

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            if (parentStage != null) {
                stage.initOwner(parentStage);
            }
            stage.setTitle("Options");

            Scene scene = new Scene(root, 400, 250);
            stage.setScene(scene);

            fxController.setStage(stage);

            stage.show();
            stage.toFront();

        } catch (IOException e) {
            System.err.println("Failed to load OptionsView.fxml: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error showing Options window: " + e.getMessage());
            e.printStackTrace();
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