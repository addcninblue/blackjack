package game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck extends ArrayList<Card> {
    public Deck() {
        resetDeck();
    }

    private void resetDeck() {
        Rank[] ranks = Rank.values();
        Suit[] suits = Suit.values();

        clear();
        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                this.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Deals a card if there are cards left. Else, null is returned.
     * (Postcondition: deck_ is unchanged.)
     * @return a card if cards are left in the deck, null otherwise
     * (Precondition: deck_ and cardsLeft_ are initialized, and deck_ is non null)
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
}
