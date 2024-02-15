package com.przemyslawlewalski.app;

import com.przemyslawlewalski.app.difficulty.AdvancedDifficulty;
import com.przemyslawlewalski.app.difficulty.DifficultyLevel;
import com.przemyslawlewalski.app.game.*;
import com.przemyslawlewalski.app.game.mode.GameMode;
import com.przemyslawlewalski.app.game.mode.GameModeMulti;
import com.przemyslawlewalski.app.game.stats.GameStats;
import com.przemyslawlewalski.app.player.Player;
import com.przemyslawlewalski.app.player.PlayerScoreManager;

import java.util.*;
import java.util.function.Supplier;

public class App {
    public static final String cpuPlayerNickname = "CPU Player";

    public static void main(String[] args) {
        System.out.println("Welcome to my number guessing game!");
        System.out.println("Are you ready for the challenge?");
        System.out.println("Can you beat the AI or your friends?");
        System.out.println("Let's test it!");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Would you like to engage in a real-time battle with your friends or test your skills against the AI?");
        String opponentType;
        do {
            System.out.print("Enter 'friends' or 'ai': ");
            opponentType = scanner.nextLine().toLowerCase();
        } while (!opponentType.equals("friends") && !opponentType.equals("ai"));

        PlayerScoreManager scoreManager = new PlayerScoreManager();
        List<Player> players = new ArrayList<>();

        if (opponentType.equalsIgnoreCase("friends")) {
            int numPlayers;
            do {
                System.out.print("Enter the number of players: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("That's not a valid number.");
                    System.out.println("Enter the number of players: ");
                    scanner.next();
                }
                numPlayers = scanner.nextInt();
            } while (numPlayers <= 0);
            scanner.nextLine();

            for (int i = 0; i < numPlayers; i++) {
                System.out.print("Enter nickname for player " + (i + 1) + ": ");
                String nickname = scanner.nextLine();
                Player player = loadOrCreatePlayer(scoreManager, nickname);
                players.add(player);
            }
        } else {
            System.out.print("Enter your nickname: ");
            String nickname = scanner.nextLine();
            Player player = loadOrCreatePlayer(scoreManager, nickname);
            Player cpuPlayer = loadOrCreatePlayer(scoreManager, cpuPlayerNickname);
            players.add(player);
            players.add(cpuPlayer);
        }

        DifficultyLevel easy = new DifficultyLevel("Easy", 0, 100);
        DifficultyLevel normal = new DifficultyLevel("Normal", 0, 10000);
        DifficultyLevel hard = new DifficultyLevel("Hard", 0, 1000000);
        AdvancedDifficulty custom;
        DifficultyLevel chosenDifficulty = null;
        String tournamentType;

        System.out.println("Choose an option:");
        System.out.println("1. Play game");
        System.out.println("2. View stats");
        System.out.print("Enter your choice: ");
        int option = scanner.nextInt();

        if (option == 1) {
            Map<String, Supplier<Game>> gameModes = getStringSupplierMap(opponentType, players);
            System.out.println("Choose game mode:");
            int index = 1;
            List<String> gameModeKeys = new ArrayList<>(gameModes.keySet());
            for (String gameMode : gameModeKeys) {
                System.out.println(index++ + ". " + gameMode);
            }
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            System.out.println("Choose difficulty level:");
            System.out.println("1. " + easy.getName());
            System.out.println("2. " + normal.getName());
            System.out.println("3. " + hard.getName());
            System.out.println("4. Custom");
            System.out.print("Enter your choice: ");
            int difficultyChoice = scanner.nextInt();

            if (difficultyChoice == 1) {
                chosenDifficulty = easy;
            } else if (difficultyChoice == 2) {
                chosenDifficulty = normal;
            } else if (difficultyChoice == 3) {
                chosenDifficulty = hard;
            } else if (difficultyChoice == 4) {
                System.out.print("Enter the lower bound: ");
                int lowerBound = scanner.nextInt();
                System.out.print("Enter the upper bound: ");
                int upperBound = scanner.nextInt();
                custom = new AdvancedDifficulty("Custom", lowerBound, upperBound);
                chosenDifficulty = custom;
            }

            if (gameModeKeys.get(choice - 1).equals("Tournament")) {
                int tournamentChoice;
                do {
                    System.out.println("Choose tournament type:");
                    System.out.println("1. Best-of-one");
                    System.out.println("2. Best-of-three");
                    System.out.println("3. Best-of-five");
                    System.out.print("Enter your choice: ");
                    tournamentChoice = scanner.nextInt();
                } while (tournamentChoice < 1 || tournamentChoice > 3);

                tournamentType = switch (tournamentChoice) {
                    case 1 -> "best-of-one";
                    case 2 -> "best-of-three";
                    case 3 -> "best-of-five";
                    default -> throw new IllegalStateException("Invalid choice: " + tournamentChoice);
                };

                Game game = new TournamentGame(players, chosenDifficulty, tournamentType);
                game.startGame();
            } else {
                Game game = gameModes.get(gameModeKeys.get(choice - 1)).get();
                game.setDifficulty(chosenDifficulty);
                game.startGame();
            }

            for (Player player : players) {
                scoreManager.savePlayer(player);
            }

            scanner.close();
        } else if (option == 2) {
            printPlayerStats(players);
        }
    }

