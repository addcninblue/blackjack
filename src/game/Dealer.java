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

    public Hand getHand() {
        return getHands().get(0);
    }

    public Card getFaceUpCard() {
        return getHand().getCard(0);
    }
}
