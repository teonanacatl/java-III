package com.przemyslawlewalski.app.game;

import com.przemyslawlewalski.app.difficulty.DifficultyLevel;
import com.przemyslawlewalski.app.game.mode.GameMode;
import com.przemyslawlewalski.app.game.stats.GameStats;
import com.przemyslawlewalski.app.player.Player;

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
            int maxAttempts = MAX_ATTEMPTS;
            if (player.isChampion()) {
                player.grantNonTournamentPrivileges();
                maxAttempts += 1;
                System.out.println(player.getNickname() + ", as a champion, you get an extra guess! You now have " + maxAttempts + " attempts.");
            }
            while (true) {
                System.out.print("Enter your guess: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a valid number. Please enter a number from " + difficulty.getStartValue() + " to " + difficulty.getEndValue() + ":");
                    scanner.next();
                }
                int guess = scanner.nextInt();
                if (guess < difficulty.getStartValue() || guess > difficulty.getEndValue()) {
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
                if (attempts >= maxAttempts) {
                    System.out.println("You have exceeded the maximum number of attempts. You lose!");
                    gameStats.setLoses(gameStats.getLoses() + 1);
                    break;
                }
            }
            askPlayAgain();
        } while (playAgain.equalsIgnoreCase("yes"));
    }
}