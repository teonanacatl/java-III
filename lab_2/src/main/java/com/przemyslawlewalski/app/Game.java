package com.przemyslawlewalski.app;

import java.util.Scanner;

public class Game {
    private Player player;
    private PlayerScoreManager scoreManager;
    private int numberToGuess;

    public Game(Player player) {
        this.player = player;
        this.scoreManager = new PlayerScoreManager();
        this.numberToGuess = (int) (Math.random() * 101);
    }

    public void start() {
        int score = scoreManager.loadScore(player);
        System.out.println("Welcome to the game, " + player.getNickname() + "! Your best score is: " + score);
        System.out.println("Try to guess the number from 0 to 100.");

        Scanner scanner = new Scanner(System.in);
        int attempts = 0;

        while (true) {
            System.out.print("Enter your guess: ");
            int guess = scanner.nextInt();
            attempts++;

            if (guess < numberToGuess) {
                System.out.println("Too low!");
            } else if (guess > numberToGuess) {
                System.out.println("Too high!");
            } else {
                System.out.println("Congratulations, you've guessed the number!");
                if (attempts < score || score == 0) {
                    System.out.println("New best score: " + attempts);
                    scoreManager.saveScore(player, attempts);
                }
                break;
            }
        }
    }
}
