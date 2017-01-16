package game;

import java.util.ArrayList;
import java.util.Scanner;

public class BlackJackCli {
    private static Scanner in;
    private static Game game;
    private static Dealer dealer;
    private static ArrayList<Player> players;

    public static void main(String[] args) {
        in = new Scanner(System.in);
        game = new Game();

        System.out.printf("Number of players: ");
        int playerCount = in.nextInt();
        in.nextLine();
        for (int i = 0; i < playerCount; i++) {
            System.out.printf("Player %d's name: ", i + 1);
            game.addPlayer(new Player(in.nextLine()));
        }
        dealer = game.getDealer();
        players = game.getPlayers();


        while (game.hasPlayers()) {
            game.newRound();
            setBets();
            deal();
            playerTurns();
            dealerTurn();
            payBets();
            removeMoneyless();
        }
    }

    public static void setBets() {
        for (Player player : players) {
            while (true) {
                String playerName = player.getName();
                int playerMoney = player.getMoney();

                System.out.printf("\n\n%s's bet ($%d left): ", playerName, playerMoney);
                int bet = in.nextInt();
                in.nextLine();

                if (game.setBet(player, bet)) {
                    break;
                } else {
                    System.out.printf("%s can't bet $%d\n", playerName, bet);
                }
            }
        }
    }

    public static void deal() {
        game.initialDeal();

        System.out.println("Initial deal:\n");
        System.out.printf("Dealer: [%s] [HIDDEN]\n", dealer.getFaceUpCard());
        for(Player player : players){
            System.out.printf("%s: ", player.getName());
            for(Card card : player.getHand()){
                System.out.printf("[%s]", card);
            }
            System.out.printf(" -> Total: %d\n", player.getHandTotal());
        }
    }

    public static void playerTurns() {
        loop: for(Player player : players){
            System.out.printf("\n\n%s's turn\n\n", player.getName());
            while (player.getHandTotal() < 21) {
                System.out.printf("Current hand total: %d\n", player.getHandTotal());
                System.out.print("1 - Hit\n2 - Stay\n> ");
                int userChoice = in.nextInt();
                in.nextLine();
                if (userChoice == 1) {
                    Card card = game.hit(player);
                    System.out.printf("Dealt: %s\n", card);
                } else {
                    continue loop;
                }
            }

            System.out.printf("\nCurrent hand total: %d\n", player.getHandTotal());
            if (player.isOver21()) {
                System.out.printf("%s busted!\n", player.getName());
            }
        }
    }

    public static void dealerTurn() {
        System.out.println("\n\nDealer's turn\n");

        System.out.printf("Dealer's cards: [%s] [%s]\n", dealer.getFaceUpCard(), dealer.getHand().get(1));
        System.out.printf("Dealer's hand total: %d\n", dealer.getHandTotal());

        while (!dealer.isOver16()) {
            Card card = game.dealCardToDealer();
            System.out.printf("Dealer drew: %s\n", card);
            System.out.printf("Dealer's hand total: %d\n", dealer.getHandTotal());
        }
        if (dealer.isOver21()) {
            System.out.println("Dealer busted!");
        }
    }

    public static void payBets() {
        System.out.println("\n\nRESULTS: ");
        for (Player player : players) {
            System.out.printf("%s: ", player.getName());
            System.out.printf(game.getResult(player));
            game.payBet(player);
            System.out.printf(" Bet: %d, Money: %d\n", player.getBet(), player.getMoney());
        }
    }

    public static void removeMoneyless() {
        ArrayList<Player> peopleRemoved = game.removeMoneyless();
        for (Player player : peopleRemoved) {
            System.out.printf("%s removed from game.\n", player.getName());
        }
    }
}