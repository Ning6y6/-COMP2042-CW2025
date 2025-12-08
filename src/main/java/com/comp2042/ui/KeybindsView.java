package com.comp2042.ui;

import com.comp2042.input.InputManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * KeybindsView builds and displays the JavaFX view for the corresponding game screen.
 */
public class KeybindsView {

    private final InputManager inputManager;
    private Stage stage;

    public KeybindsView(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     * Performs the show operation.
     */
    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/KeybindsView.fxml"));
            Parent root = loader.load();

            KeybindsFXController controller = loader.getController();

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Key Bindings");

            Scene scene = new Scene(root, 350, 500);
            stage.setScene(scene);

            controller.setInputManager(inputManager);
            controller.setStage(stage);

            stage.show();
            root.setFocusTraversable(true);
            root.requestFocus();

        } catch (IOException e) {
            System.err.println("Failed to load KeybindsView.fxml: " + e.getMessage());
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