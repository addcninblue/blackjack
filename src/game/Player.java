package game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.StringJoiner;

/**
 * Represents a blackjack player.
 *
 * @author Addison Chan / Darian Nguyen / Daniel Phan
 * @version 2.1.17
 */
public class Player {
    private String name;
    private ArrayList<Hand> hands;
    private int money;
    private int bet; //the amount bet during a turn
    private boolean insured; //whether or not the player is insured

    /**
     * Constructs a new player with the given name
     * @param name the player's name
     */
    public Player(String name){
        this.name = name;
        hands = new ArrayList<>();
        money = 1000;
        bet = 0;
        insured = false;
    }

    /**
     * Constructs a new player with the given name and money
     * @param name the player's name
     * @param money the player's money
     */
    public Player(String name, int money){
        this(name);
        this.money = money;
    }

    /**
     * Constructs a new player with the given name, money, and hand
     * @param name the player's name
     * @param money the player's money
     * @param hands the player's hand
     */
    public Player(String name, int money, ArrayList<Hand> hands){
        this.name = name;
        this.hands = hands;
        this.money = money;
        bet = 0;
        insured = false;
    }

    /**
     * The player bets a certain amount of money.
     * @param betAmt the amount the player bets
     */
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

    /**
     * Prepares a player for the next round.
     */
    public void resetTurn() {
        hands.clear();
        hands.add(new Hand());
        bet = 0;
        insured = false;
    }

    /**
     * Returns a string representation of the player
     * @return a string representation of the player
     */
    @Override
    public String toString() {
        return String.format("Player: %s\nMoney: %d", name, money);
    }

    /**
     * Returns whether or not the player can split the hand.
     * @param hand the hand to split
     * @return if the player can split the hand
     */
    public boolean canSplitHand(Hand hand) {
        return hands.contains(hand) && hands.size() < 4
                && hand.isSplittable()&& getMoney() >= getBet();
    }

    /**
     * Returns whether or not the player double down.
     * @return if the player can double down
     */
    public boolean canDoubleDown() {
        Hand hand = hands.get(0);
        //has not split, has not hit, and has enough money
        return hands.size() == 1 && hand.count() == 2 && !hand.isBlackJack()
                && getMoney() >= getBet();
    }

    /**
     * Returns the player name
     * @return the player name
     */
    public String getName(){
        return name;
    }

    /**
     * Returns if the player is insured.
     * @return if the player is insured.
     */
    public boolean isInsured() {
        return insured;
    }

    /**
     * Updates the player's insured status
     * @param insured the new insured status
     */
    public void setInsured(boolean insured) {
        this.insured = insured;
    }

    /**
     * Returns the player's hands
     * @return the player's hands
     */
    public ArrayList<Hand> getHands(){
        return hands;
    }

    /**
     * Returns the hand at the given index
     * @param i index of the hand
     * @return the hand at the given index
     */
    public Hand getHand(int i) {
        return hands.get(i);
    }

    /**
     * Returns the player's cash.
     * @return the player's cash
     */
    public int getMoney() {
        return money;
    }

    /**
     * Sets the player's money
     * @param money the player's new money amount
     */
    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Returns the player's bet.
     * @return the player's bet amount
     */
    public int getBet() {
        return bet;
    }
}
