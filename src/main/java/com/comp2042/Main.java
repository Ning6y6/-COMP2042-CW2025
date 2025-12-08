package com.comp2042;

import com.comp2042.input.InputManager;
import com.comp2042.leaderboard.FileLeaderboardDAO;
import com.comp2042.leaderboard.LeaderboardService;
import com.comp2042.logic.Board;
import com.comp2042.logic.GameBoard;
import com.comp2042.logic.GameMode;
import com.comp2042.ui.AudioManager;
import com.comp2042.ui.GameFXController;
import com.comp2042.ui.JavaFXGameLoop;
import com.comp2042.ui.KeybindsView;
import com.comp2042.ui.LeaderboardView;
import com.comp2042.ui.MainMenuController;
import com.comp2042.ui.ModeSelectionView;
import com.comp2042.ui.GameOverView;
import com.comp2042.ui.OptionsView;
import com.comp2042.ui.VictoryView;
import com.comp2042.viewmodel.GameViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    private GameViewModel currentViewModel;
    private Scene gameScene;
    private InputManager inputManager;
    private LeaderboardService leaderboardService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        try {
            Font.loadFont(getClass().getResourceAsStream("/assets/font/main.otf"), 12);
        } catch (Exception e) {
            System.err.println("Failed to load custom font: " + e.getMessage());

        }

        AudioManager audioManager = AudioManager.getInstance();
        audioManager.playBGM();

        this.inputManager = new InputManager();
        FileLeaderboardDAO dao = new FileLeaderboardDAO();
        this.leaderboardService = new LeaderboardService(dao);

        showMainMenu();

        primaryStage.setTitle("Tetris");
        primaryStage.setWidth(GameConstants.WINDOW_WIDTH);
        primaryStage.setHeight(GameConstants.WINDPW_HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> {
            audioManager.stopBGM();
        });
    }

    private void showMainMenu() {
        try {

            AudioManager.getInstance().resumeBGM();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_menu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setStage(primaryStage);

            controller.setOnPlayCallback(() -> showModeSelection());
            controller.setOnControlsCallback(() -> {
                KeybindsView keybindsView = new KeybindsView(inputManager);
                keybindsView.show();
            });
            controller.setOnHighScoresCallback(() -> {
                LeaderboardView leaderboardView = new LeaderboardView(leaderboardService);
                leaderboardView.show();
            });
            controller.setOnOptionsCallback(() -> {
                OptionsView optionsView = new OptionsView();
                optionsView.show(primaryStage);
            });
            controller.setOnQuitCallback(() -> {
                Platform.exit();
            });

            Scene scene = new Scene(root, GameConstants.WINDOW_WIDTH, GameConstants.WINDPW_HEIGHT);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Error loading main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showModeSelection() {
        ModeSelectionView modeSelection = new ModeSelectionView();

        modeSelection.setCallbacks(
            () -> startGame(GameMode.ENDLESS),
            () -> startGame(GameMode.CHALLENGE),
            () -> {

                showMainMenu();
            }
        );

        Scene modeSelectionScene = modeSelection.createScene(primaryStage);
        primaryStage.setScene(modeSelectionScene);
        primaryStage.setTitle("Tetris - Select Game Mode");
    }

    private void startGame(GameMode mode) {
        try {
            Board board = new GameBoard();

            GameViewModel viewModel = new GameViewModel();
            GameLoop gameLoop = new JavaFXGameLoop();
            GameController gameController = new GameController(board, viewModel, inputManager, leaderboardService, gameLoop, mode);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
            Parent root = loader.load();

            GameFXController fxController = loader.getController();
            fxController.initialize(gameController, viewModel);

            gameController.setOnVictoryCallback(() -> {
                showVictoryScreen(gameController);
            });

            gameController.setOnGameOverCallback(() -> {
                showGameOverScreen(gameController);
            });

            gameController.setOnBackToMainMenuCallback(() -> {
                showMainMenu();
            });

            gameScene = new Scene(root, GameConstants.WINDOW_WIDTH, GameConstants.WINDPW_HEIGHT);
            primaryStage.setScene(gameScene);
            primaryStage.setTitle("Tetris - " + (mode == GameMode.ENDLESS ? "Endless Mode" : "Challenge Mode"));

            currentViewModel = viewModel;

            gameController.start();
        } catch (Exception e) {
            System.err.println("Error starting game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showVictoryScreen(GameController gameController) {
        VictoryView victoryView = new VictoryView();

        int finalScore = Integer.parseInt(currentViewModel.scoreProperty().get());

        victoryView.setCallbacks(
            () -> {

                gameController.restartGame();
            },
            () -> {

                showMainMenu();
            }
        );

        victoryView.show(primaryStage, finalScore);
    }

    private void showGameOverScreen(GameController gameController) {
        GameOverView gameOverView = new GameOverView();

        int finalScore = Integer.parseInt(currentViewModel.scoreProperty().get());

        gameOverView.setCallbacks(
            () -> {

                gameController.restartGame();

                AudioManager.getInstance().resumeBGM();
            },
            () -> {

                showMainMenu();
            }
        );

        gameOverView.show(primaryStage, finalScore);
    }

    public static void main(String[] args) {
        launch(args);
    }
}