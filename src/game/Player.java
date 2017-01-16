package game;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> hand;
    private int money;
    private int bet; //the amount bet during a turn

    public Player(String name){
        this.name = name;
        hand = new ArrayList<>();
        money = 1000;
        bet = 0;
    }

    /**
     * Returns name
     * (Postcondition: name is returned)
     * (Precondition: none)
     */
    public String getName(){
        return name;
    }

    public int getHandTotal() {
        int sum = 0;
        for (Card c : hand) {
            sum += c.getValue();
        }
        return sum;
    }

    public void bet(int amount) {
        if (amount <= money) {
            this.money -= amount;
            this.bet = amount;
        }
    }

    public boolean isOver21() {
        return getHandTotal() > 21;
    }

    public boolean hasBlackJack() {
        return hand.size() == 2 && getHandTotal() == 21;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void demoteAces() {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getValue() == 11) { //only promoted aces will have 11
                hand.get(i).setValue(1); //magic numbers
            }
        }
    }

    public void resetTurn() {
        hand.clear();
        if (money < bet) {
            bet = 1;
        }
    }

    /**
     * Adds a given card to a player's hand
     * (Postcondition: card is added to hand)
	 * @param card card to add
     * (Precondition: card is a valid card)
     */
    public void addCard(Card card){
        if (card.RANK == Rank.ACE && getHandTotal() <= 10) {
            card.setValue(11);
        }
        hand.add(card);
    }

    @Override
    public String toString() {
        return String.format("Player: %s\nMoney: %d", name, money);
    }

    /**
     * Returns the player's hand
     * (Postcondition: player hand is returned)
     * (Precondition: hand is initialized)
     * @return player's hand
     */
    public ArrayList<Card> getHand(){
        return hand;
    }

    public int getMoney() {
        return money;
    }

    public int getBet() {
        return bet;
    }
}
