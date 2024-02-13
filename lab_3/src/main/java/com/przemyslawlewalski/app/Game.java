package com.przemyslawlewalski.app;

import java.util.Scanner;

public class Game {
    private static final int MAX_ATTEMPTS = 10;
    private final Player player;
    private final Player cpuPlayer;
    private final PlayerScoreManager scoreManager;
    private int numberToGuess;

    public Game(Player player, Player cpuPlayer) {
        this.player = player;
        this.cpuPlayer = cpuPlayer;
        this.scoreManager = new PlayerScoreManager();
    }

    public void startClassicGame(DifficultyLevel difficulty, boolean isMulti) {
        while (true) {
            GameMode gameMode = player.getSingle().get("classic game");
            GameStats gameStats = gameMode.getDifficulty().get(difficulty.getName());
            if (gameStats == null) {
                gameStats = new GameStats();
                gameMode.getDifficulty().put(difficulty.getName(), gameStats);
            }
            int score = gameStats.getWins();
            System.out.println("Welcome to the game, " + player.getNickname() + "! Your best score is: " + score);
            System.out.println("Try to guess the number from 0 to " + difficulty.getMaxNumber() + ".");
            if (isMulti) {
                System.out.println("You have " + MAX_ATTEMPTS + " attempts.");
            }
            numberToGuess = (int) (Math.random() * difficulty.getMaxNumber() + 1);
            Scanner scanner = new Scanner(System.in);
            int attempts = 0;
            while (true) {
                System.out.print("Enter your guess: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a valid number. Please enter a number from 0 to " + difficulty.getMaxNumber() + ":");
                    scanner.next();
                }
                int guess = scanner.nextInt();
                if (guess < 0 || guess > difficulty.getMaxNumber()) {
                    System.out.println("The number is out of range. Please enter a number from 0 to " + difficulty.getMaxNumber() + ":");
                    continue;
                }
                attempts++;
                if (guess < numberToGuess) {
                    System.out.println("Too low! Try again.");
                } else if (guess > numberToGuess) {
                    System.out.println("Too high! Try again.");
                }
                if (isMulti && attempts > MAX_ATTEMPTS) {
                    System.out.println("You've reached the maximum number of attempts. You lost!");
                    GameStats playerGameStats = player.getMulti().get("classic game").getDifficulty().get(difficulty.getName());
                    if (playerGameStats == null) {
                        playerGameStats = new GameStats();
                        player.getMulti().get("classic game").getDifficulty().put(difficulty.getName(), playerGameStats);
                    }
                    playerGameStats.setLoses(playerGameStats.getLoses() + 1);

                    GameStats cpuPlayerGameStats = cpuPlayer.getMulti().get("classic game").getDifficulty().get(difficulty.getName());
                    if (cpuPlayerGameStats == null) {
                        cpuPlayerGameStats = new GameStats();
                        cpuPlayer.getMulti().get("classic game").getDifficulty().put(difficulty.getName(), cpuPlayerGameStats);
                    }
                    cpuPlayerGameStats.setWins(cpuPlayerGameStats.getWins() + 1);
                    break;
                } else if (guess == numberToGuess) {
                    System.out.println("Congratulations, you've guessed the number!");
                    if (attempts < score || score == 0) {
                        System.out.println("New best score: " + attempts);
                        gameStats.setWins(attempts);
                        scoreManager.savePlayer(player);
                    }
                    player.getMulti().get("classic game").getDifficulty().get(difficulty.getName()).setWins(gameStats.getWins() + 1);
                    cpuPlayer.getMulti().get("classic game").getDifficulty().get(difficulty.getName()).setLoses(gameStats.getLoses() + 1);
                    break;
                }
            }
            if (!isMulti) {
                break;
            }
            System.out.print("Do you want to play again? (yes/no): ");
            String playAgain = scanner.next();
            if (!playAgain.equalsIgnoreCase("yes")) {
                break;
            }
        }
    }

    public void startReverseGame(DifficultyLevel difficulty, boolean isMulti) {
        int seriesAttempts = 0;
        while (true) {
            GameMode gameMode = cpuPlayer.getMulti().get("reverse game");
            GameStats gameStats = gameMode.getDifficulty().get(difficulty.getName());
            if (gameStats == null) {
                gameStats = new GameStats();
                gameMode.getDifficulty().put(difficulty.getName(), gameStats);
            }
            int score = gameStats.getWins();
            System.out.println("Welcome to the reverse game, " + player.getNickname() + "! CPU's best score is: " + score);
            System.out.println("Please enter a number from 0 to " + difficulty.getMaxNumber() + ".");
            Scanner scanner = new Scanner(System.in);
            scanner.nextLine();

            int low = 0;
            int high = difficulty.getMaxNumber();
            int guess;
            int attempts = 0;

            while (true) {
                guess = (low + high) / 2;
                System.out.println("Is your number " + guess + "?");
                System.out.print("Type 'guessed' if I guessed, 'high' if my number is too high, or 'low' if my number is too low: ");
                String response = scanner.nextLine().toLowerCase();

                if (response.equals("guessed") || response.equals("high") || response.equals("low")) {
                    attempts++;
                    seriesAttempts++;
                } else {
                    System.out.println("I don't understand your response. Try again.");
                    continue;
                }

                if (isMulti && attempts > MAX_ATTEMPTS) {
                    System.out.println("I reached the maximum number of attempts. I lost!");
                    GameStats playerGameStats = player.getMulti().get("reverse game").getDifficulty().get(difficulty.getName());
                    if (playerGameStats == null) {
                        playerGameStats = new GameStats();
                        player.getMulti().get("reverse game").getDifficulty().put(difficulty.getName(), playerGameStats);
                    }
                    playerGameStats.setLoses(playerGameStats.getLoses() + 1);

                    GameStats cpuPlayerGameStats = cpuPlayer.getMulti().get("reverse game").getDifficulty().get(difficulty.getName());
                    if (cpuPlayerGameStats == null) {
                        cpuPlayerGameStats = new GameStats();
                        cpuPlayer.getMulti().get("reverse game").getDifficulty().put(difficulty.getName(), cpuPlayerGameStats);
                    }
                    cpuPlayerGameStats.setWins(cpuPlayerGameStats.getWins() + 1);
                    break;
                } else if (response.equals("guessed")) {
                    System.out.println("Hooray! I guessed your number!");
                    if (attempts < score || score == 0) {
                        System.out.println("New best score for CPU Player: " + attempts);
                        gameStats.setWins(attempts);
                        scoreManager.savePlayer(cpuPlayer);
                    }
                    player.getMulti().get("reverse game").getDifficulty().get(difficulty.getName()).setWins(gameStats.getWins() + 1);
                    cpuPlayer.getMulti().get("reverse game").getDifficulty().get(difficulty.getName()).setLoses(gameStats.getLoses() + 1);
                    break;
                } else if (response.equals("high")) {
                    high = guess - 1;
                } else if (response.equals("low")) {
                    low = guess + 1;
                }
            }

            if (seriesAttempts < cpuPlayer.getMulti().get("reverse game").getDifficulty().get(difficulty.getName()).getWins() || cpuPlayer.getMulti().get("reverse game").getDifficulty().get(difficulty.getName()).getWins() == 0) {
                cpuPlayer.getMulti().get("reverse game").getDifficulty().get(difficulty.getName()).setWins(seriesAttempts);
                scoreManager.savePlayer(cpuPlayer);
            }

            if (!isMulti) {
                break;
            }

            System.out.print("Do you want to play again? (yes/no): ");
            String playAgain = scanner.next();
            if (!playAgain.equalsIgnoreCase("yes")) {
                break;
            }
        }
    }

    public void startMixedGame(DifficultyLevel difficulty) {
        System.out.println("Welcome to the mixed game, " + player.getNickname() + "!");
        System.out.println("Please enter a number from 0 to " + difficulty.getMaxNumber() + " for me to guess.");
        Scanner scanner = new Scanner(System.in);
        int playerNumber = scanner.nextInt();

        System.out.println("Now I will choose a number for you to guess.");
        int cpuNumber = (int) (Math.random() * difficulty.getMaxNumber() + 1);

        System.out.println("Let's toss a coin to see who guesses first. 0 is for me and 1 is for you.");
        int coinToss = (int) (Math.random() * 2);
        System.out.println("The result of the coin toss is: " + coinToss);

        int low = 0;
        int high = difficulty.getMaxNumber();
        int guess;
        boolean playerTurn = (coinToss == 1);

        while (true) {
            if (playerTurn) {
                System.out.print("Enter your guess: ");
                guess = scanner.nextInt();
                if (guess == cpuNumber) {
                    System.out.println("Congratulations, you've guessed the my number! You won the game!");
                    break;
                } else if (guess < cpuNumber) {
                    System.out.println("Too low! Try again.");
                } else {
                    System.out.println("Too high! Try again.");
                }
            } else {
                guess = (low + high) / 2;
                System.out.println("My guess is: " + guess);
                if (guess == playerNumber) {
                    System.out.println("I guessed your number! I won the game!");
                    break;
                } else if (guess < playerNumber) {
                    System.out.println("My guess is too low!");
                    low = guess + 1;
                } else {
                    System.out.println("My guess is too high!");
                    high = guess - 1;
                }
            }
            playerTurn = !playerTurn;
        }
    }

}
