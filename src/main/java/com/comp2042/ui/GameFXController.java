package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.GameController;
import com.comp2042.logic.bricks.BrickType;
import com.comp2042.viewmodel.GameViewModel;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import java.util.List;

public class GameFXController {

    @FXML private StackPane rootStackPane;
    @FXML private GridPane mainBoardGrid;
    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private Button pauseButton;


    @FXML private GridPane holdPanel;
    @FXML private GridPane nextBrickPanel1;
    @FXML private GridPane nextBrickPanel2;
    @FXML private GridPane nextBrickPanel3;
    @FXML private StackPane overlayPane;

    private GameController controller;
    private GameViewModel viewModel;

    // View Helpers
    private NextPieceView nextPieceView;
    private HoldView holdView;

    private Rectangle[][] displayGridNodes;

    public void initialize(GameController controller, GameViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        if (pauseButton != null) {
            pauseButton.setFocusTraversable(false);
        }
        // Initialize Helpers
        this.nextPieceView = new NextPieceView();
        this.nextPieceView.initialize(nextBrickPanel1, nextBrickPanel2, nextBrickPanel3);

        this.holdView = new HoldView();
        this.holdView.initialize(holdPanel);

        initGridNodes();

        scoreLabel.textProperty().bind(viewModel.scoreProperty());
        levelLabel.textProperty().bind(viewModel.levelProperty());

        // 2. Bind Main Board
        viewModel.boardGridProperty().addListener((obs, oldGrid, newGrid) -> renderBoard(newGrid));

        // 3. Bind Side Panels
        viewModel.nextPiecesProperty().addListener((obs, oldVal, newVal) ->
                nextPieceView.updateNextBrickPanels(newVal)
        );

        viewModel.holdPieceProperty().addListener((obs, oldVal, newVal) ->
                holdView.updateHoldPanel(newVal, viewModel.holdAllowedProperty().get())
        );

        // 4. Bind Overlays / Pause
        overlayPane.visibleProperty().bind(
                Bindings.or(viewModel.isPausedProperty(), viewModel.isGameOverProperty())
        );

        // 5. Notifications
        viewModel.notificationMessageProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                showNotification(newVal);
            }
        });

        // Setup Input Focus
        rootStackPane.setFocusTraversable(true);
        rootStackPane.setOnKeyPressed(this::handleInput);
        rootStackPane.setOnMouseClicked(e -> rootStackPane.requestFocus());
        Platform.runLater(() ->rootStackPane.requestFocus());
    }

    private void initGridNodes() {
        mainBoardGrid.getChildren().clear();
        displayGridNodes = new Rectangle[GameConstants.VISIBLE_ROWS][GameConstants.BOARD_WIDTH];

        for (int r = 0; r < GameConstants.VISIBLE_ROWS; r++) {
            for (int c = 0; c < GameConstants.BOARD_WIDTH; c++) {
                Rectangle rect = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rect.getStyleClass().add("cell"); // Base class
                displayGridNodes[r][c] = rect;
                mainBoardGrid.add(rect, c, r);
            }
        }
    }

    private void renderBoard(int[][] gridData) {
        if (gridData == null) return;

        // Offset for hidden rows
        int offset = GameConstants.HIDDEN_ROWS;

        for (int r = 0; r < GameConstants.VISIBLE_ROWS; r++) {
            for (int c = 0; c < GameConstants.BOARD_WIDTH; c++) {
                int dataRow = r + offset;
                if (dataRow < gridData.length) {
                    int typeId = gridData[dataRow][c];
                    updateCell(displayGridNodes[r][c], typeId);
                }
            }
        }
    }

    private void updateCell(Rectangle rect, int typeId) {
        rect.getStyleClass().clear();
        rect.getStyleClass().add("cell");

        // Map ID to CSS Class
        BrickType type = BrickType.getById(typeId);
        rect.getStyleClass().add(type.getCssClass());
    }

    private void handleInput(KeyEvent event) {
        // Delegate pure input to InputManager via Controller
        controller.getInputManager().handleInput(event.getCode(), controller);
        event.consume();
    }

    private void showNotification(String message) {
        NotificationPanel panel = new NotificationPanel(message);
        rootStackPane.getChildren().add(panel);
        panel.playAnimation();
    }

    @FXML
    public void openSettings() {
        controller.handlePauseToggle();
    }
}