package com.przemyslawlewalski.app.game;

import com.przemyslawlewalski.app.player.Player;
import com.przemyslawlewalski.app.difficulty.DifficultyLevel;

import java.util.*;

public class MultiplayerGame extends Game {
    public MultiplayerGame(List<Player> players, DifficultyLevel difficulty) {
        super(players, difficulty);
    }

    @Override
    public void startGame() {
        System.out.println("Welcome to the multiplayer game!");
        do {
            Random random = new Random();
            Player choosingPlayer = players.get(random.nextInt(players.size()));
            System.out.println("It's " + choosingPlayer.getNickname() + "'s turn to choose a number from " + difficulty.getStartValue() + " to " + difficulty.getEndValue() + " for others to guess.");
            Scanner scanner = new Scanner(System.in);
            int chosenNumber = scanner.nextInt();

            System.out.println("Now each player will try to guess the number.");
            System.out.println("You all have " + MAX_ATTEMPTS + " attempts.");
            int guess;

            List<Player> guessingPlayers = new ArrayList<>(players);
            guessingPlayers.remove(choosingPlayer);

            Map<Player, Integer> attempts = new HashMap<>();
            for (Player player : guessingPlayers) {
                attempts.put(player, 0);
            }

            boolean gameWon = false;
            while (!gameWon && Collections.max(attempts.values()) < MAX_ATTEMPTS) {
                for (Player player : guessingPlayers) {
                    if (attempts.get(player) < MAX_ATTEMPTS) {
                        System.out.println("It's " + player.getNickname() + "'s turn.");
                        System.out.print("Enter your guess: ");
                        guess = scanner.nextInt();
                        attempts.put(player, attempts.get(player) + 1);
                        if (guess == chosenNumber) {
                            System.out.println("Congratulations, " + player.getNickname() + "! You've guessed the number! You won the game!");
                            gameWon = true;
                            askPlayAgain();
                            break;
                        } else if (guess < chosenNumber) {
                            System.out.println("Too low!");
                        } else {
                            System.out.println("Too high!");
                        }
                    }
                }
            }
            if (!gameWon) {
                System.out.println("No one guessed the number. The player who chose the number, " + choosingPlayer.getNickname() + ", wins!");
            }
            askPlayAgain();
        } while (playAgain.equalsIgnoreCase("yes"));
    }
}

