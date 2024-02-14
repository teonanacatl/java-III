package com.przemyslawlewalski.app.game.mode;

import com.przemyslawlewalski.app.game.stats.GameStatsMulti;

public class GameModeMulti extends GameMode {
    public GameModeMulti() {
        super();
        this.getDifficulty().put("easy", new GameStatsMulti());
        this.getDifficulty().put("normal", new GameStatsMulti());
        this.getDifficulty().put("hard", new GameStatsMulti());
    }
}