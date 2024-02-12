package com.przemyslawlewalski.app;

public class GameModeMulti extends GameMode {
    public GameModeMulti() {
        super();
        this.getDifficulty().put("easy", new GameStatsMulti());
        this.getDifficulty().put("normal", new GameStatsMulti());
        this.getDifficulty().put("hard", new GameStatsMulti());
    }
}