    private static Map<String, Supplier<Game>> getStringSupplierMap(String opponentType, List<Player> players) {
        Map<String, Supplier<Game>> gameModes = new HashMap<>();
        if (opponentType.equalsIgnoreCase("ai")) {
            gameModes.put("Standard game", () -> new ClassicGame(players.getFirst(), null));
            gameModes.put("Reverse game", () -> new ReverseGame(players.getFirst(), null));
            gameModes.put("Mixed game", () -> new MixedGame(players.getFirst(), null));
        }
        if (opponentType.equalsIgnoreCase("friends")) {
            gameModes.put("Multiplayer game", () -> new MultiplayerGame(players, null));
            gameModes.put("Tournament", () -> new TournamentGame(players, null, null));
        }
        return gameModes;
    }

    private static Player loadOrCreatePlayer(PlayerScoreManager scoreManager, String nickname) {
        Player player = scoreManager.loadPlayer(nickname);
        if (player == null) {
            player = new Player(nickname);
        }
        return player;
    }

    public static void printPlayerStats(List<Player> players) {
        for (Player player : players) {
            System.out.println("\nStats for player: " + player.getNickname());
            System.out.println("Is the player a champion? " + (player.isChampion() ? "Yes" : "No"));
            System.out.println("\nSingle player games:");
            for (Map.Entry<String, GameMode> entry : player.getSingle().entrySet()) {
                System.out.println("  Game mode: " + entry.getKey());
                for (Map.Entry<String, GameStats> statsEntry : entry.getValue().getDifficulty().entrySet()) {
                    System.out.println("    Difficulty level: " + statsEntry.getKey());
                    System.out.println("      Wins: " + statsEntry.getValue().getWins());
                    System.out.println("      Losses: " + statsEntry.getValue().getLoses());
                    System.out.println("      Record: " + statsEntry.getValue().getWins());
                }
            }
            System.out.println("\nMultiplayer games:");
            for (Map.Entry<String, GameModeMulti> entry : player.getMulti().entrySet()) {
                System.out.println("  Game mode: " + entry.getKey());
                for (Map.Entry<String, GameStats> statsEntry : entry.getValue().getDifficulty().entrySet()) {
                    System.out.println("    Difficulty level: " + statsEntry.getKey());
                    System.out.println("      Wins: " + statsEntry.getValue().getWins());
                    System.out.println("      Losses: " + statsEntry.getValue().getLoses());
                }
            }
        }
    }

}
