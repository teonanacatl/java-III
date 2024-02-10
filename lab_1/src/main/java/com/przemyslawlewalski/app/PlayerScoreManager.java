package com.przemyslawlewalski.app;

import java.io.*;
import java.nio.file.*;

public class PlayerScoreManager {
    private static final String SCORES_DIRECTORY = "scores";

    public PlayerScoreManager() {
        try {
            Files.createDirectories(Paths.get(SCORES_DIRECTORY));
        } catch (IOException e) {
            System.out.println("An error occurred while creating the scores directory.");
            e.printStackTrace();
        }
    }

    public void saveScore(Player player, int score) {
        try (PrintWriter out = new PrintWriter(new FileWriter(SCORES_DIRECTORY + "/" + player.getNickname() + ".txt"))) {
            out.println(score);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the score.");
            e.printStackTrace();
        }
    }

    public int loadScore(Player player) {
        try {
            String scoreString = new String(Files.readAllBytes(Paths.get(SCORES_DIRECTORY + "/" + player.getNickname() + ".txt")));
            return Integer.parseInt(scoreString.trim()); // trim the string before parsing
        } catch (IOException e) {
            System.out.println("No previous score found for this player.");
            return 0;
        }
    }
}
