package com.przemyslawlewalski.app.game.mode;

import com.przemyslawlewalski.app.game.stats.GameStats;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameMode implements Serializable {
    private Map<String, GameStats> difficulty;

    public GameMode() {
        this.setDifficulty(new HashMap<>());
        this.getDifficulty().put("easy", new GameStats());
        this.getDifficulty().put("normal", new GameStats());
        this.getDifficulty().put("hard", new GameStats());
    }

    public Map<String, GameStats> getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Map<String, GameStats> difficulty) {
        this.difficulty = difficulty;
    }
}