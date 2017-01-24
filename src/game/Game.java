package game;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private ArrayList<Player> players;
    private Dealer dealer;

    public Game(){
        this.players = new ArrayList<>();
        dealer = new Dealer();
    }

    public Game(List<Player> playersList) {
        this();
        for (Player player : playersList) {
            players.add(player);
        }
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

    public void split(Player player, Hand hand) {
        if (!player.canSplitHand(hand)) {
            throw new IllegalArgumentException(player.getName() + " can't split.\n");
        }

        List<Hand> hands = player.getHands();
        int handIndex = hands.indexOf(hand);
        int newHandIndex = handIndex + 1;
        Hand newHand = new Hand();
        newHand.addCard(hand.getCard(1));

        hands.add(newHandIndex, newHand);
        hand.getCards().remove(1);

        hit(player.getHand(handIndex));
        hit(player.getHand(newHandIndex));

        player.bet(player.getBet());
    }


    /**
     * Deals the initial two cards to each player
     * (Postcondition: Each player has two cards)
     * (Precondition: No players in players is null)
     */
    public void initialDeal() {
        for (int i = 0; i < 2; i++) {
            hit(dealer.getHand());
            dealer.hideHand();
        }

        for (Player player : players) {
            for (int j = 0; j < 2; j++) {
                hit(player.getHand(0));
            }
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
        Card c = dealer.deal();
        hand.addCard(c);
        if (hand.isOver21()) {
            hand.demoteAces();
        }
        return c;
    }
    /**
     *
     * @param player the player to double down
     * @return the card dealt to the hand
     */
    public Card doubleDown(Player player) {
        if (!player.canDoubleDown()) {
            throw new IllegalArgumentException(
                    String.format("%s can't double down.\n", player.getName()));
        }
        Hand hand = player.getHand(0);
        Card card = hit(hand);

        card.setHidden(true);

        player.bet(player.getBet());
        return card;
    }

    /**
     *
     * @param player the player to insure
     */
    public void insure(Player player) {
        if (!canInsure(player)) {
            throw new IllegalStateException("Can't insure this player!\n");
        }
        player.setMoney(player.getMoney() - player.getBet()/2);
        player.setInsured(true);
    }

    /**
     * Pays the player according to their result and bet
     * (Postcondition: the player is paid according to their result and bet)
     * @param player the player to pay
     * (Precondition: player is nonnull)
     */
    public void payBet(Player player) {
        int dealerHand = dealer.getHand().getValue();
        for (Hand hand : player.getHands()) {
            int playerHand = hand.getValue();
            int moneyWon = 0;

            if (hand.isBlackJack() && !dealer.getHand().isBlackJack()) { //blackjack
                moneyWon = player.getBet(); //return bet
                moneyWon += (int)(player.getBet() * 1.5); //pay 3:2
            } else if (!hand.isOver21() && (playerHand > dealerHand || dealer.getHand().isOver21())) { //win
                moneyWon = player.getBet(); //return bet
                moneyWon += player.getBet(); //pay 1:1
            } else if ((!hand.isOver21() && !dealer.getHand().isBlackJack() && playerHand == dealerHand)
                    || (hand.isBlackJack() && dealer.getHand().isBlackJack())) { //push
                moneyWon = player.getBet(); //return bet
            }
            player.setMoney(player.getMoney() + moneyWon);
        }
        if (player.isInsured() && dealer.getHand().isBlackJack()) {
            player.setMoney(player.getMoney() + (int)(player.getBet()*1.5));
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
        String result = h.isBlackJack() ? "BLACKJACK"
                : h.isOver21() ? "busted!"
                : pHand < dHand && !dealer.getHand().isOver21() ? "lost!"
                : pHand == dHand ? "pushed!"
                : "won!";
        return result;
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

    public boolean canInsure(Player player) {
        return player.getMoney() >= player.getBet() / 2 //has money
                && !player.isInsured() //hasn't insured yet
                && player.getHands().size() == 1 //hasn't split
                && player.getHand(0).count() == 2 //hasn't hit
                && dealer.getFaceUpCard().RANK == Rank.ACE;
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
