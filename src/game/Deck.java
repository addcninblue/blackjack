package game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import util.SpriteLoader;

/**
 * A standard deck.
 *
 * @author Addison Chan / Darian Nguyen / Daniel Phan
 * @version 1.28.17
 */
public class Deck extends ArrayList<Card> {
    private BufferedImage[] cardImages;

    /**
     * Constructs a new deck with the standard 52 cards.
     */
    public Deck() {
        resetDeck();
    }

    private void resetDeck() {
        cardImages = new SpriteLoader("cards", 67, 95).cardImages;
        BufferedImage cardBack = cardImages[52];

        Rank[] ranks = Rank.values();
        Suit[] suits = Suit.values();
        int i = 0;

        clear();
        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                this.add(new Card(rank, suit, cardImages[i], cardBack));
                i++;
            }
        }
    }

    /**
     * Deals a card if there are cards left. Else, null is returned.
     * @return a card if cards are left in the deck, null otherwise
     */
    public Card deal() {
        return isEmpty() ? null : remove(0);
    }

    /**
     * Reshuffles the entire deck, putting used cards back into deck
     * Resuffle is for used decks, shuffle is for unused decks
     * (Postcondition: the deck is shuffled)
     * (Precondition: deck is used (ie. there are cards taken out of deck))
     */
    public void reshuffle() {
        resetDeck();
        shuffle();
    }

    /**
     * Shuffles the deck
     * (Postcondition: Deck is shuffled (randomized))
     * (Precondition: Brand new deck (just initialized))
     */
    public void shuffle() {
        Random rand = new Random();

        for (int i = 0; i < size(); i++) {
            int index = rand.nextInt(size() - i) + i;
            Collections.swap(this, i, index);
        }
    }

    /**
     * Returns the deck's card images.
     * @return the deck's card images
     */
    public BufferedImage[] getCardImages() {
        return cardImages;
    }
}
