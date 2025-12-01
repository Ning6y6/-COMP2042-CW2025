package com.comp2042.logic.bricks;

import com.comp2042.GameConstants;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomBrickGenerator implements BrickGenerator {

    private final List<Brick> brickList;

    private final Deque<Brick> nextBricks = new ArrayDeque<>();

    public RandomBrickGenerator() {
        brickList = new ArrayList<>();
        brickList.add(new IBrick());
        brickList.add(new JBrick());
        brickList.add(new LBrick());
        brickList.add(new OBrick());
        brickList.add(new SBrick());
        brickList.add(new TBrick());
        brickList.add(new ZBrick());
        
        fillQueue();
    }

    private void fillQueue() {
        while (nextBricks.size() < GameConstants.PREVIEW_COUNT){
            nextBricks.add(brickList.get(ThreadLocalRandom.current().nextInt(brickList.size())));
        }

    }

    @Override
    public Brick getBrick() {
        ensureQueueHasBricks();
        Brick brick = nextBricks.poll();
        if (brick == null) {
            throw new IllegalStateException("Failed to generate brick");
        }
        while (nextBricks.size() < GameConstants.PREVIEW_COUNT) {
            fillQueue();
        }
        return brick;
    }

    @Override
    public Brick getNextBrick() {
        ensureQueueHasBricks();
        Brick nextBrick = nextBricks.peek();
        if (nextBrick == null) {
            throw new IllegalStateException("Failed to generate next brick");
        }
        return nextBrick;
    }

    private void ensureQueueHasBricks() {
        if (nextBricks.isEmpty()) {
            fillQueue();
        }
    }

    @Override
    public List<Brick> getNextThreeBricks() {
        ensureQueueHasBricks();
        List<Brick> result = new ArrayList<>();
        int index = 0;
        for (Brick brick : nextBricks) {
            if (index >= GameConstants.PREVIEW_COUNT) break;
            if (brick == null) {
                throw new IllegalStateException("Null brick found in queue at index " + index);
            }
            result.add(brick);
            index++;
        }
        while (result.size() < GameConstants.PREVIEW_COUNT) {
            fillQueue();
            Brick additionalBrick = nextBricks.peek();
            if (additionalBrick != null && !result.contains(additionalBrick)) {
                result.add(additionalBrick);
            }
        }
        return result;
    }
}


