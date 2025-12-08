package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.GameController;
import com.comp2042.event.ViewData;
import com.comp2042.input.commands.PauseCommand;
import com.comp2042.logic.GameMode;
import com.comp2042.logic.bricks.BrickType;
import com.comp2042.viewmodel.GameViewModel;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.util.List;

/**
 * GameFXController acts as a controller, coordinating between the game model and the view.
 */
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
    @FXML private Label overlayLabel;
    @FXML private Label challengeTimerLabel;
    @FXML private Label levelHeaderLabel;
    @FXML private StackPane victoryPane;
    @FXML private ImageView backgroundImageLevel123;
    @FXML private ImageView backgroundImageLevel4;

    private GameController controller;
    private GameViewModel viewModel;
    private int previousChallengeLevel = 1;

    private ViewData currentViewData;

    private NextPieceView nextPieceView;
    private HoldView holdView;

    private Rectangle[][] displayGridNodes;

    /**
     * Performs the initialize operation.
     *
     * @param controller the controller
     * @param viewModel the viewModel
     */
    public void initialize(GameController controller, GameViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        if (pauseButton != null) {
            pauseButton.setFocusTraversable(false);
        }

        initializeBackgroundImages();

        this.nextPieceView = new NextPieceView();
        this.nextPieceView.initialize(nextBrickPanel1, nextBrickPanel2, nextBrickPanel3);

        this.holdView = new HoldView();
        this.holdView.initialize(holdPanel);

        initGridNodes();

        scoreLabel.textProperty().bind(viewModel.scoreProperty());

        if (levelHeaderLabel != null && levelLabel != null) {

            javafx.beans.binding.BooleanBinding isChallengeMode = Bindings.createBooleanBinding(
                    () -> {
                        GameMode mode = viewModel.gameModeProperty().get();
                        return mode != null && mode == GameMode.CHALLENGE;
                    },
                    viewModel.gameModeProperty()
            );

            levelHeaderLabel.visibleProperty().bind(isChallengeMode);
            levelHeaderLabel.managedProperty().bind(levelHeaderLabel.visibleProperty());

            levelLabel.visibleProperty().bind(isChallengeMode);
            levelLabel.managedProperty().bind(levelLabel.visibleProperty());

            levelLabel.textProperty().bind(
                    Bindings.createStringBinding(
                            () -> {
                                GameMode mode = viewModel.gameModeProperty().get();
                                if (mode != null && mode == GameMode.CHALLENGE) {
                                    int level = viewModel.challengeLevelProperty().get();
                                    return String.valueOf(level);
                                }
                                return "";
                            },
                            viewModel.gameModeProperty(),
                            viewModel.challengeLevelProperty()
                    )
            );
        }

        if (challengeTimerLabel != null) {
            challengeTimerLabel.textProperty().bind(viewModel.challengeTimerProperty());
            challengeTimerLabel.visibleProperty().bind(
                    Bindings.createBooleanBinding(
                            () -> viewModel.gameModeProperty().get() == GameMode.CHALLENGE,
                            viewModel.gameModeProperty()
                    )
            );
            challengeTimerLabel.managedProperty().bind(challengeTimerLabel.visibleProperty());
        }

        viewModel.challengeLevelProperty().addListener((obs, oldLevel, newLevel) -> {
            if (viewModel.gameModeProperty().get() == GameMode.CHALLENGE) {
                handleChallengeLevelChange(oldLevel.intValue(), newLevel.intValue());
            }
        });

        updateBackgroundForMode();

        viewModel.boardGridProperty().addListener((obs, oldGrid, newGrid) -> renderBoard(newGrid));

        viewModel.viewDataProperty().addListener((obs, oldData, newData) -> {
            currentViewData = newData;

            renderBoard(viewModel.boardGridProperty().get());
        });

        viewModel.nextPiecesProperty().addListener((obs, oldVal, newVal) ->
                nextPieceView.updateNextBrickPanels(newVal)
        );

        viewModel.holdPieceProperty().addListener((obs, oldVal, newVal) ->
                holdView.updateHoldPanel(newVal, viewModel.holdAllowedProperty().get())
        );

        overlayPane.visibleProperty().bind(viewModel.isPausedProperty());

        if (victoryPane != null) {
            victoryPane.visibleProperty().bind(
                    Bindings.and(viewModel.isGameOverProperty(), viewModel.isVictoryProperty())
            );
        }

        viewModel.isPausedProperty().addListener((obs, wasPaused, isPaused) -> {
            if (isPaused) {
                overlayLabel.setText("PAUSED");
            }
        });

        viewModel.notificationMessageProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                showNotification(newVal);
            }
        });

        rootStackPane.setFocusTraversable(true);
        rootStackPane.setOnKeyPressed(this::handleInput);
        rootStackPane.setOnMouseClicked(e -> rootStackPane.requestFocus());
        Platform.runLater(() ->rootStackPane.requestFocus());
    }

    /**
     * Performs the init grid nodes operation.
     */
    private void initGridNodes() {
        mainBoardGrid.getChildren().clear();
        displayGridNodes = new Rectangle[GameConstants.VISIBLE_ROWS][GameConstants.BOARD_WIDTH];

        for (int r = 0; r < GameConstants.VISIBLE_ROWS; r++) {
            for (int c = 0; c < GameConstants.BOARD_WIDTH; c++) {
                Rectangle rect = new Rectangle(GameConstants.BRICK_SIZE, GameConstants.BRICK_SIZE);
                rect.getStyleClass().add("cell");
                displayGridNodes[r][c] = rect;
                mainBoardGrid.add(rect, c, r);
            }
        }
    }

    /**
     * Renders the board.
     *
     * @param gridData the gridData
     */
    private void renderBoard(int[][] gridData) {
        if (gridData == null) return;

        int offset = GameConstants.HIDDEN_ROWS;

        for (int r = 0; r < GameConstants.VISIBLE_ROWS; r++) {
            for (int c = 0; c < GameConstants.BOARD_WIDTH; c++) {
                int dataRow = r + offset;
                int typeId = 0;
                boolean isValidCell = false;

                if (dataRow >= 0 && dataRow < gridData.length && gridData[dataRow] != null && c < gridData[dataRow].length) {
                    typeId = gridData[dataRow][c];
                    isValidCell = true;
                }

                GhostCellInfo ghostInfo = getGhostCellInfo(dataRow, c);
                if (ghostInfo.isGhost && typeId == 0 && isValidCell) {

                    updateCell(displayGridNodes[r][c], ghostInfo.brickTypeId, true);
                } else {

                    updateCell(displayGridNodes[r][c], typeId, false);
                }
            }
        }
    }

    private static class GhostCellInfo {
        final boolean isGhost;
        final int brickTypeId;

        GhostCellInfo(boolean isGhost, int brickTypeId) {
            this.isGhost = isGhost;
            this.brickTypeId = brickTypeId;
        }
    }

    /**
     * Returns the ghost cell info.
     *
     * @param boardRow the boardRow
     * @param boardCol the boardCol
     * @return the result of the operation
     */
    private GhostCellInfo getGhostCellInfo(int boardRow, int boardCol) {
        if (currentViewData == null) {
            return new GhostCellInfo(false, 0);
        }

        int[][] brickData = currentViewData.getBrickData();
        if (brickData == null) {
            return new GhostCellInfo(false, 0);
        }

        int activeX = currentViewData.getXPosition();
        int activeY = currentViewData.getYPosition();
        int ghostY = currentViewData.getGhostYPosition();

        if (ghostY == activeY) {
            return new GhostCellInfo(false, 0);
        }

        for (int r = 0; r < brickData.length; r++) {
            for (int c = 0; c < brickData[r].length; c++) {
                if (brickData[r][c] != 0) {
                    int ghostTargetY = ghostY + r;
                    int ghostTargetX = activeX + c;

                    if (ghostTargetY == boardRow && ghostTargetX == boardCol) {

                        int activeTargetY = activeY + r;
                        int activeTargetX = activeX + c;
                        if (ghostTargetY != activeTargetY || ghostTargetX != activeTargetX) {

                            return new GhostCellInfo(true, brickData[r][c]);
                        }
                    }
                }
            }
        }

        return new GhostCellInfo(false, 0);
    }

    /**
     * Updates the cell.
     *
     * @param rect the rect
     * @param typeId the typeId
     * @param isGhost the isGhost
     */
    private void updateCell(Rectangle rect, int typeId, boolean isGhost) {
        rect.getStyleClass().clear();
        rect.getStyleClass().add("cell");

        BrickType type = BrickType.getById(typeId);
        rect.getStyleClass().add(type.getCssClass());

        if (isGhost) {
            rect.getStyleClass().add("ghost");
        }
    }

    /**
     * Handles the input.
     *
     * @param event the event
     */
    private void handleInput(KeyEvent event) {

        boolean wasPaused = viewModel.isPausedProperty().get();
        KeyCode pauseKey = controller.getInputManager().getKeyForCommand(PauseCommand.class);
        boolean isPauseKey = event.getCode() == pauseKey;

        controller.getInputManager().handleInput(event.getCode(), controller);

        if (isPauseKey && !wasPaused) {
            Platform.runLater(() -> {
                if (viewModel.isPausedProperty().get()) {
                    showSettingsWindow();
                }
            });
        }

        event.consume();
    }

    /**
     * Performs the show notification operation.
     *
     * @param message the message
     */
    private void showNotification(String message) {
        NotificationPanel panel = new NotificationPanel(message);
        rootStackPane.getChildren().add(panel);
        panel.playAnimation();
    }

    @FXML
    /**
     * Performs the open settings operation.
     */
    public void openSettings() {

        boolean wasPaused = viewModel.isPausedProperty().get();

        if (!wasPaused) {

            controller.handlePauseToggle();

            Platform.runLater(() -> {
                if (viewModel.isPausedProperty().get()) {
                    showSettingsWindow();
                }
            });
        } else {

            showSettingsWindow();
        }
    }

    /**
     * Performs the show settings window operation.
     */
    private void showSettingsWindow() {
        try {
            SettingsView settingsView = new SettingsView(controller);
            settingsView.show();
        } catch (Exception e) {
            System.err.println("ERROR showing Settings window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Performs the initialize background images operation.
     */
    private void initializeBackgroundImages() {
        if (backgroundImageLevel123 != null && rootStackPane != null) {
            backgroundImageLevel123.fitWidthProperty().bind(rootStackPane.widthProperty());
            backgroundImageLevel123.fitHeightProperty().bind(rootStackPane.heightProperty());
            backgroundImageLevel123.setOpacity(1.0);
        }

        if (backgroundImageLevel4 != null && rootStackPane != null) {
            backgroundImageLevel4.fitWidthProperty().bind(rootStackPane.widthProperty());
            backgroundImageLevel4.fitHeightProperty().bind(rootStackPane.heightProperty());
            backgroundImageLevel4.setOpacity(0.0);
        }
    }

    /**
     * Updates the background for mode.
     */
    private void updateBackgroundForMode() {
        GameMode mode = viewModel.gameModeProperty().get();
        if (mode == GameMode.ENDLESS) {

            showLevel123Background();
        } else if (mode == GameMode.CHALLENGE) {

            int level = viewModel.challengeLevelProperty().get();
            if (level == 4) {
                showLevel4Background();
            } else {
                showLevel123Background();
            }
        }
    }

    /**
     * Handles the challenge level change.
     *
     * @param oldLevel the oldLevel
     * @param newLevel the newLevel
     */
    private void handleChallengeLevelChange(int oldLevel, int newLevel) {
        previousChallengeLevel = oldLevel;

        if (oldLevel == 3 && newLevel == 4) {
            transitionToLevel4Background();
        } else if (newLevel < 4 && oldLevel == 4) {

            transitionToLevel123Background();
        }
    }

    /**
     * Performs the show level123 background operation.
     */
    private void showLevel123Background() {
        if (backgroundImageLevel123 != null && backgroundImageLevel4 != null) {
            backgroundImageLevel123.setOpacity(1.0);
            backgroundImageLevel4.setOpacity(0.0);
        }
    }

    /**
     * Performs the show level4 background operation.
     */
    private void showLevel4Background() {
        if (backgroundImageLevel123 != null && backgroundImageLevel4 != null) {
            backgroundImageLevel123.setOpacity(0.0);
            backgroundImageLevel4.setOpacity(1.0);
        }
    }

    /**
     * Performs the transition to level4 background operation.
     */
    private void transitionToLevel4Background() {
        if (backgroundImageLevel123 == null || backgroundImageLevel4 == null) {
            return;
        }

        FadeTransition fadeOut = new FadeTransition(Duration.millis(1500), backgroundImageLevel123);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), backgroundImageLevel4);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeOut.play();
        fadeIn.play();
    }

    /**
     * Performs the transition to level123 background operation.
     */
    private void transitionToLevel123Background() {
        if (backgroundImageLevel123 == null || backgroundImageLevel4 == null) {
            return;
        }

        FadeTransition fadeOut = new FadeTransition(Duration.millis(1500), backgroundImageLevel4);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), backgroundImageLevel123);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeOut.play();
        fadeIn.play();
    }
}