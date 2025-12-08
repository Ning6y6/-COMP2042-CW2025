package com.comp2042.logic;

import com.comp2042.event.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BrickRotatorTest {

    private BrickRotator rotator;
    private Brick brick;
    private BrickGenerator brickGenerator;

    @BeforeEach
    void setUp() {
        rotator = new BrickRotator();
        brickGenerator = new RandomBrickGenerator();
        brick = brickGenerator.getBrick();
        rotator.setBrick(brick);
    }

    @Test
    void testInitialShapeIndex() {
        int[][] shape = rotator.getCurrentShape();
        assertNotNull(shape);
    }

    @Test
    void testGetNextShape() {
        NextShapeInfo nextShape = rotator.getNextShape();
        
        assertNotNull(nextShape);
        assertNotNull(nextShape.getShape());
        assertTrue(nextShape.getPosition() >= 0);
    }

    @Test
    void testGetNextShapeCyclesThroughRotations() {
        int firstPosition = rotator.getNextShape().getPosition();
        rotator.setCurrentShape(firstPosition);
        
        NextShapeInfo second = rotator.getNextShape();
        rotator.setCurrentShape(second.getPosition());
        
        NextShapeInfo third = rotator.getNextShape();
        assertEquals(firstPosition, third.getPosition());
    }

    @Test
    void testSetCurrentShape() {
        int targetShape = 1;
        rotator.setCurrentShape(targetShape);
        
        NextShapeInfo next = rotator.getNextShape();
        assertNotEquals(targetShape, next.getPosition());
    }

    @Test
    void testSetCurrentShapeThrowsExceptionOnInvalidIndex() {
        assertThrows(IllegalArgumentException.class, () -> {
            rotator.setCurrentShape(-1);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            rotator.setCurrentShape(10);
        });
    }

    @Test
    void testSetBrick() {
        Brick newBrick = brickGenerator.getBrick();
        rotator.setBrick(newBrick);
        
        assertSame(newBrick, rotator.getBrick());
        int[][] shape = rotator.getCurrentShape();
        assertNotNull(shape);
    }

    @Test
    void testSetBrickThrowsExceptionOnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            rotator.setBrick(null);
        });
    }

    @Test
    void testGetCurrentShapeThrowsExceptionWhenBrickNotSet() {
        BrickRotator newRotator = new BrickRotator();
        
        assertThrows(IllegalStateException.class, () -> {
            newRotator.getCurrentShape();
        });
    }

    @Test
    void testGetNextShapeThrowsExceptionWhenBrickNotSet() {
        BrickRotator newRotator = new BrickRotator();
        
        assertThrows(IllegalStateException.class, () -> {
            newRotator.getNextShape();
        });
    }
}

