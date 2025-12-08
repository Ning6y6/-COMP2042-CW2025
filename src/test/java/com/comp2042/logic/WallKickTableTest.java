package com.comp2042.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WallKickTableTest {

    @Test
    void testOffsetsContainsFiveEntries() {
        assertEquals(5, WallKickTable.OFFSETS.length);
    }

    @Test
    void testFirstOffsetIsZeroZero() {
        assertArrayEquals(new int[]{0, 0}, WallKickTable.OFFSETS[0]);
    }

    @Test
    void testOffsetsContainPositiveXKick() {
        boolean hasPositiveX = false;
        for (int[] offset : WallKickTable.OFFSETS) {
            if (offset[0] > 0) {
                hasPositiveX = true;
                break;
            }
        }
        assertTrue(hasPositiveX, "Wall kick table should contain positive X offset");
    }

    @Test
    void testOffsetsContainNegativeXKick() {
        boolean hasNegativeX = false;
        for (int[] offset : WallKickTable.OFFSETS) {
            if (offset[0] < 0) {
                hasNegativeX = true;
                break;
            }
        }
        assertTrue(hasNegativeX, "Wall kick table should contain negative X offset");
    }

    @Test
    void testAllOffsetsHaveTwoElements() {
        for (int[] offset : WallKickTable.OFFSETS) {
            assertEquals(2, offset.length, "Each offset should have exactly 2 elements (x, y)");
        }
    }

    @Test
    void testOffsetsAreSymmetric() {
        boolean hasPlus1 = false;
        boolean hasMinus1 = false;
        boolean hasPlus2 = false;
        boolean hasMinus2 = false;

        for (int[] offset : WallKickTable.OFFSETS) {
            if (offset[0] == 1) hasPlus1 = true;
            if (offset[0] == -1) hasMinus1 = true;
            if (offset[0] == 2) hasPlus2 = true;
            if (offset[0] == -2) hasMinus2 = true;
        }

        assertTrue(hasPlus1 && hasMinus1, "Should have symmetric +1 and -1 X offsets");
        assertTrue(hasPlus2 && hasMinus2, "Should have symmetric +2 and -2 X offsets");
    }

    @Test
    void testAllYOffsetsAreZero() {
        for (int[] offset : WallKickTable.OFFSETS) {
            assertEquals(0, offset[1], "Y offset should be 0 for horizontal wall kicks");
        }
    }

    @Test
    void testExpectedOffsetValues() {
        assertArrayEquals(new int[]{0, 0}, WallKickTable.OFFSETS[0]);
        assertArrayEquals(new int[]{1, 0}, WallKickTable.OFFSETS[1]);
        assertArrayEquals(new int[]{-1, 0}, WallKickTable.OFFSETS[2]);
        assertArrayEquals(new int[]{2, 0}, WallKickTable.OFFSETS[3]);
        assertArrayEquals(new int[]{-2, 0}, WallKickTable.OFFSETS[4]);
    }
}
