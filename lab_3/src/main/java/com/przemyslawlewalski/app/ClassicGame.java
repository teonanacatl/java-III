package com.przemyslawlewalski.app;

import java.util.Scanner;

public class ClassicGame extends Game {
    public ClassicGame(Player player, DifficultyLevel difficulty) {
        super(player, difficulty);
    }

    @Override
    public void startGame() {
        GameMode gameMode = player.getSingle().get("classic game");
        GameStats gameStats = gameMode.getDifficulty().get(difficulty.getName());
        if (gameStats == null) {
            gameStats = new GameStats();
            gameMode.getDifficulty().put(difficulty.getName(), gameStats);
        }
        int score = gameStats.getWins();
        System.out.println("Welcome to the game, " + player.getNickname() + "! Your best score is: " + score);
        do {
            System.out.println("Try to guess the number from " + difficulty.getStartValue() + " to " + difficulty.getEndValue() + ".");
            numberToGuess = (int) (Math.random() * difficulty.getEndValue() + 1);
            Scanner scanner = new Scanner(System.in);
            int attempts = 0;
            while (true) {
                System.out.print("Enter your guess: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a valid number. Please enter a number from " + difficulty.getStartValue() + " to " + difficulty.getEndValue() + ":");
                    scanner.next();
                }
                int guess = scanner.nextInt();
                if (guess < 0 || guess > difficulty.getEndValue()) {
                    System.out.println("The number is out of range. Please enter a number from " + difficulty.getStartValue() + " to " + difficulty.getEndValue() + ":");
                    continue;
                }
                attempts++;
                if (guess < numberToGuess) {
                    System.out.println("Too low! Try again.");
                } else if (guess > numberToGuess) {
                    System.out.println("Too high! Try again.");
                } else {
                    System.out.println("Congratulations, you've guessed the number!");
                    if (attempts < score || score == 0) {
                        System.out.println("New best score: " + attempts);
                        gameStats.setWins(attempts);
                        scoreManager.savePlayer(player);
                    }
                    break;
                }
            }
            askPlayAgain();
        } while (playAgain.equalsIgnoreCase("yes"));
    }
}
