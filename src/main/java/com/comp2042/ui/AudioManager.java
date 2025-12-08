package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioManager {

    private MediaPlayer bgmPlayer;
    private static AudioManager instance;
    private double currentVolume = 0.5;

    private AudioManager() {

    }

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playBGM() {
        try {

            stopBGM();

            String bgmPath = getClass().getResource("/assets/audio/bgm.mp3").toExternalForm();
            Media bgmMedia = new Media(bgmPath);
            bgmPlayer = new MediaPlayer(bgmMedia);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(currentVolume);
            bgmPlayer.play();
        } catch (Exception e) {
            System.err.println("Failed to load BGM: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;
        }
    }

    public void pauseBGM() {
        if (bgmPlayer != null && bgmPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            bgmPlayer.pause();
        }
    }

    public void resumeBGM() {
        if (bgmPlayer == null) {

            playBGM();
        } else {
            MediaPlayer.Status status = bgmPlayer.getStatus();
            if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.STOPPED) {
                bgmPlayer.play();
            } else if (status == MediaPlayer.Status.READY) {

                bgmPlayer.play();
            }
        }
    }

    public void setVolume(double volume) {
        currentVolume = Math.max(0.0, Math.min(1.0, volume));
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(currentVolume);
        }
    }

    public double getVolume() {
        return currentVolume;
    }
}