package game;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Daniel Phan
 */
public class Hand implements Iterable<Card>{
    private ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }


    /**
     * Adds a given card to a player's hand
     * (Postcondition: card is added to hand)
     * @param card card to add
     * (Precondition: card is a valid card)
     */
    public void addCard(Card card){
        if (card.RANK == Rank.ACE && getTotal() <= 10) {
            card.setValue(11);
        }
        cards.add(card);
    }

    public int getTotal() {
        int sum = 0;
        for (Card c : cards) {
            sum += c.getValue();
        }
        return sum;
    }

    public boolean isOver21() {
        return getTotal() > 21;
    }

    public boolean isOver16() { //only matters for dealer
        return getTotal() > 16;
    }

    public boolean isBlackJack() {
        return cards.size() == 2 && getTotal() == 21;
    }

    public boolean isSplittable() {
        return cards.size() == 2 && cards.get(0).RANK == cards.get(1).RANK;
    }

    public void demoteAces() {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getValue() == 11) { //only promoted aces will have 11
                cards.get(i).setValue(1); //magic numbers
            }
        }
    }

    public int count() {
        return cards.size();
    }

    public Card getCard(int i) {
        return cards.get(i);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
}
