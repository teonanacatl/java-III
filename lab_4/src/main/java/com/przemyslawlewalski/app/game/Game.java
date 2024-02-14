package com.przemyslawlewalski.app.game;

import com.przemyslawlewalski.app.player.Player;
import com.przemyslawlewalski.app.player.PlayerScoreManager;
import com.przemyslawlewalski.app.difficulty.DifficultyLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Game {
    protected static final int MAX_ATTEMPTS = 10;
    protected final List<Player> players;
    protected final PlayerScoreManager scoreManager;
    protected Player player;
    protected DifficultyLevel difficulty;
    protected int numberToGuess;
    protected String playAgain;

    public Game(Player player, DifficultyLevel difficulty) {
        this.player = player;
        this.difficulty = difficulty;
        this.players = new ArrayList<>();
        this.players.add(player);
        this.scoreManager = new PlayerScoreManager();
    }

    public Game(List<Player> players, DifficultyLevel difficulty) {
        this.players = players;
        this.player = players.get(0);
        this.difficulty = difficulty;
        this.scoreManager = new PlayerScoreManager();
    }

    public abstract void startGame();

    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }

    public void askPlayAgain() {
        System.out.print("Do you want to play again? (yes/no): ");
        Scanner scanner = new Scanner(System.in);
        playAgain = scanner.next();
    }
}