package com.przemyslawlewalski.app.game;

import com.przemyslawlewalski.app.difficulty.DifficultyLevel;
import com.przemyslawlewalski.app.game.mode.GameMode;
import com.przemyslawlewalski.app.game.stats.GameStats;
import com.przemyslawlewalski.app.player.Player;

import java.util.*;

public class TournamentGame extends Game {
    private final String tournamentType;
    private Map<Player, Integer> wins;

    public TournamentGame(List<Player> players, DifficultyLevel difficulty, String tournamentType) {
        super(players, difficulty);
        this.tournamentType = tournamentType;
        this.wins = new HashMap<>();
        for (Player player : players) {
            if (player.getSingle().get("Tournament game") == null) {
                player.getSingle().put("Tournament game", new GameMode());
            }
        }
    }

    @Override
    public void startGame() {
        System.out.println("Welcome to the tournament game!");
        int gamesToWin = switch (tournamentType) {
            case "best-of-one" -> 1;
            case "best-of-three" -> 2;
            case "best-of-five" -> 3;
            default -> throw new IllegalArgumentException("Invalid tournament type");
        };
        wins = new HashMap<>();
        for (Player player : players) {
            wins.put(player, 0);
        }
        Player champion = null;
        int maxWins = 0;
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
            Map<Player, Integer> maxAttempts = new HashMap<>();
            for (Player player : guessingPlayers) {
                int attemptsLimit = MAX_ATTEMPTS;
                if (player.isChampion()) {
                    player.grantTournamentPrivileges();
                    attemptsLimit += 3;
                    System.out.println(player.getNickname() + ", as a champion, you get an extra guess! You now have " + attemptsLimit + " attempts.");
                }
                attempts.put(player, 0);
                maxAttempts.put(player, attemptsLimit);
            }

            boolean gameWon = false;
            guessingPlayers.sort(Comparator.comparing(Player::isLeader).reversed());
            while (!gameWon && Collections.max(attempts.values()) < Collections.max(maxAttempts.values())) {
                for (Player player : guessingPlayers) {
                    if (attempts.get(player) < maxAttempts.get(player)) {
                        System.out.println("It's " + player.getNickname() + "'s turn.");
                        System.out.print("Enter your guess: ");
                        guess = scanner.nextInt();
                        attempts.put(player, attempts.get(player) + 1);
                        if (guess == chosenNumber) {
                            System.out.println("Congratulations, " + player.getNickname() + "! You've guessed the number! You won the game!");
                            System.out.println("As the leader, you will be the first to guess in the next round!");
                            player.setLeader(true);
                            for (Player otherPlayer : guessingPlayers) {
                                if (!otherPlayer.equals(player)) {
                                    otherPlayer.setLeader(false);
                                }
                            }
                            gameWon = true;
                            wins.put(player, wins.get(player) + 1);
                            GameMode gameMode = player.getSingle().get("Tournament game");
                            GameStats gameStats = gameMode.getDifficulty().get(difficulty.getName());
                            if (gameStats == null) {
                                gameStats = new GameStats();
                                gameMode.getDifficulty().put(difficulty.getName(), gameStats);
                            }
                            gameStats.setWins(gameStats.getWins() + 1);
                            if (gameStats.getWins() > maxWins) {
                                maxWins = gameStats.getWins();
                                champion = player;
                            }
                            scoreManager.savePlayer(player);
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
                wins.put(choosingPlayer, wins.get(choosingPlayer) + 1);
                GameMode gameMode = choosingPlayer.getSingle().get("Tournament game");
                GameStats gameStats = gameMode.getDifficulty().get(difficulty.getName());
                if (gameStats == null) {
                    gameStats = new GameStats();
                    gameMode.getDifficulty().put(difficulty.getName(), gameStats);
                }
                gameStats.setWins(gameStats.getWins() + 1);
                if (gameStats.getWins() > maxWins) {
                    maxWins = gameStats.getWins();
                    champion = choosingPlayer;
                }
                scoreManager.savePlayer(choosingPlayer);
            }
        } while (Collections.max(wins.values()) < gamesToWin);

        if (champion != null && !champion.equals(player)) {
            player.setChampion(false);
            System.out.println(player.getNickname() + ", you have lost the champion status as you lost the tournament.");
        }

        System.out.println("Congratulations, " + Objects.requireNonNull(champion).getNickname() + "! You are the champion of the tournament!");
        champion.setChampion(true);
        endGame();
    }

    @Override
    public void endGame() {
        askPlayAgain();
    }
}




