package game;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private ArrayList<Player> players;
    private Dealer dealer;
    private Scanner in;

    public Game(){
        this.players = new ArrayList<Player>();
        dealer = new Dealer("DEALER");
        in = new Scanner(System.in);
    }

    public void run() {
        while (players.size() > 0) {
            resetTurn();
            doBets();
            deal();
            doPlayerTurns();
            doDealerTurn();
            payBets();
            removeMoneyless();
        }
        System.out.println("There are no players left in the game!");
    }

    private void resetTurn() {
        dealer.resetTurn();
        players.forEach(Player::resetTurn);
    }

    private void doBets() {
        for (Player p : players) {
            p.resetTurn();
            while (true) {
                System.out.printf("%s's bet ($%d left): ", p.getName(), p.getMoney());
                int bet = in.nextInt();
                in.nextLine();

                if (bet > p.getMoney()) {
                    System.out.printf("%s only has $%d", p.getName(), p.getMoney());
                    continue;
                }
                p.bet(bet);
                break;
            }
        }
    }

    private void deal() {
        System.out.println("Initial deal:\n");
        for (int i = 0; i < 2; i++) {
            dealer.addCard(dealer.deal());
        }
        System.out.printf("Dealer: [%s] [HIDDEN]\n", dealer.getFaceUpCard());

        for (Player p : players) {
            System.out.printf("%s: ", p.getName());
            for (int j = 0; j < 2; j++) {
                Card c = dealer.deal();
                System.out.printf("[%s]", c);
                if (c.RANK == Rank.ACE) {
                    c.setValue(11);
                }
                p.addCard(c); //+100 for code reuse
            }
            System.out.printf(" -> Total: %d\n", p.getHandTotal());
        }
    }

    private void doPlayerTurns() {
        loop: for (Player p : players) {
            System.out.printf("\n\n%s's turn\n\n", p.getName());
            while (p.getHandTotal() < 21) {
                System.out.printf("Current hand total: %d\n", p.getHandTotal());
                System.out.print("1 - Hit\nelse - Stay\n> ");
                if (in.nextInt() == 1) {
                    Card c = dealer.deal();
                    if (c.RANK == Rank.ACE && p.getHandTotal() <= 10) {
                        c.setValue(11);
                    }
                    System.out.printf("Dealt: %s\n", c);
                    p.addCard(c);
                } else {
                    continue loop;
                }
                if (p.isOver21()) {
                    p.demoteAces();
                }
            }
            System.out.printf("\nCurrent hand total: %d\n", p.getHandTotal());
            System.out.printf("%s busted!\n", p.getName());
            in.nextLine();
        }
    }

    private void doDealerTurn() {
        System.out.println("\n\nDEALER's turn\n");

        System.out.printf("Dealer's cards: [%s] [%s]\n", dealer.getFaceUpCard(), dealer.getHand().get(1));
        System.out.printf("DEALER's hand total: %d", dealer.getHandTotal());

        while (!dealer.isOver16()) {
            Card c = dealer.deal();
            System.out.printf("DEALER drew: %s\n", c);
            dealer.addCard(c);
            System.out.printf("DEALER's hand total: %d", dealer.getHandTotal());
        }
        if (dealer.isOver21()) {
            System.out.println("DEALER busted!");
        }
    }

    private void payBets() {
        System.out.println("\n\nRESULTS: ");
        int dTotal = dealer.getHandTotal();
        for (Player p : players) {
            int pTotal = p.getHandTotal();
            System.out.printf("%s: ", p.getName());
            System.out.printf(
                    pTotal > dTotal ? "WIN"
                    : pTotal == dTotal ? "PUSH"
                    : "LOSE"
            );
            if (p.getHandTotal() > dealer.getHandTotal()) {
                p.addMoney(p.getBet());
            }
            System.out.printf(" Bet: %d, Money: %d\n", p.getBet(), p.getMoney());
        }
    }

    private void removeMoneyless() {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getMoney() == 0) {
                System.out.printf("%s removed.", players.get(i).getName());
                players.remove(i);
            }
        }
    }

    /**
     * Returns an arraylist of the game's players
     * (Postcondition: an arraylist of the game's players is returned)
     * @return an arraylist of the game's players
     * (Precondition: game is initialized and people are added)
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

    /**
     * Adds player to the game
     * (Postcondition: added to arraylist players)
	 * @param player player to add
     * (Precondition: player is a valid player)
     */
    public void addPlayer(Player player){
        players.add(player);
    }

    /**
     * Removes player with name name
     * (Postcondition: person is removed from players)
	 * @param name name of player to remove
     * (Precondition: name is a valid name of a person)
     * @return the Player removed.
     *         null => couldn't find the player
     */
    public Player removePlayer(String name){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getName().equals(name)){
                Player playertoRemove = players.get(i);
                players.remove(i);
                return playertoRemove;
            }
        }
        return null;
    }

    /**
     * Hits a player
     * (Postcondition: card is added to a player)
     * @param playertoHit the player to hit
     * (Precondition: none)
     * @return the card added
     */
    public Card hit(Player playertoHit){
        for(Player player : players){
            if(player == playertoHit){
                Card card = dealer.deal();
                player.addCard(card);
                return card;
            }
        }
        return null;
    }

    public static void main (String [] args){
        Game game = new Game();

        Player player1 = new Player("add");
        Player player2 = new Player("daniel");
        Player player3 = new Player("darian");
        Player player4 = new Player("lucy");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);

        ArrayList<Player> players = game.getPlayers();

        for(Player player : players){
            Card c = game.hit(player);
            System.out.format("hit: %s\n", c.toString());
            System.out.println(player.getName());
            for(Card card : player.getHand()){
                System.out.println(card.toString());
            }
            System.out.println();
        }

    }

}
