package com.przemyslawlewalski.app.game;

import com.przemyslawlewalski.app.difficulty.DifficultyLevel;
import com.przemyslawlewalski.app.game.mode.GameMode;
import com.przemyslawlewalski.app.game.stats.GameStats;
import com.przemyslawlewalski.app.player.Player;

import java.util.*;

public class MultiplayerGame extends Game {
    public MultiplayerGame(List<Player> players, DifficultyLevel difficulty) {
        super(players, difficulty);
    }

    @Override
    public void startGame() {
        System.out.println("Welcome to the multiplayer game!");
        Player winner = null;
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
                int maxAttempts = MAX_ATTEMPTS;
                if (player.isChampion()) {
                    player.grantNonTournamentPrivileges();
                    maxAttempts += 1;
                    System.out.println(player.getNickname() + ", as a champion, you get an extra guess! You now have " + maxAttempts + " attempts.");
                }
                attempts.put(player, 0);
            }

            boolean gameWon = false;
            guessingPlayers.sort(Comparator.comparing(Player::isLeader).reversed());
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
                            winner = player;
                            int nextPlayerIndex = (guessingPlayers.indexOf(player) + 1) % guessingPlayers.size();
                            Player nextPlayer = guessingPlayers.get(nextPlayerIndex);
                            System.out.println("As the winner of this round, " + player.getNickname() + " will choose the number in the next round!");
                            System.out.println("As the next player in line, " + nextPlayer.getNickname() + " will be the first to guess in the next round!");
                            player.setLeader(false);
                            nextPlayer.setLeader(true);

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
                winner = choosingPlayer;
            }
            endGame();
        } while (playAgain.equalsIgnoreCase("yes"));

        GameMode gameMode = winner.getSingle().get("Multiplayer game");
        GameStats gameStats = gameMode.getDifficulty().get(difficulty.getName());
        if (gameStats == null) {
            gameStats = new GameStats();
            gameMode.getDifficulty().put(difficulty.getName(), gameStats);
        }
        gameStats.setWins(gameStats.getWins() + 1);
        scoreManager.savePlayer(winner);

    }

    @Override
    public void endGame() {
        askPlayAgain();
    }
}






