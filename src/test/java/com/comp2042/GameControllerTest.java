package com.comp2042;

import com.comp2042.logic.Board;
import com.comp2042.logic.GameMode;
import com.comp2042.logic.Score;
import com.comp2042.state.PlayingState;
import com.comp2042.event.ClearRow;
import com.comp2042.event.ViewData;
import com.comp2042.viewmodel.GameViewModel;
import com.comp2042.input.InputManager;
import com.comp2042.leaderboard.LeaderboardService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest extends JavaFXTestBase {

    @Mock
    private Board board;

    @Mock
    private GameViewModel viewModel;

    @Mock
    private InputManager inputManager;

    @Mock
    private LeaderboardService leaderboardService;

    @Mock
    private GameLoop gameLoop;

    @Mock
    private Score score;

    @Mock
    private ViewData mockViewData;

    @Mock
    private ClearRow mockClearRow;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        // Setup ViewData mock to return valid data
        lenient().when(mockViewData.getBrickData()).thenReturn(new int[][]{{1}});
        lenient().when(mockViewData.getNextThreeBricksData()).thenReturn(new ArrayList<>());
        lenient().when(mockViewData.getHeldBrickData()).thenReturn(null);
        lenient().when(mockViewData.isHoldAllowed()).thenReturn(true);
        lenient().when(mockViewData.getXPosition()).thenReturn(0);
        lenient().when(mockViewData.getYPosition()).thenReturn(0);

        // Setup ClearRow mock
        lenient().when(mockClearRow.getLinesRemoved()).thenReturn(0);

        // Stub methods for the 'board' mock to prevent NullPointerExceptions
        lenient().when(board.getBoardMatrix()).thenReturn(new int[25][10]);
        lenient().when(board.getViewData()).thenReturn(mockViewData);
        lenient().when(board.createNewBrick()).thenReturn(false);
        lenient().when(board.moveBrickDown()).thenReturn(true);
        lenient().when(board.rotateLeftBrick()).thenReturn(true);
        lenient().when(board.clearRows()).thenReturn(mockClearRow);
        lenient().when(board.getScore()).thenReturn(score);
        lenient().when(score.getScore()).thenReturn(0);

        // Initialize the GameController with mocked dependencies
        gameController = new GameController(board, viewModel, inputManager, leaderboardService, gameLoop, GameMode.ENDLESS);
    }

    @Test
    void start() {
        gameController.start();

        verify(board).newGame();
        verify(board).createNewBrick();
        verify(viewModel, times(2)).setGameMode(GameMode.ENDLESS);
        verify(gameLoop).setSpeed(GameConstants.GAME_SPEED_MILLIS);
        verify(gameLoop).start();
    }

    @Test
    void pauseAndResumeGame() {
        try (MockedStatic<com.comp2042.ui.AudioManager> audioManagerMock = mockStatic(com.comp2042.ui.AudioManager.class)) {
            com.comp2042.ui.AudioManager mockAudioManager = mock(com.comp2042.ui.AudioManager.class);
            audioManagerMock.when(com.comp2042.ui.AudioManager::getInstance).thenReturn(mockAudioManager);

            gameController.pauseGame();

            verify(gameLoop).stop();
            verify(viewModel).setPaused(true);

            gameController.resumeGame();
            verify(gameLoop).start();  // resumeGame calls start() once
            verify(viewModel).setPaused(false);
        }
    }
    
    @Test
    void restartGame() {
        gameController.restartGame();

        verify(board).newGame();
        verify(board).createNewBrick();
        verify(viewModel).setGameOver(false);
        verify(viewModel).setPaused(false);
        verify(viewModel).setVictory(false);
        verify(gameLoop).setSpeed(GameConstants.GAME_SPEED_MILLIS);  // restartGame calls setSpeed once
        verify(gameLoop).start();
    }
    
    @Test
    void moveLeft() {
        gameController.setState(new PlayingState());
        gameController.moveLeft();
        verify(board).moveBrickLeft();
    }
    
    @Test
    void moveRight() {
        gameController.setState(new PlayingState());
        gameController.moveRight();
        verify(board).moveBrickRight();
    }
    
    @Test
    void rotate() {
        gameController.setState(new PlayingState());
        gameController.rotate();
        verify(board).rotateLeftBrick();
    }
    
    @Test
    void softDrop() {
        gameController.setState(new PlayingState());
        when(board.moveBrickDown()).thenReturn(true);
        gameController.softDrop();
        verify(board).moveBrickDown();
        verify(score).add(1);
    }
    
    @Test
    void hardDrop() {
        gameController.setState(new PlayingState());
        gameController.hardDrop();
        verify(board).hardDrop();
    }
    
    @Test
    void hold() {
        gameController.setState(new PlayingState());
        gameController.hold();
        verify(board).holdBrick();
    }
}
