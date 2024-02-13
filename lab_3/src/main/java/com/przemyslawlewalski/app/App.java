package com.przemyslawlewalski.app;

import java.util.Map;
import java.util.Scanner;

public class App {
    public static String cpuPlayerNickname = "CPU Player";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your nickname: ");
        String nickname = scanner.nextLine();

        PlayerScoreManager scoreManager = new PlayerScoreManager();
        Player player = loadOrCreatePlayer(scoreManager, nickname);
        Player cpuPlayer = loadOrCreatePlayer(scoreManager, cpuPlayerNickname);

        Game game = new Game(player, cpuPlayer);

        DifficultyLevel easy = new DifficultyLevel("Easy", 100);
        DifficultyLevel normal = new DifficultyLevel("Normal", 10000);
        DifficultyLevel hard = new DifficultyLevel("Hard", 1000000);

        System.out.println("Choose an option:");
        System.out.println("1. Play game");
        System.out.println("2. View stats");
        System.out.print("Enter your choice: ");
        int option = scanner.nextInt();

        if (option == 1) {
            System.out.println("Choose game mode:");
            System.out.println("1. Standard game");
            System.out.println("2. Reverse game");
            System.out.println("3. Mixed game");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            System.out.println("Choose difficulty level:");
            System.out.println("1. " + easy.getName());
            System.out.println("2. " + normal.getName());
            System.out.println("3. " + hard.getName());
            System.out.print("Enter your choice: ");
            int difficultyChoice = scanner.nextInt();
            DifficultyLevel chosenDifficulty = null;

            if (difficultyChoice == 1) {
                chosenDifficulty = easy;
            } else if (difficultyChoice == 2) {
                chosenDifficulty = normal;
            } else if (difficultyChoice == 3) {
                chosenDifficulty = hard;
            } else {
                System.out.println("Invalid choice. Please enter 1 for Easy, 2 for Normal, or 3 for Hard.");
            }

            if (choice == 1) {
                game.startClassicGame(chosenDifficulty, false);
            } else if (choice == 2) {
                game.startReverseGame(chosenDifficulty, false);
            } else if (choice == 3) {
                game.startMixedGame(chosenDifficulty);
            } else {
                System.out.println("Invalid choice. Please enter 1 for Standard game, 2 for Reverse game, or 3 for Mixed game.");
            }

            scoreManager.savePlayer(player);
            scoreManager.savePlayer(cpuPlayer);
            scanner.close();
        } else if (option == 2) {
            printPlayerStats(player);
        }
    }

    private static Player loadOrCreatePlayer(PlayerScoreManager scoreManager, String nickname) {
        Player player = scoreManager.loadPlayer(nickname);
        if (player == null) {
            player = new Player(nickname);
        }
        return player;
    }

    public static void printPlayerStats(Player player) {
        System.out.println("Stats for " + player.getNickname() + ":");
        for (Map.Entry<String, GameMode> entry : player.getSingle().entrySet()) {
            System.out.println("Single " + entry.getKey() + ":");
            for (Map.Entry<String, GameStats> statsEntry : entry.getValue().getDifficulty().entrySet()) {
                System.out.println("  Difficulty: " + statsEntry.getKey());
                System.out.println("    Record: " + statsEntry.getValue().getWins());
            }
        }
        for (Map.Entry<String, GameModeMulti> entry : player.getMulti().entrySet()) {
            System.out.println("Multi " + entry.getKey() + ":");
            for (Map.Entry<String, GameStats> statsEntry : entry.getValue().getDifficulty().entrySet()) {
                System.out.println("  Difficulty: " + statsEntry.getKey());
                System.out.println("    Wins: " + statsEntry.getValue().getWins());
                System.out.println("    Losses: " + statsEntry.getValue().getLoses());
            }
        }
    }
}
