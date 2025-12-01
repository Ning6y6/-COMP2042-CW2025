package com.comp2042.ui;

import com.comp2042.leaderboard.LeaderboardEntry;
import com.comp2042.leaderboard.LeaderboardService;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class LeaderboardView {

    private final LeaderboardService leaderboardService;

    public LeaderboardView(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    public void show() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("High Scores");

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");

        Label header = new Label("TOP 10 SCORES");
        header.setStyle("-fx-text-fill: yellow; -fx-font-size: 24px; -fx-font-weight: bold;");

        ListView<String> scoreList = new ListView<>();
        scoreList.setStyle("-fx-control-inner-background: #2b2b2b; -fx-background-color: #2b2b2b;");

        List<LeaderboardEntry> topScores = leaderboardService.getTopScores();

        if (topScores.isEmpty()) {
            scoreList.getItems().add("No scores yet!");
        } else {
            int rank = 1;
            for (LeaderboardEntry entry : topScores) {
                String displayText = String.format("%d. %s - %d pts (%s)",
                        rank++, entry.getName(), entry.getScore(), entry.getDate());
                scoreList.getItems().add(displayText);
            }
        }

        scoreList.setPrefHeight(250);

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-font-size: 14px; -fx-min-width: 100px;");
        closeBtn.setOnAction(e -> stage.close());

        layout.getChildren().addAll(header, scoreList, closeBtn);

        Scene scene = new Scene(layout, 350, 450);
        stage.setScene(scene);
        stage.showAndWait();
    }
}