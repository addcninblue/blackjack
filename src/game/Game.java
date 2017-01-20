package game;

import java.util.ArrayList;
import javax.swing.SwingUtilities;

public class Game {
    private ArrayList<Player> players;
    private Dealer dealer;

    public Game(){
        this.players = new ArrayList<>();
        dealer = new Dealer("DEALER");
    }
    
    /**
     * Prepares for a new round by emptying each player's hand
     * (Postcondition: the dealer and players' hands are emptied)
     * (Precondition: none of the players or the dealer are null)
     */
    public void newRound() {
        dealer.resetTurn();
        players.forEach(Player::resetTurn);
    }

    /**
     * Deals the initial two cards to each player
     * (Postcondition: Each player has two cards)
     * (Precondition: No players in players is null)
     */
    public void initialDeal() {
        for (int i = 0; i < 2; i++) {
            dealer.getHand().addCard(dealer.deal());
        }
        dealer.getHand().getCard(0).setHidden(true);

        for (Player player : players) {
            for (int j = 0; j < 2; j++) {
                player.getHand(0).addCard(dealer.deal());
            }
        }
    }
    
    public void splitPlayer(Player player, Hand hand) {
        if (!hand.isSplittable()) {
            throw new IllegalArgumentException(String.format("%s's hand can't be split.", player.getName()));
        }
        
    }
    /**
     * The dealer deals the player a card
     * (Postcondition: player is dealt a card)
     * @param hand the hand to hit
     * @return the card given to the player
     * (Precondition: player is nonnull)
     */
    public Card hit(Hand hand) {
        if (hand.isOver21()) {
            throw new IllegalStateException("Your hand is already busted!");
        }
        Card c = dealer.deal();
        hand.addCard(c);
        if (hand.isOver21()) {
            hand.demoteAces();
        }
        return c;
    }
    /*public Card deal(Player player, Hand hand) {
        
    }*/
    /**
     * Deals a card to the dealer
     * (Postcondition: dealer is dealt a card)
     * @return the card dealt
     * (Precondition: dealer is nonnull and has a hand under 21)
     */
    public Card dealCardToDealer() {
        Card c = dealer.deal();
        dealer.getHand().addCard(c);
        return c;
    }

    /**
     * Pays the player according to their result and bet
     * (Postcondition: the player is paid according to their result and bet)
     * @param player the player to pay
     * (Precondition: player is nonnull)
     */
    public void payBet(Player player) {
        int dHand = dealer.getHand().getValue();
        for (Hand h : player.getHands()) {
            int pHand = h.getValue();
            if (h.isBlackJack() && !dealer.getHand().isBlackJack()) { //blackjack
                player.addMoney((int) (player.getBet() * 2.5));
            } //247blackjack rounds down
            if (!h.isOver21() && (pHand > dHand || dealer.getHand().isOver21())) { //win
                player.addMoney(player.getBet() * 2);
            } else if (!h.isOver21() && !dealer.getHand().isBlackJack() && pHand == dHand) { //push
                player.addMoney(player.getBet());
            }
        }
    }

    /**
     * Returns a string representing the outcome of the last round
     * for the player.
     * (Postcondition: player is unchanged)
     * @param h the hand to get the result of
     * @return a string representing the outcome of the last round
     * for the player.
     * (Precondition: player is nonnull)
     */
    public String getResult(Hand h) {
        int dHand = dealer.getHand().getValue();
        int pHand = h.getValue();
        return h.isBlackJack() ? "BLACKJACK"
                : h.isOver21() ? "BUST"
                : pHand < dHand && !dealer.getHand().isOver21() ? "LOSE"
                : pHand == dHand ? "PUSH"
                : "WIN";
    }

    /**
     * Removes the moneyless players from the game
     * (Postcondition: the moneyless players are removed from players)
     * @return an arraylist containing the removed players
     * (Precondition: all players in players are nonnull)
     */
    public ArrayList<Player> removeMoneyless() {
        ArrayList<Player> moneyless = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getMoney() == 0) {
                moneyless.add(players.get(i));
                players.remove(i--);
            }
        }
        return moneyless;
    }

    public boolean hasPlayers() {
        return players.size() > 0;
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

    public Dealer getDealer() {
        return dealer;
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
}
