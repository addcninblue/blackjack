package game;

import gui.Controller;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Daniel Phan
 */
public class Hand implements Iterable<Card>{
    private ArrayList<Card> cards;
    private boolean isDoubleDowned;

    public Hand() {
        cards = new ArrayList<>();
        isDoubleDowned = false;
    }
    
    public void addCard(Card card){
        if (card.RANK == Rank.ACE && getValue() <= 10) {
            card.setValue(11);
        }
        cards.add(card);
    }
    
     public void demoteAces() {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getValue() == 11) { //only promoted aces will have 11
                cards.get(i).setValue(1); //magic numbers
            }
        }
    }
     
    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
    
    @Override
    public String toString() {
        String msg = "";
        for (Card card : this) {
            msg += card + " ";
        }
        return msg;
    }
    public int getValue() {
        int sum = 0;
        for (Card c : cards) {
            sum += c.getValue();
        }
        return sum;
    }

    public boolean isDoubleDowned() {
        return isDoubleDowned;
    }

    public boolean isOver21() {
        return getValue() > 21;
    }

    public boolean isOver16() { //only matters for dealer
        return getValue() > 16;
    }

    public boolean isBlackJack() {
        return cards.size() == 2 && getValue() == 21;
    }

    public boolean isSplittable() {
        return cards.size() == 2 && cards.get(0).RANK == cards.get(1).RANK;
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

    public void setDoubleDowned() {
        isDoubleDowned = true;
    }
}
