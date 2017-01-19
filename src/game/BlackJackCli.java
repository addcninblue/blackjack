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
            displayHands();
            playerTurns();
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

    public static void displayHands() {
        System.out.println("Initial deal:\n");
        System.out.printf("Dealer: [%s] [HIDDEN]\n", dealer.getFaceUpCard());
        for(Player player : players){
            System.out.printf("%s: ", player.getName());
            for(Card card : player.getHand(0)){ //only one hand
                System.out.printf("[%s]", card);
            }
            System.out.printf(" -> Total: %d\n", player.getHand(0).getTotal());
        }
    }

    public static void playerTurns() {
        for(Player player : players){
            System.out.printf("\n\n%s's turn\n\n", player.getName());
            loop: for (int i = 0; i < player.getHands().size(); i++) {
                if (player.canSplitHand(i)) {
                    System.out.printf("Split hand? (y/n): ");
                    if (input.nextLine().equals("y")) {
                        game.splitPlayer(player, i);
                        for (int j = 0; j < player.getHands().size(); j++) {
                            System.out.printf("Hand %d: ", j);
                            for (Card card : player.getHand(j)) { //only one hand
                                System.out.printf("[%s]", card);
                            }
                            System.out.printf(" -> Total: %d\n", player.getHand(j).getTotal());
                        }
                    }
                }

                Hand h = player.getHand(i);
                while (h.getTotal() < 21) {
                    System.out.printf("Current hand total: %d\n", h.getTotal());
                    System.out.print("1 - Hit\n2 - Stay\n");
                    if (player.canDoubleDown(i)) {
                        System.out.println("3 - Double Down");
                    }
                    System.out.print("> ");
                    int userChoice = input.nextInt();
                    input.nextLine();
                    if (userChoice == 1) {
                        Card card = game.hit(h);
                        System.out.printf("Dealt: %s\n", card);
                    } else if (userChoice == 3 && player.canDoubleDown(i)) {
                        Card card = game.doubleDown(player, i);
                        System.out.printf("Dealt: %s\n", card);
                        break;
                    } else{
                        continue loop;
                    }
                }

                System.out.printf("\nCurrent hand total: %d\n", h.getTotal());
                if (h.isOver21()) {
                    System.out.printf("%s busted!\n", player.getName());
                }
            }
        }
    }

    public static void dealerTurn() {
        System.out.println("\n\nDealer's turn\n");

        System.out.printf("Dealer's cards: [%s] [%s]\n", dealer.getFaceUpCard(), dealer.getHand().getCard(1));
        System.out.printf("Dealer's hand total: %d\n", dealer.getHand().getTotal());

        while (!dealer.getHand().isOver16()) {
            Card card = game.dealCardToDealer();
            System.out.printf("Dealer drew: %s\n", card);
            System.out.printf("Dealer's hand total: %d\n", dealer.getHand().getTotal());
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
            }
            game.payBet(player);
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
