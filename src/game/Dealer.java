package game;

/**
 * @author Daniel Phan
 */
public class Dealer {
    Deck deck;
    Hand hand;
    public Dealer() {
        deck = new Deck();
        deck.shuffle();
        this.hand = new Hand();
    }

    public Card deal() {
        if (deck.getCardCount() <= 0) {
            deck.reshuffle();
        }
        Card card = deck.deal();

        return card;
    }

    public void resetTurn() {
        hand = new Hand();
    }

    public void hideHand() {
        hand.getCard(0).setHidden(true);
    }

    public Hand getHand() {
        return hand;
    }

    public Card getFaceUpCard() {
        return hand.getCard(1);
    }

    public Deck getDeck() {
        return deck;
    }
}
