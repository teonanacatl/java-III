package com.przemyslawlewalski.app;

import java.util.Scanner;

public class ReverseGame extends Game {
    public ReverseGame(Player player, DifficultyLevel difficulty) {
        super(player, difficulty);
    }

    @Override
    public void startGame() {
        GameMode gameMode = player.getSingle().get("reverse game");
        GameStats gameStats = gameMode.getDifficulty().get(difficulty.getName());
        if (gameStats == null) {
            gameStats = new GameStats();
            gameMode.getDifficulty().put(difficulty.getName(), gameStats);
        }
        int score = gameStats.getWins();
        System.out.println("Welcome to the reverse game, " + player.getNickname() + "! Your best score is: " + score);
        do {
            System.out.println("Please enter a number from " + difficulty.getStartValue() + " to " + difficulty.getEndValue() + ".");
            Scanner scanner = new Scanner(System.in);
            numberToGuess = scanner.nextInt();
            scanner.nextLine();

            int low = difficulty.getStartValue();
            int high = difficulty.getEndValue();
            int guess;
            int attempts = 0;

            while (true) {
                guess = (low + high) / 2;
                System.out.println("Is your number " + guess + "?");
                System.out.print("Type 'guessed' if I guessed, 'high' if my number is too high, or 'low' if my number is too low: ");
                String response = scanner.nextLine().toLowerCase();

                if (response.equals("guessed") || response.equals("high") || response.equals("low")) {
                    attempts++;
                } else {
                    System.out.println("I don't understand your response. Try again.");
                    continue;
                }

                if (attempts > MAX_ATTEMPTS) {
                    System.out.println("I reached the maximum number of attempts. You won!");
                    gameStats.setLoses(gameStats.getLoses() + 1);
                    break;
                } else if (response.equals("guessed")) {
                    System.out.println("Hooray! I guessed your number!");
                    if (attempts < score || score == 0) {
                        System.out.println("New best score: " + attempts);
                        gameStats.setWins(attempts);
                        scoreManager.savePlayer(player);
                    }
                    break;
                } else if (response.equals("high")) {
                    high = guess - 1;
                } else {
                    low = guess + 1;
                }
            }
            askPlayAgain();
        } while (playAgain.equalsIgnoreCase("yes"));
    }

}

