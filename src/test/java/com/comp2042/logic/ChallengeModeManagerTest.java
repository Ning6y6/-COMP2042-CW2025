package com.comp2042.logic;

import com.comp2042.JavaFXTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChallengeModeManagerTest extends JavaFXTestBase {

    private ChallengeModeManager manager;
    private boolean levelCompleteCalled;
    private boolean victoryCalled;
    private boolean gameOverCalled;

    @BeforeEach
    void setUp() {
        manager = new ChallengeModeManager();
        levelCompleteCalled = false;
        victoryCalled = false;
        gameOverCalled = false;
        
        manager.setOnLevelCompleteCallback(() -> levelCompleteCalled = true);
        manager.setOnVictoryCallback(() -> victoryCalled = true);
        manager.setOnGameOverCallback(() -> gameOverCalled = true);
    }

    @Test
    void testInitialState() {
        assertFalse(manager.isActive());
        assertEquals(1, manager.currentLevelProperty().get());
        assertEquals("00:30", manager.timerDisplayProperty().get());
    }

    @Test
    void testStartChallenge() {
        manager.startChallenge(0);
        
        assertTrue(manager.isActive());
        assertEquals(ChallengeModeManager.ChallengeLevel.LEVEL_1, manager.getCurrentLevel());
        assertEquals(1.0, manager.getSpeedMultiplier());
        assertFalse(manager.isKeysReversed());
    }

    @Test
    void testLevel1ToLevel2Transition() {
        manager.startChallenge(0);
        long startTime = System.currentTimeMillis();
        
        manager.update(startTime + 30_001, 0);
        
        assertTrue(levelCompleteCalled);
        assertEquals(ChallengeModeManager.ChallengeLevel.LEVEL_2, manager.getCurrentLevel());
        assertEquals(0.75, manager.getSpeedMultiplier());
        assertFalse(manager.isKeysReversed());
    }

    @Test
    void testLevel2ToLevel3Transition() {
        manager.startChallenge(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(0);
        
        assertEquals(ChallengeModeManager.ChallengeLevel.LEVEL_3, manager.getCurrentLevel());
        assertEquals(0.75, manager.getSpeedMultiplier());
        assertTrue(manager.isKeysReversed());
    }

    @Test
    void testLevel3ToLevel4Transition() {
        manager.startChallenge(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(1000);
        
        assertEquals(ChallengeModeManager.ChallengeLevel.LEVEL_4, manager.getCurrentLevel());
        assertEquals(0.75, manager.getSpeedMultiplier());
        assertFalse(manager.isKeysReversed());
    }

    @Test
    void testLevel4VictoryCondition() {
        manager.startChallenge(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(1000);
        
        long startTime = System.currentTimeMillis();
        manager.update(startTime + 1000, 1500);
        
        assertTrue(victoryCalled);
        assertFalse(manager.isActive());
    }

    @Test
    void testLevel4GameOverTimeExpired() {
        manager.startChallenge(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(1000);
        
        long startTime = System.currentTimeMillis();
        manager.update(startTime + 60_001, 1499);
        
        assertTrue(gameOverCalled);
        assertFalse(manager.isActive());
    }

    @Test
    void testPlayerDeathBeforeTimeExpires() {
        manager.startChallenge(0);
        
        manager.handlePlayerDeath();
        
        assertTrue(gameOverCalled);
        assertFalse(manager.isActive());
    }

    @Test
    void testLevel1DeathBefore30Seconds() {
        manager.startChallenge(0);
        long startTime = System.currentTimeMillis();
        
        manager.update(startTime + 15_000, 0);
        manager.handlePlayerDeath();
        
        assertTrue(gameOverCalled);
        assertFalse(manager.isActive());
    }

    @Test
    void testLevel4ScoreRequirement() {
        manager.startChallenge(1000);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(1000);
        
        long startTime = System.currentTimeMillis();
        manager.update(startTime + 1000, 1501);
        
        assertTrue(victoryCalled);
    }

    @Test
    void testLevel4ScoreRequirementExact() {
        manager.startChallenge(1000);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(1000);
        
        long startTime = System.currentTimeMillis();
        manager.update(startTime + 1000, 1500);
        
        assertTrue(victoryCalled);
    }

    @Test
    void testReset() {
        manager.startChallenge(500);
        manager.advanceToNextLevel(0);
        
        manager.reset();
        
        assertFalse(manager.isActive());
        assertNull(manager.getCurrentLevel());
        assertEquals(1, manager.currentLevelProperty().get());
        assertEquals("00:30", manager.timerDisplayProperty().get());
    }

    @Test
    void testTimerDisplayFormat() {
        manager.startChallenge(0);
        
        manager.update(System.currentTimeMillis() + 10_000, 0);
        
        String timer = manager.timerDisplayProperty().get();
        assertTrue(timer.matches("\\d{2}:\\d{2}"));
    }

    @Test
    void testNoActionWhenInactive() {
        manager.handlePlayerDeath();
        assertFalse(gameOverCalled);
        
        manager.update(System.currentTimeMillis(), 0);
        assertFalse(levelCompleteCalled);
    }

    @Test
    void testAdvanceToLevel4DoesNotGoBeyond() {
        manager.startChallenge(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(0);
        manager.advanceToNextLevel(0);
        
        assertEquals(ChallengeModeManager.ChallengeLevel.LEVEL_4, manager.getCurrentLevel());
    }
}

