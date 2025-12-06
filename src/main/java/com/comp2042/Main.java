package com.comp2042;

import com.comp2042.input.InputManager;
import com.comp2042.leaderboard.FileLeaderboardDAO;
import com.comp2042.leaderboard.LeaderboardService;
import com.comp2042.logic.Board;
import com.comp2042.logic.GameBoard;
import com.comp2042.ui.GameView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        GameView gameView = fxmlLoader.getController();

        Board board = new GameBoard();
        InputManager inputManager = new InputManager();
        FileLeaderboardDAO dao = new FileLeaderboardDAO();
        LeaderboardService leaderboardService = new LeaderboardService(dao);


        GameController gameController = new GameController(gameView, board, inputManager, leaderboardService);
        Scene scene = new Scene(root, GameConstants.WINDOW_WIDTH, GameConstants.WINDPW_HEIGHT);

        root.setFocusTraversable(true);
        root.setOnMouseClicked(event -> root.requestFocus());

        primaryStage.setTitle("TetrisJFX");
        primaryStage.setScene(scene);
        primaryStage.show();

        root.requestFocus();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
