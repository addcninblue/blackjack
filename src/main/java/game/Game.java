package game;

import java.util.ArrayList;

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
     * Sets a player's bet.
     * (Postcondition: player's bet is set if player has enough money)
     * @param player the betting player
     * @param bet the amount bet
     * @return whether or not the bet was possible
     * (Precondition: player is nonnull and bet is nonnegative)
     */
    public boolean setBet(Player player, int bet) {
        if (bet > player.getMoney() || bet < 1) {
            return false;
        }
        player.bet(bet);
        return true;
    }

    /**
     * Deals the initial two cards to each player
     * (Postcondition: Each player has two cards)
     * (Precondition: No players in players is null)
     */
    public void initialDeal() {
        for (int i = 0; i < 2; i++) {
            dealer.addCard(dealer.deal());
        }

        for (Player p : players) {
            for (int j = 0; j < 2; j++) {
                p.addCard(dealer.deal());
            }
        }
    }

    /**
     * The dealer deals the player a card
     * (Postcondition: player is dealt a card)
     * @param player the player to hit
     * @return the card given to the player
     * (Precondition: player is nonnull)
     */
    public Card hit(Player player) {
        Card c = dealer.deal();
        player.addCard(c);
        if (player.isOver21()) {
            player.demoteAces();
        }
        return c;
    }

    /**
     * Deals a card to the dealer
     * (Postcondition: dealer is dealt a card)
     * @return the card dealt
     * (Precondition: dealer is nonnull and has a hand under 21)
     */
    public Card dealCardToDealer() {
        Card c = dealer.deal();
        dealer.addCard(c);
        return c;
    }

    /**
     * Pays the player according to their result and bet
     * (Postcondition: the player is paid according to their result and bet)
     * @param player the player to pay
     * (Precondition: player is nonnull)
     */
    public void payBet(Player player) {
        int dHand = dealer.getHandTotal();
        int pHand = player.getHandTotal();
        if (player.hasBlackJack() && !dealer.hasBlackJack()) { //blackjack
            player.addMoney((int)(player.getBet() * 2.5));
        } //247blackjack rounds down
        if (!player.isOver21() && (pHand > dHand || dealer.isOver21())) { //win
            player.addMoney(player.getBet() * 2);
        }
        else if(!player.isOver21() && !dealer.hasBlackJack() && pHand == dHand) { //push
            player.addMoney(player.getBet());
        }
    }

    /**
     * Returns a string representing the outcome of the last round
     * for the player.
     * (Postcondition: player is unchanged)
     * @param player the player to get the results of
     * @return a string representing the outcome of the last round
     * for the player.
     * (Precondition: player is nonnull)
     */
    public String getResult(Player player) {
        int dHand = dealer.getHandTotal();
        int pHand = player.getHandTotal();
        return player.hasBlackJack() ? "BLACKJACK"
                : player.isOver21() ? "BUST"
                : pHand < dHand && !dealer.isOver21() ? "LOSE"
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
                players.remove(i);
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