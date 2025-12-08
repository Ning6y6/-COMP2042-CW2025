package com.comp2042.ui;

import com.comp2042.leaderboard.LeaderboardService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Window manager for LeaderboardView.
 * Handles Stage creation, modal setup, and FXML loading.
 */
public class LeaderboardView {

    private final LeaderboardService leaderboardService;
    private Stage stage;

    public LeaderboardView(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LeaderboardView.fxml"));
            Parent root = loader.load();

            LeaderboardFXController controller = loader.getController();

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("High Scores");

            Scene scene = new Scene(root, 350, 450);
            stage.setScene(scene);

            controller.setLeaderboardService(leaderboardService);
            controller.setStage(stage);

            stage.showAndWait();

        } catch (IOException e) {
            System.err.println("Failed to load LeaderboardView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}
