package game;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Hand> hands;
    private int money;
    private int bet; //the amount bet during a turn
    private boolean insured; //whether or not the player is insured

    public Player(String name){
        this.name = name;
        hands = new ArrayList<>();
        money = 1000;
        bet = 0;
        insured = false;
    }

    public void bet(int betAmt) {
        if (betAmt < 1) {
            throw new IllegalArgumentException("You can't bet below $1!");
        }
        if (betAmt > money) {
            throw new IllegalArgumentException("You don't have enough money to bet!");
        }

        this.money -= betAmt;
        this.bet += betAmt;
    }

    public void resetTurn() {
        hands.clear();
        hands.add(new Hand());
        bet = 0;
        insured = false;
    }
    
    /**
     * Splits a hand
     * (Postcondition: the hand at index i is split)
     * @param hand
     * (Precondition: i is an index in hands)
     */
    public void splitHand(Hand hand) {
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

    @Override
    public String toString() {
        return String.format("Player: %s\nMoney: %d", name, money);
    }

    /**
     * Returns whether or not a hand is splittable
     * (Postcondition: hands is unchanged)
     * @param hand
     * @return whether hands(i) is splittable
     * (Precondition: i is a valid index in hands)
     */
    public boolean canSplitHand(Hand hand) {
        return hands.contains(hand) && hand.isSplittable() && getMoney() >= getBet();
    }

    public boolean canDoubleDown() {
        Hand hand = hands.get(0);
        //has not split, has not hit, and has enough money
        return hands.size() == 1 && hand.count() == 2 && !hand.isBlackJack()
                && getMoney() >= getBet();
    }
    
    public String getName(){
        return name;
    }
    
    public boolean isInsured() {
        return insured;
    }
    
    public void setInsured(boolean insured) {
        this.insured = insured;
    }

    public ArrayList<Hand> getHands(){
        return hands;
    }

    public Hand getHand(int i) {
        return hands.get(i);
    }

    public int getMoney() {
        return money;
    }
    
    public void setMoney(int money) {
        this.money = money;
    }

    public int getBet() {
        return bet;
    }
}
