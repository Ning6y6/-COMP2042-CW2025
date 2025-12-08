package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.GameController;
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
        
        // Initialize background images
        initializeBackgroundImages();
        
        // Initialize Helpers
        this.nextPieceView = new NextPieceView();
        this.nextPieceView.initialize(nextBrickPanel1, nextBrickPanel2, nextBrickPanel3);

        this.holdView = new HoldView();
        this.holdView.initialize(holdPanel);

        initGridNodes();

        scoreLabel.textProperty().bind(viewModel.scoreProperty());
        
        // Level display: Hide in endless mode, show challenge level in challenge mode
        if (levelHeaderLabel != null && levelLabel != null) {
            // Create a single binding for challenge mode check to reuse
            javafx.beans.binding.BooleanBinding isChallengeMode = Bindings.createBooleanBinding(
                () -> {
                    GameMode mode = viewModel.gameModeProperty().get();
                    return mode != null && mode == GameMode.CHALLENGE;
                },
                viewModel.gameModeProperty()
            );
            
            // Bind visibility to game mode - only show in challenge mode
            levelHeaderLabel.visibleProperty().bind(isChallengeMode);
            levelHeaderLabel.managedProperty().bind(levelHeaderLabel.visibleProperty());
            
            levelLabel.visibleProperty().bind(isChallengeMode);
            levelLabel.managedProperty().bind(levelLabel.visibleProperty());
            
            // Bind level label to challenge level property
            // This will automatically update when challenge level changes
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
        
        // Challenge Mode Timer
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
        
        // Listen for challenge level changes to update background
        viewModel.challengeLevelProperty().addListener((obs, oldLevel, newLevel) -> {
            if (viewModel.gameModeProperty().get() == GameMode.CHALLENGE) {
                handleChallengeLevelChange(oldLevel.intValue(), newLevel.intValue());
            }
        });
        
        // Set initial background based on game mode
        updateBackgroundForMode();

        // 2. Bind Main Board
        viewModel.boardGridProperty().addListener((obs, oldGrid, newGrid) -> renderBoard(newGrid));

        // 3. Bind Side Panels
        viewModel.nextPiecesProperty().addListener((obs, oldVal, newVal) ->
                nextPieceView.updateNextBrickPanels(newVal)
        );

        viewModel.holdPieceProperty().addListener((obs, oldVal, newVal) ->
                holdView.updateHoldPanel(newVal, viewModel.holdAllowedProperty().get())
        );

        // 4. Bind Overlays / Pause / Victory
        overlayPane.visibleProperty().bind(
                Bindings.or(viewModel.isPausedProperty(), 
                    Bindings.and(viewModel.isGameOverProperty(), 
                        viewModel.isVictoryProperty().not()))
        );
        
        // Victory overlay
        if (victoryPane != null) {
            victoryPane.visibleProperty().bind(
                Bindings.and(viewModel.isGameOverProperty(), viewModel.isVictoryProperty())
            );
        }

        // Update overlay text based on game state
        viewModel.isGameOverProperty().addListener((obs, wasGameOver, isGameOver) -> {
            if (isGameOver) {
                overlayLabel.setText("GAME OVER");
            }
        });
        viewModel.isPausedProperty().addListener((obs, wasPaused, isPaused) -> {
            if (isPaused && !viewModel.isGameOverProperty().get()) {
                overlayLabel.setText("PAUSED");
            }
        });

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
        // Board has rows 0-24 (25 rows total)
        // Hidden rows: 0-1 (HIDDEN_ROWS = 2) - these are above the visible area
        // Visible rows should show board rows 2-24, which maps to visual rows 0-22
        // So: visual row r displays board row (r + HIDDEN_ROWS)
        int offset = GameConstants.HIDDEN_ROWS;

        for (int r = 0; r < GameConstants.VISIBLE_ROWS; r++) {
            for (int c = 0; c < GameConstants.BOARD_WIDTH; c++) {
                int dataRow = r + offset;
                if (dataRow >= 0 && dataRow < gridData.length && gridData[dataRow] != null && c < gridData[dataRow].length) {
                    int typeId = gridData[dataRow][c];
                    updateCell(displayGridNodes[r][c], typeId);
                } else {
                    // Clear cells that are out of bounds
                    updateCell(displayGridNodes[r][c], 0);
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
        // Check if this is the pause key
        boolean wasPaused = viewModel.isPausedProperty().get();
        KeyCode pauseKey = controller.getInputManager().getKeyForCommand(PauseCommand.class);
        boolean isPauseKey = event.getCode() == pauseKey;
        
        // Delegate pure input to InputManager via Controller
        controller.getInputManager().handleInput(event.getCode(), controller);
        
        // If pause key was pressed and game is now paused, show Settings window
        if (isPauseKey && !wasPaused) {
            Platform.runLater(() -> {
                if (viewModel.isPausedProperty().get()) {
                    showSettingsWindow();
                }
            });
        }
        
        event.consume();
    }

    private void showNotification(String message) {
        NotificationPanel panel = new NotificationPanel(message);
        rootStackPane.getChildren().add(panel);
        panel.playAnimation();
    }

    @FXML
    public void openSettings() {
        // Pause the game first if not already paused
        boolean wasPaused = viewModel.isPausedProperty().get();
        
        if (!wasPaused) {
            // Pause the game first
            controller.handlePauseToggle();
            // Use Platform.runLater to ensure pause state is updated, then show settings
            Platform.runLater(() -> {
                if (viewModel.isPausedProperty().get()) {
                    showSettingsWindow();
                }
            });
        } else {
            // If already paused, just show settings immediately
            showSettingsWindow();
        }
    }
    
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
     * Initializes background images to fill the StackPane.
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
     * Updates background based on game mode.
     */
    private void updateBackgroundForMode() {
        GameMode mode = viewModel.gameModeProperty().get();
        if (mode == GameMode.ENDLESS) {
            // Endless mode: show level123 background
            showLevel123Background();
        } else if (mode == GameMode.CHALLENGE) {
            // Challenge mode: show based on current level
            int level = viewModel.challengeLevelProperty().get();
            if (level == 4) {
                showLevel4Background();
            } else {
                showLevel123Background();
            }
        }
    }
    
    /**
     * Handles challenge level changes and transitions background smoothly.
     */
    private void handleChallengeLevelChange(int oldLevel, int newLevel) {
        previousChallengeLevel = oldLevel;
        
        // Transition from level 3 to level 4
        if (oldLevel == 3 && newLevel == 4) {
            transitionToLevel4Background();
        } else if (newLevel < 4 && oldLevel == 4) {
            // Transition back to level123 if needed (shouldn't happen, but handle it)
            transitionToLevel123Background();
        }
    }
    
    /**
     * Shows level 1-3 background immediately.
     */
    private void showLevel123Background() {
        if (backgroundImageLevel123 != null && backgroundImageLevel4 != null) {
            backgroundImageLevel123.setOpacity(1.0);
            backgroundImageLevel4.setOpacity(0.0);
        }
    }
    
    /**
     * Shows level 4 background immediately.
     */
    private void showLevel4Background() {
        if (backgroundImageLevel123 != null && backgroundImageLevel4 != null) {
            backgroundImageLevel123.setOpacity(0.0);
            backgroundImageLevel4.setOpacity(1.0);
        }
    }
    
    /**
     * Smoothly transitions from level 1-3 background to level 4 background.
     */
    private void transitionToLevel4Background() {
        if (backgroundImageLevel123 == null || backgroundImageLevel4 == null) {
            return;
        }
        
        // Fade out level123 background
        FadeTransition fadeOut = new FadeTransition(Duration.millis(1500), backgroundImageLevel123);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        // Fade in level4 background
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), backgroundImageLevel4);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        // Start both transitions
        fadeOut.play();
        fadeIn.play();
    }
    
    /**
     * Smoothly transitions from level 4 background back to level 1-3 background.
     */
    private void transitionToLevel123Background() {
        if (backgroundImageLevel123 == null || backgroundImageLevel4 == null) {
            return;
        }
        
        // Fade out level4 background
        FadeTransition fadeOut = new FadeTransition(Duration.millis(1500), backgroundImageLevel4);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        // Fade in level123 background
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), backgroundImageLevel123);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        // Start both transitions
        fadeOut.play();
        fadeIn.play();
    }
}