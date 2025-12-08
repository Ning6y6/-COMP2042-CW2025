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
        
        // Load custom font
        try {
            Font.loadFont(getClass().getResourceAsStream("/assets/font/main.otf"), 12);
        } catch (Exception e) {
            System.err.println("Failed to load custom font: " + e.getMessage());
            // Continue without custom font
        }
        
        // Initialize and start background music
        AudioManager audioManager = AudioManager.getInstance();
        audioManager.playBGM();
        
        // Initialize shared services
        this.inputManager = new InputManager();
        FileLeaderboardDAO dao = new FileLeaderboardDAO();
        this.leaderboardService = new LeaderboardService(dao);
        
        // Show main menu first
        showMainMenu();
        
        primaryStage.setTitle("Tetris");
        primaryStage.setWidth(GameConstants.WINDOW_WIDTH);
        primaryStage.setHeight(GameConstants.WINDPW_HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        // Stop BGM when window is closed
        primaryStage.setOnCloseRequest(e -> {
            audioManager.stopBGM();
        });
    }
    
    private void showMainMenu() {
        try {
            // Ensure BGM is playing when returning to main menu
            AudioManager.getInstance().resumeBGM();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_menu.fxml"));
            Parent root = loader.load();
            
            MainMenuController controller = loader.getController();
            controller.setStage(primaryStage);
            
            // Set up callbacks
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
                // Back to menu - show main menu
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
            // Use shared inputManager and leaderboardService

            GameViewModel viewModel = new GameViewModel();
            GameLoop gameLoop = new JavaFXGameLoop();
            GameController gameController = new GameController(board, viewModel, inputManager, leaderboardService, gameLoop, mode);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
            Parent root = loader.load();

            GameFXController fxController = loader.getController();
            fxController.initialize(gameController, viewModel);

            // Set up victory callback
            gameController.setOnVictoryCallback(() -> {
                showVictoryScreen(gameController);
            });
            
            // Set up game over callback
            gameController.setOnGameOverCallback(() -> {
                showGameOverScreen(gameController);
            });
            
            // Set up back to main menu callback
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
        
        // Get current score from the view model
        int finalScore = Integer.parseInt(currentViewModel.scoreProperty().get());
        
        victoryView.setCallbacks(
            () -> {
                // Play Again - restart current game
                gameController.restartGame();
            },
            () -> {
                // Back to Menu - show main menu
                showMainMenu();
            }
        );
        
        victoryView.show(primaryStage, finalScore);
    }
    
    private void showGameOverScreen(GameController gameController) {
        GameOverView gameOverView = new GameOverView();
        
        // Get current score from the view model
        int finalScore = Integer.parseInt(currentViewModel.scoreProperty().get());
        
        gameOverView.setCallbacks(
            () -> {
                // Restart - restart current game
                gameController.restartGame();
                // Resume BGM after restart
                AudioManager.getInstance().resumeBGM();
            },
            () -> {
                // Back to Menu - show main menu
                showMainMenu();
            }
        );
        
        gameOverView.show(primaryStage, finalScore);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
