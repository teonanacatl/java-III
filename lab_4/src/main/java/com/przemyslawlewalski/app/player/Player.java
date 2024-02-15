package com.przemyslawlewalski.app.player;

import com.przemyslawlewalski.app.game.mode.GameMode;
import com.przemyslawlewalski.app.game.mode.GameModeMulti;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {
    private String nickname;
    private Map<String, GameMode> single;
    private Map<String, GameModeMulti> multi;
    private boolean leader = false;
    private boolean champion = false;

    public Player(String nickname) {
        this.setNickname(nickname);
        this.setSingle(new HashMap<>());
        this.setMulti(new HashMap<>());
        this.getSingle().put("classic game", new GameMode());
        this.getSingle().put("reverse game", new GameMode());
        this.getMulti().put("classic game", new GameModeMulti());
        this.getMulti().put("reverse game", new GameModeMulti());
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Map<String, GameMode> getSingle() {
        return single;
    }

    public void setSingle(Map<String, GameMode> single) {
        this.single = single;
    }

    public Map<String, GameModeMulti> getMulti() {
        return multi;
    }

    public void setMulti(Map<String, GameModeMulti> multi) {
        this.multi = multi;
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public boolean isChampion() {
        return champion;
    }

    public void setChampion(boolean champion) {
        this.champion = champion;
    }

    public void grantTournamentPrivileges() {
        if (this.isChampion()) {
            System.out.println("As a champion, you get an extra 3 attempts!");
        }
    }

    public void grantNonTournamentPrivileges() {
        if (this.isChampion()) {
            System.out.println("As a champion, you get an extra 1 attempts!");
        }
    }
}
