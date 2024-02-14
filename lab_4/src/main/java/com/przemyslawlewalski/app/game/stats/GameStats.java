package com.przemyslawlewalski.app.game.stats;

import java.io.Serializable;

public class GameStats implements Serializable {
    private int wins;
    private int loses;
    private String record;

    public GameStats() {
        this.setWins(0);
        this.setLoses(0);
        this.setRecord("");
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public void setRecord(String record) {
        this.record = record;
    }
}
