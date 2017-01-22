package game;

import javax.swing.SwingUtilities;

/**
 * @author Daniel Phan
 */
public class Dealer extends Player {
    Deck deck;

    public Dealer(String name) {
        super(name);
        deck = new Deck();
        deck.shuffle();
    }

    public Card deal() {
        if (deck.getCardCount() <= 0) {
            deck.reshuffle();
        }
        Card card = deck.deal();
        //card.setHidden(false);
        return card;
    }

    public void hideHand() {
        getHand().getCard(0).setHidden(true);
    }

    public void unhideHand() {
        for (Card card : getHand()) {
            card.setHidden(false);
        }
    }

    public Hand getHand() {
        return getHands().get(0);
    }

    public Card getFaceUpCard() {
        return getHand().getCard(1);
    }

    public Deck getDeck() {
        return deck;
    }
}
