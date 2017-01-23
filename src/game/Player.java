package game;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Hand> hands;
    private int money;
    private int bet; //the amount bet during a turn
    private boolean insured; //whether or not the player is insured
    private boolean doubleDowned;

    public Player(String name){
        this.name = name;
        hands = new ArrayList<>();
        money = 1000;
        bet = 0;
        insured = false;
        doubleDowned = false;
    }

    /**
     * Returns name
     * (Postcondition: name is returned)
     * (Precondition: none)
     */
    public String getName(){
        return name;
    }

    public void bet(int betAmt) {
        if (betAmt < 1) {
            throw new IllegalArgumentException("You can't bet below $1!");
        }
        if (betAmt > money) {
            throw new IllegalArgumentException("You don't have enough money to bet!");
        }

        this.money -= betAmt;
        this.bet = betAmt;
    }

    public void insure() {
        if (!hasInsuranceMoney()) {
            throw new IllegalStateException(name + " doesn't have enough money to insure.\n");
        }
        money -= bet / 2;
        insured = true;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void unhideHand() {
        for (Card card : hands.get(0)) {
            card.setHidden(false);
        }
    }

    public void resetTurn() {
        hands.clear();
        hands.add(new Hand());
        if (money < bet) {
            bet = 1;
        }
        insured = false;
    }

    /**
     * Returns whether or not a hand is splittable
     * (Postcondition: hands is unchanged)
     * @param hand
     * @return whether hands(i) is splittable
     * (Precondition: i is a valid index in hands)
     */
    public boolean canSplitHand(Hand hand) {
        checkHand(hand);
        return hand.isSplittable() && getMoney() >= getBet();
    }

    public boolean canDoubleDown() {
        Hand hand = hands.get(0);
        //has not split, has not hit, and has enough money
        return hands.size() == 1 && hand.count() == 2
                && getMoney() >= getBet();
    }

    /**
     * Returns whether or not the player can insure
     * @param dealer the dealer
     * @return whether or not the player can insure
     * (Precondition: the dealer and player have not hit yet)
     */
    public boolean canInsure(Dealer dealer) {
        return hasInsuranceMoney()
                && !insured //hasn't insured yet
                && hands.size() == 1 //hasn't split
                && hands.get(0).count() == 2 //hasn't hit
                && dealer.getFaceUpCard().RANK == Rank.ACE;
    }

    public boolean hasInsuranceMoney() {
        return money >= bet / 2; //this is used in two methods
    }

    /**
     * Splits a hand
     * (Postcondition: the hand at index i is split)
     * @param hand
     * (Precondition: i is an index in hands)
     */
    public void splitHand(Hand hand) {
        checkHand(hand);
        if (!canSplitHand(hand)) {
            throw new IllegalArgumentException(name + " can't split.\n");
        }
        // Hand h1 = new Hand();
        Hand h2 = new Hand();
        // h1.addCard(hand.getCard(0));
        h2.addCard(hand.getCard(1));
        int i = hands.indexOf(hand);
        // hands.add(i, h1);
        hands.add(i + 1, h2);
        hand.getCards().remove(1);
        // hands.remove(hand);
        //too lazy to make a removeCard function
    }

    /**
     * Throws an IllegalArgumentException if hand is not player's
     * @param hand
     */
    private void checkHand(Hand hand) {
        if (!hands.contains(hand)) {
            throw new IllegalArgumentException("This hand doesn't belong to " + name);
        }
    }

    @Override
    public String toString() {
        return String.format("Player: %s\nMoney: %d", name, money);
    }

    public boolean isInsured() {
        return insured;
    }

    public boolean isDoubleDowned() {
        return doubleDowned;
    }

    public void setDoubleDowned(boolean doubleDowned) {
        this.doubleDowned = doubleDowned;
    }

    /**
     * Returns the player's hands
     * (Postcondition: player hands are returned)
     * (Precondition: hands is initialized)
     * @return player's hands
     */
    public ArrayList<Hand> getHands(){
        return hands;
    }

    public Hand getHand(int i) {
        return hands.get(i);
    }

    public int getMoney() {
        return money;
    }

    public int getBet() {
        return bet;
    }
}
