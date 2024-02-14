package com.przemyslawlewalski.app;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Player implements Serializable {
    private String nickname;
    private Map<String, GameMode> single;
    private Map<String, GameModeMulti> multi;

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
}
