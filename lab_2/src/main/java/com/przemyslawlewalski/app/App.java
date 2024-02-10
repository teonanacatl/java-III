package com.przemyslawlewalski.app;

import java.util.Scanner;

public class App {
    public static void main( String[] args ) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your nickname: ");
        String nickname = scanner.nextLine();

        Player player = new Player(nickname);
        Game game = new Game(player);
        game.start();

        scanner.close();
    }
}
