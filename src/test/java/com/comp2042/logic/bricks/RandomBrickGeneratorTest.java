package com.comp2042.logic.bricks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RandomBrickGeneratorTest {

    private RandomBrickGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomBrickGenerator();
    }

    @Test
    void getBrick() {
        assertNotNull(generator.getBrick());
    }

    @Test
    void getNextBrick() {
        assertNotNull(generator.getNextBrick());
    }

    @Test
    void getNextThreeBricks() {
        List<Brick> nextThree = generator.getNextThreeBricks();
        assertEquals(3, nextThree.size());
        for (Brick brick : nextThree) {
            assertNotNull(brick);
        }
    }

    @Test
    void generatedBricksAreRandom() {
        List<Brick> bricks = IntStream.range(0, 14)
                .mapToObj(i -> generator.getBrick())
                .collect(Collectors.toList());

        long distinctBricks = bricks.stream()
                .map(brick -> brick.getClass().getSimpleName())
                .distinct()
                .count();

        assertTrue(distinctBricks > 1, "Generator should produce more than one type of brick");
    }
}