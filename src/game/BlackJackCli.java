package game;

import java.util.ArrayList;
import java.util.Scanner;

public class BlackJackCli {
    private static Scanner input;
    private static Game game;
    private static Dealer dealer;
    private static ArrayList<Player> players;

    public static void main(String[] args) {
        input = new Scanner(System.in);
        game = new Game();

        System.out.printf("Number of players: ");
        int playerCount = input.nextInt();
        input.nextLine();
        for (int i = 0; i < playerCount; i++) {
            System.out.printf("Player %d's name: ", i + 1);
            game.addPlayer(new Player(input.nextLine()));
        }
        dealer = game.getDealer();
        players = game.getPlayers();


        while (game.hasPlayers()) {
            game.newRound();
            for (Player player : players) {
                getBet(player);
            }
            game.initialDeal();
            displayAllHands();
            for (Player player : players) {
                playerTurns(player);
            }
            dealerTurn();
            payBets();
            removeMoneyless();
        }
    }

    public static void getBet(Player player) {
        while (true) {
            System.out.printf("\n\n%s's bet ($%d left): ", player.getName(), player.getMoney());
            int betAmt = input.nextInt();
            input.nextLine();

            try {
                player.bet(betAmt);
                return;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        
    }

    public static void displayAllHands() {
        System.out.println("Initial deal:\n");
        System.out.printf("Dealer: [%s] [HIDDEN]\n", dealer.getFaceUpCard());
        for(Player player : players){
            System.out.printf("%s: ", player.getName());
            for(Card card : player.getHand(0)){ //only one hand
                System.out.printf("[%s]", card);
            }
            System.out.printf(" -> Total: %d\n", player.getHand(0).getValue());
        }
    }

    public static void playerTurns(Player player) {
        System.out.printf("\n\n%s's turn\n\n", player.getName());
        for (int i = 0; i < player.getHands().size(); i++) {
            if (player.getHand(i).isSplittable()) {
                System.out.printf("Split hand? (y/n): ");
                if (input.nextLine().equals("y")) {
                    player.splitHand(i);
                    player.getHand(i).addCard(dealer.deal());
                    player.getHand(i + 1).addCard(dealer.deal());
                    for (int j = 0; j < player.getHands().size(); j++) {
                        System.out.printf("Hand %d: ", j);
                        for (Card card : player.getHand(j)) { //only one hand
                            System.out.printf("[%s]", card);
                        }
                        System.out.printf(" -> Total: %d\n", player.getHand(0).getValue());
                    }
                }
            }
            Hand hand = player.getHand(i);
            while (hand.getValue() < 21) {
                System.out.printf("Current hand total: %d\n", hand.getValue());
                System.out.print("1 - Hit\n2 - Stay\n> ");
                int userChoice = input.nextInt();
                input.nextLine();
                if (userChoice == 1) {
                    Card card = game.hit(hand);
                    System.out.printf("Dealt: %s\n", card);
                } else {
                    return;
                }
            }

            System.out.printf("\nCurrent hand total: %d\n", hand.getValue());
            if (hand.isOver21()) {
                System.out.printf("%s busted!\n", player.getName());
            }
        }
        
    }

    public static void dealerTurn() {
        System.out.println("\n\nDealer's turn\n");

        System.out.printf("Dealer's cards: [%s] [%s]\n", dealer.getFaceUpCard(), dealer.getHand().getCard(1));
        System.out.printf("Dealer's hand total: %d\n", dealer.getHand().getValue());

        while (!dealer.getHand().isOver16()) {
            Card card = game.dealCardToDealer();
            System.out.printf("Dealer drew: %s\n", card);
            System.out.printf("Dealer's hand total: %d\n", dealer.getHand().getValue());
        }
        if (dealer.getHand().isOver21()) {
            System.out.println("Dealer busted!");
        }
    }

    public static void payBets() {
        System.out.println("\n\nRESULTS: ");
        for (Player player : players) {
            System.out.printf("%s: ", player.getName());
            for (Hand h : player.getHands()) {
                System.out.printf("%s ", game.getResult(h));
                game.payBet(player);
            }
            System.out.printf("Bet: %d, Money: %d\n", player.getBet(), player.getMoney());
        }
    }

    public static void removeMoneyless() {
        ArrayList<Player> peopleRemoved = game.removeMoneyless();
        for (Player player : peopleRemoved) {
            System.out.printf("%s removed from game.\n", player.getName());
        }
    }
}
