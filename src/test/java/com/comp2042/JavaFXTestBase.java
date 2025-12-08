package com.comp2042;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Base class for tests that require JavaFX toolkit initialization.
 * Extend this class or call initJavaFX() before running tests that use JavaFX components.
 */
public abstract class JavaFXTestBase {

    private static boolean javaFXInitialized = false;

    @BeforeAll
    static void initJavaFX() throws InterruptedException {
        if (javaFXInitialized) {
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(() -> {
                // JavaFX toolkit initialized
                latch.countDown();
            });
        } catch (IllegalStateException e) {
            // Toolkit already initialized
            latch.countDown();
        }

        // Wait for initialization with timeout
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Failed to initialize JavaFX toolkit");
        }

        javaFXInitialized = true;
    }

    /**
     * Run an action on the JavaFX Application Thread and wait for completion.
     */
    protected void runOnFXThreadAndWait(Runnable action) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        latch.await(5, TimeUnit.SECONDS);
    }
}
