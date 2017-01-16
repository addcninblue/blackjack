package game;

/**
 * @author Daniel Phan
 */
public class Dealer extends Player{
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
        return deck.deal();
    }

    public boolean isOver16() {
        return getHandTotal() > 16;
    }

    public Card getFaceUpCard() {
        return getHand().get(0);
    }
}
