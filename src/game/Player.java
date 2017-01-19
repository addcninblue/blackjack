package game;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Hand> hands;
    private int money;
    private int bet; //the amount bet during a turn

    public Player(String name){
        this.name = name;
        hands = new ArrayList<>();
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

    public void bet(int betAmt) {
        if (betAmt > money || betAmt < 1) {
            throw new IllegalArgumentException(String.format("%s can't bet $%d\n", name, betAmt));
        }
        
        this.money -= betAmt;
        this.bet = betAmt;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void resetTurn() {
        hands.clear();
        hands.add(new Hand());
        if (money < bet) {
            bet = 1;
        }
    }

    /**
     * Returns whether or not a hand is splittable
     * (Postcondition: hands is unchanged)
     * @param i the index of the hand to split
     * @return whether hands(i) is splittable
     * (Precondition: i is a valid index in hands)
     */
    public boolean canSplitHand(int i) {
        return getHand(i).isSplittable() && getMoney() >= getBet();
    }

    public boolean canDoubleDown(int i) {
        return getHand(i).count() == 2 && getMoney() >= getBet();
    }

    /**
     *
     * @param c the card dealt to the hand
     * @param i the index of the hand to double down
     */
    public void doubleDown(Card c, int i) {
        if (!canDoubleDown(i)) {
            throw new IllegalArgumentException(
                    String.format("%s can't double down.\n", name)
            );
        }

        bet(getBet());
        getHand(i).addCard(c);
        getHand(i).setDoubleDowned();
    }

    /**
     * Splits a hand
     * (Postcondition: the hand at index i is split)
     * @param i the index of the hand in hands to split
     * (Precondition: i is an index in hands)
     */
    public void splitHand(int i) {
        if (!getHand(i).isSplittable()) {
            throw new IllegalArgumentException(name + " can't split.\n");
        }
        Hand h1 = new Hand();
        Hand h2 = new Hand();
        Hand original = hands.get(i);
        h1.addCard(original.getCard(0));
        h2.addCard(original.getCard(1));
        hands.add(i, h1);
        hands.add(i, h2);
        hands.remove(original);
        //too lazy to make a removeCard function
    }

    @Override
    public String toString() {
        return String.format("Player: %s\nMoney: %d", name, money);
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
