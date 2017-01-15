package game;

import java.util.Scanner;

public class BlackJack {
    public static void main(String[] args) {
        Game g = new Game();
        Scanner in = new Scanner(System.in);
        System.out.printf("Number of players: ");
        int playerCount = in.nextInt();
        in.nextLine();
        for (int i = 0; i < playerCount; i++) {
            System.out.printf("Player %d's name: ", i + 1);
            g.addPlayer(new Player(in.nextLine()));
        }
        g.run();
    }
}
