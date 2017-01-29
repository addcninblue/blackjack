package game;

import gui.Controller;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A BlackJack hand.
 *
 * @author Addison Chan / Darian Nguyen / Daniel Phan
 * @version 1.28.17
 */
public class Hand implements Iterable<Card>{
    private ArrayList<Card> cards;

    /**
     * Constructs a new hand with no cards.
     */
    public Hand() {
        cards = new ArrayList<>();
    }

    /**
     * Adds a card to the hand.
     * @param card the card to add
     */
    public void addCard(Card card){
        if (card.RANK == Rank.ACE && getValue() <= 10) {
            card.setValue(11);
        }
        cards.add(card);
    }

    /**
     * Demotes all the aces in the hand.
     */
    public void demoteAces() {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getValue() == 11) { //only promoted aces will have 11
                cards.get(i).setValue(1); //magic numbers
            }
        }
    }

    /**
     * Unhides all cards in the hand.
     */
    public void unhideCards() {
        for (Card card : cards) {
            card.setHidden(false);
        }
    }

    /**
     * Returns an iterator for the hand.
     * @return an iterator for the hand
     */
    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    /**
     * Returns the string representation of the hand.
     * @return the string representation of the hand
     */
    @Override
    public String toString() {
        String msg = "";
        for (Card card : this) {
            msg += card + " ";
        }
        return msg;
    }

    /**
     * Returns the hand's total.
     * @return the hand's total
     */
    public int getValue() {
        int sum = 0;
        for (Card c : cards) {
            sum += c.getValue();
        }
        return sum;
    }

    /**
     * Returns whether or not the hand is over 21
     * @return whether or not the hand is over 21
     */
    public boolean isOver21() {
        return getValue() > 21;
    }

    /**
     * Returns whether or not the hand is over 16
     * @return whether or not the hand is over 16
     */
    public boolean isOver16() { //only matters for dealer
        return getValue() > 16;
    }

    /**
     * Returns whether or not the hand is a blackjack
     * @return whether or not the hand is a blackjack
     */
    public boolean isBlackJack() {
        return cards.size() == 2 && getValue() == 21;
    }

    /**
     * Returns whether or not the hand is splittable
     * @return whether or not the hand is splittable
     */
    public boolean isSplittable() {
        return cards.size() == 2 && cards.get(0).RANK == cards.get(1).RANK;
    }

    /**
     * Returns the number of cards in the hand.
     * @return the number of cards in the hand
     */
    public int count() {
        return cards.size();
    }

    /**
     * Returns a card in the hand.
     * @param i the index of the card to get
     * @return the card at index i
     */
    public Card getCard(int i) {
        return cards.get(i);
    }

    /**
     * Returns the hand's cards.
     * @return the cards in the hand
     */
    public ArrayList<Card> getCards() {
        return cards;
    }
}
