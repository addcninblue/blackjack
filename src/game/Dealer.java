package game;

/**
 * The BlackJack dealer.
 *
 * @author Addison Chan / Darian Nguyen / Daniel Phan
 * @version 1.28.17
 */
public class Dealer {
    private Deck deck;
    private Hand hand;

    /**
     * Constructs a new dealer.
     */
    public Dealer() {
        deck = new Deck();
        deck.shuffle();
        this.hand = new Hand();
    }

    /**
     * Deals a card from the dealer's deck,
     * and reshuffles if the deck is empty.
     * @return a card
     */
    public Card deal() {
        if (deck.isEmpty()) {
            deck.reshuffle();
        }
        Card card = deck.deal();

        return card;
    }

    /**
     * Empties the dealer's hand.
     */
    public void resetTurn() {
        hand = new Hand();
    }

    /**
     * Hides the dealer's first card.
     */
    public void hideHand() {
        hand.getCard(0).setHidden(true);
    }

    /**
     * Returns the dealer's hand.
     * @return the dealer's hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Returns the dealer's face-up card.
     * @return the dealer's face-up card
     */
    public Card getFaceUpCard() {
        return hand.getCard(1);
    }

    /**
     * Returns the dealer's deck.
     * @return the dealer's deck
     */
    public Deck getDeck() {
        return deck;
    }
}
