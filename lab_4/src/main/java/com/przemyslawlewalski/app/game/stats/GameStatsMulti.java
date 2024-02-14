package com.przemyslawlewalski.app.game.stats;

public class GameStatsMulti extends GameStats {
    private int seriesWins;
    private int seriesLoses;
    private String seriesRecord;

    public GameStatsMulti() {
        super();
        this.setSeriesWins(0);
        this.setSeriesLoses(0);
        this.setSeriesRecord("");
    }

    public int getSeriesWins() {
        return seriesWins;
    }

    public void setSeriesWins(int seriesWins) {
        this.seriesWins = seriesWins;
    }

    public int getSeriesLoses() {
        return seriesLoses;
    }

    public void setSeriesLoses(int seriesLoses) {
        this.seriesLoses = seriesLoses;
    }

    public String getSeriesRecord() {
        return seriesRecord;
    }

    public void setSeriesRecord(String seriesRecord) {
        this.seriesRecord = seriesRecord;
    }
}