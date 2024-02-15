package com.przemyslawlewalski.app.game;

import com.przemyslawlewalski.app.difficulty.DifficultyLevel;
import com.przemyslawlewalski.app.player.Player;

import java.util.Scanner;

public class MixedGame extends Game {
    public MixedGame(Player player, DifficultyLevel difficulty) {
        super(player, difficulty);
    }

    @Override
    public void startGame() {
        System.out.println("Welcome to the mixed game, " + player.getNickname() + "!");
        System.out.println("We both need to guess the number from " + difficulty.getStartValue() + " to " + difficulty.getEndValue() + ".");
        do {
            numberToGuess = (int) (Math.random() * (difficulty.getEndValue() - difficulty.getStartValue() + 1)) + difficulty.getStartValue();
            System.out.println("Let's toss a coin to see who guesses first. Tails is for me and heads is for you.");

            int coinToss = (int) (Math.random() * 2);
            String coinFace = coinToss == 0 ? "Tails." : "Heads.";
            System.out.println("The result of the coin toss is: " + coinFace);

            Scanner scanner = new Scanner(System.in);

            int low = difficulty.getStartValue();
            int high = difficulty.getEndValue();

            boolean playerTurn = (coinToss == 1);
            if (playerTurn) {
                System.out.println("You start the game.");
            } else {
                System.out.println("I start the game.");
            }

            while (true) {

                if (playerTurn) {
                    System.out.print("Enter your guess: ");
                    int guess = scanner.nextInt();
                    if (guess == numberToGuess) {
                        System.out.println("Congratulations, you've guessed the number! You won the game!");
                        break;
                    } else if (guess < numberToGuess) {
                        System.out.println("Too low! Try again.");
                        low = Math.max(low, guess + 1);
                    } else {
                        System.out.println("Too high! Try again.");
                        high = Math.min(high, guess - 1);
                    }
                } else {
                    int guess = (low + high) / 2;
                    System.out.println("My guess is: " + guess);
                    if (guess == numberToGuess) {
                        System.out.println("I guessed your number! I won the game!");
                        break;
                    } else if (guess < numberToGuess) {
                        System.out.println("My guess is too low!");
                        low = guess + 1;
                    } else {
                        System.out.println("My guess is too high!");
                        high = guess - 1;
                    }
                }
                playerTurn = !playerTurn;
            }
            askPlayAgain();
        } while (playAgain.equalsIgnoreCase("yes"));
    }
}
