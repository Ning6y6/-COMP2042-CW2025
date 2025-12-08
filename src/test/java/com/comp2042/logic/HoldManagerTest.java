package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class HoldManagerTest {

    private HoldManager holdManager;
    private Brick brick1;
    private Brick brick2;
    private Brick brick3;

    @BeforeEach
    void setUp() {
        holdManager = new HoldManager();
        // Use mock bricks for deterministic testing
        brick1 = Mockito.mock(Brick.class);
        brick2 = Mockito.mock(Brick.class);
        brick3 = Mockito.mock(Brick.class);

        // Setup minimal behavior for mocks
        Mockito.lenient().when(brick1.getShapeMatrix()).thenReturn(Collections.singletonList(new int[][]{{1}}));
        Mockito.lenient().when(brick2.getShapeMatrix()).thenReturn(Collections.singletonList(new int[][]{{2}}));
        Mockito.lenient().when(brick3.getShapeMatrix()).thenReturn(Collections.singletonList(new int[][]{{3}}));
    }

    @Test
    void testInitialState() {
        assertNull(holdManager.getHeldBrick());
        assertTrue(holdManager.isCanSwap());
    }

    @Test
    void testFirstSwapStoresBrick() {
        Optional<Brick> result = holdManager.swap(brick1);
        
        assertTrue(result.isEmpty());
        assertSame(brick1, holdManager.getHeldBrick());
        assertFalse(holdManager.isCanSwap());
    }

    @Test
    void testSecondSwapReturnsHeldBrick() {
        // First swap stores brick1, canSwap becomes false
        holdManager.swap(brick1);

        // Reset flag to allow another swap (simulates a new piece being placed)
        holdManager.resetSwapFlag();

        // Second swap should exchange brick2 with brick1
        Optional<Brick> result = holdManager.swap(brick2);

        assertTrue(result.isPresent());
        assertSame(brick1, result.get());
        assertSame(brick2, holdManager.getHeldBrick());
        assertFalse(holdManager.isCanSwap());
    }

    @Test
    void testSwapWhenCanSwapIsFalse() {
        // First swap stores brick1, canSwap becomes false
        holdManager.swap(brick1);

        // Since canSwap is false, attempting to swap brick3 should return brick3 back
        // (the swap is blocked)
        assertFalse(holdManager.isCanSwap());

        Optional<Brick> blockedSwap = holdManager.swap(brick3);

        // When canSwap is false, the implementation returns the input brick
        assertTrue(blockedSwap.isPresent());
        assertSame(brick3, blockedSwap.get());

        // The held brick should still be brick1 (unchanged)
        assertSame(brick1, holdManager.getHeldBrick());
    }

    @Test
    void testResetSwapFlag() {
        holdManager.swap(brick1);
        assertFalse(holdManager.isCanSwap());
        
        holdManager.resetSwapFlag();
        assertTrue(holdManager.isCanSwap());
    }

    @Test
    void testReset() {
        holdManager.swap(brick1);
        holdManager.reset();
        
        assertNull(holdManager.getHeldBrick());
        assertTrue(holdManager.isCanSwap());
    }

    @Test
    void testMultipleSwaps() {
        holdManager.swap(brick1);
        holdManager.resetSwapFlag();
        Optional<Brick> result1 = holdManager.swap(brick2);
        assertSame(brick1, result1.get());

        holdManager.resetSwapFlag();
        Optional<Brick> result2 = holdManager.swap(brick3);
        assertSame(brick2, result2.get());

        assertSame(brick3, holdManager.getHeldBrick());
    }
}

