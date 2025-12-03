package com.comp2042.ui;

import com.comp2042.GameController;
import com.comp2042.input.commands.PauseCommand;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingsView {

    private final GameController controller;
    private boolean gameResumed = false;
    private Stage stage;

    public SettingsView(GameController controller) {
        this.controller = controller;
    }

    public void show() {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Settings");

        stage.setOnCloseRequest(e -> {
            if (!gameResumed) {
                controller.resumeGame();
                gameResumed = true;
            }
        });

        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");

        Button resumeBtn = createStyledButton("Resume");
        resumeBtn.setOnAction(e -> {
            controller.resumeGame();
            gameResumed = true;
        });

        Button restartBtn = createStyledButton("Restart");
        restartBtn.setOnAction(e -> {
            controller.restartGame();
            gameResumed = true;
        });

        Button keybindsBtn = createStyledButton("Key Bindings");
        keybindsBtn.setOnAction(e -> {
            new KeybindsView(controller.getInputManager()).show();
        });

        Button leaderboardBtn = createStyledButton("Leaderboard");
        leaderboardBtn.setOnAction(e -> {
            new LeaderboardView(controller.getLeaderboardService()).show();
        });

        layout.getChildren().addAll(createHeader("Paused"), resumeBtn, restartBtn, keybindsBtn, leaderboardBtn);

        Scene scene = new Scene(layout, 300, 450);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode pauseKey = controller.getInputManager().getKeyForCommand(PauseCommand.class);
            if (event.getCode() == pauseKey) {
                controller.handlePauseToggle();
                event.consume();
            }
        });
        stage.setScene(scene);
        stage.show();

    }

    public void close() {
        if (stage != null) {
            gameResumed = true;
            stage.close();
        }
    }
    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-font-size: 16px; -fx-min-width: 150px;");
        return btn;
    }

    private Label createHeader(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        return lbl;
    }
}