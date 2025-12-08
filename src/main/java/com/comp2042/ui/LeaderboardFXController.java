package com.comp2042.ui;

import com.comp2042.leaderboard.LeaderboardEntry;
import com.comp2042.leaderboard.LeaderboardService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

public class LeaderboardFXController {

    @FXML
    private ListView<String> scoreListView;

    private LeaderboardService leaderboardService;
    private Stage stage;

    public void setLeaderboardService(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
        populateScores();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void populateScores() {
        scoreListView.getItems().clear();

        List<LeaderboardEntry> topScores = leaderboardService.getTopScores();

        if (topScores.isEmpty()) {
            scoreListView.getItems().add("No scores yet!");
        } else {
            int rank = 1;
            for (LeaderboardEntry entry : topScores) {
                String displayText = String.format("%d. %s - %d pts (%s)",
                        rank++, entry.getName(), entry.getScore(), entry.getDate());
                scoreListView.getItems().add(displayText);
            }
        }
    }

    @FXML
    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
}