package com.przemyslawlewalski.app;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


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

    public void savePlayer(Player player) {
        String filename = player.getNickname() + ".ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SCORES_DIRECTORY + "/" + filename))) {
            oos.writeObject(player);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the player.");
            e.printStackTrace();
        }
    }

    public Player loadPlayer(String nickname) {
        String filename = nickname + ".ser";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SCORES_DIRECTORY + "/" + filename))) {
            return (Player) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            if (!nickname.equals(App.cpuPlayerNickname)) {
                System.out.println("No data found for this player. Creating new save file.");
            }
            return null;
        }
    }
}
