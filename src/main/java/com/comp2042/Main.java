package com.comp2042;

import com.comp2042.input.InputManager;
import com.comp2042.leaderboard.FileLeaderboardDAO;
import com.comp2042.leaderboard.LeaderboardService;
import com.comp2042.logic.Board;
import com.comp2042.logic.GameBoard;
import com.comp2042.ui.GameFXController;
import com.comp2042.ui.JavaFXGameLoop;
import com.comp2042.viewmodel.GameViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Board board = new GameBoard();
        InputManager inputManager = new InputManager();
        FileLeaderboardDAO dao = new FileLeaderboardDAO();
        LeaderboardService leaderboardService = new LeaderboardService(dao);

        GameViewModel viewModel = new GameViewModel();
        GameLoop gameLoop = new JavaFXGameLoop();
        GameController gameController = new GameController(board, viewModel, inputManager, leaderboardService, gameLoop);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameView.fxml"));
        Parent root = loader.load();

        GameFXController fxController = loader.getController();
        fxController.initialize(gameController, viewModel);

        Scene scene = new Scene(root, GameConstants.WINDOW_WIDTH, GameConstants.WINDPW_HEIGHT);
        primaryStage.setTitle("Tetris");
        primaryStage.setScene(scene);
        primaryStage.show();

        gameController.start();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
