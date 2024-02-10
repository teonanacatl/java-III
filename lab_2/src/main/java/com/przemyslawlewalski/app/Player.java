package com.przemyslawlewalski.app;

public class Player {
    private String nickname;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void sayHello() {
        System.out.println("Hello World!\nMy nickname is " + this.nickname + ".");
    }
}
