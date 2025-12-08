package com.comp2042.event;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {

    @Test
    void testConstructorAndGetters() {
        int[][] brickData = {{1, 1}, {1, 1}};
        int[][] nextBrickData = {{0, 1}, {1, 1}};
        List<int[][]> nextThreeBricks = new ArrayList<>();
        nextThreeBricks.add(new int[][]{{1, 0}, {1, 1}});
        nextThreeBricks.add(new int[][]{{1, 1}, {0, 1}});
        nextThreeBricks.add(new int[][]{{1, 1}, {1, 0}});
        int[][] heldBrickData = {{1, 1, 1, 1}};

        ViewData viewData = new ViewData(brickData, 5, 3, 18, nextBrickData, nextThreeBricks, heldBrickData, true);

        assertEquals(5, viewData.getXPosition());
        assertEquals(3, viewData.getYPosition());
        assertEquals(18, viewData.getGhostYPosition());
        assertTrue(viewData.isHoldAllowed());
    }

    @Test
    void testGetBrickDataReturnsCopy() {
        int[][] brickData = {{1, 1}, {1, 1}};
        ViewData viewData = new ViewData(brickData, 0, 0, 0, null, new ArrayList<>(), null, true);

        int[][] returnedBrickData = viewData.getBrickData();
        returnedBrickData[0][0] = 99;

        assertNotSame(brickData, returnedBrickData);
        assertEquals(1, brickData[0][0]);
    }

    @Test
    void testGetNextBrickDataReturnsCopy() {
        int[][] nextBrickData = {{1, 0}, {1, 1}};
        ViewData viewData = new ViewData(null, 0, 0, 0, nextBrickData, new ArrayList<>(), null, true);

        int[][] returnedNextBrickData = viewData.getNextBrickData();
        returnedNextBrickData[0][0] = 99;

        assertNotSame(nextBrickData, returnedNextBrickData);
        assertEquals(1, nextBrickData[0][0]);
    }

    @Test
    void testGetNextThreeBricksDataReturnsDeepCopy() {
        List<int[][]> nextThreeBricks = new ArrayList<>();
        int[][] brick1 = {{1, 0}, {1, 1}};
        nextThreeBricks.add(brick1);

        ViewData viewData = new ViewData(null, 0, 0, 0, null, nextThreeBricks, null, true);

        List<int[][]> returnedList = viewData.getNextThreeBricksData();
        returnedList.get(0)[0][0] = 99;

        assertNotSame(nextThreeBricks, returnedList);
        assertEquals(1, brick1[0][0]);
    }

    @Test
    void testGetHeldBrickDataReturnsCopy() {
        int[][] heldBrickData = {{1, 1, 1, 1}};
        ViewData viewData = new ViewData(null, 0, 0, 0, null, new ArrayList<>(), heldBrickData, true);

        int[][] returnedHeldBrickData = viewData.getHeldBrickData();
        returnedHeldBrickData[0][0] = 99;

        assertNotSame(heldBrickData, returnedHeldBrickData);
        assertEquals(1, heldBrickData[0][0]);
    }

    @Test
    void testGetHeldBrickDataReturnsNullWhenNotSet() {
        ViewData viewData = new ViewData(null, 0, 0, 0, null, new ArrayList<>(), null, true);

        assertNull(viewData.getHeldBrickData());
    }

    @Test
    void testHoldAllowedFalse() {
        ViewData viewData = new ViewData(null, 0, 0, 0, null, new ArrayList<>(), null, false);

        assertFalse(viewData.isHoldAllowed());
    }

    @Test
    void testGhostPositionBelowActivePosition() {
        ViewData viewData = new ViewData(null, 5, 2, 20, null, new ArrayList<>(), null, true);

        assertTrue(viewData.getGhostYPosition() >= viewData.getYPosition());
    }

    @Test
    void testNextThreeBricksDataSize() {
        List<int[][]> nextThreeBricks = new ArrayList<>();
        nextThreeBricks.add(new int[][]{{1}});
        nextThreeBricks.add(new int[][]{{2}});
        nextThreeBricks.add(new int[][]{{3}});

        ViewData viewData = new ViewData(null, 0, 0, 0, null, nextThreeBricks, null, true);

        assertEquals(3, viewData.getNextThreeBricksData().size());
    }
}
