package game;

import java.awt.image.BufferedImage;
import java.util.Random;
import util.SpriteLoader;

public class Deck {
    private Card[] deck; //the array of cards
    private int cardsLeft; //the number of cards left still not dealt

    public Deck() {
        Rank[] ranks = Rank.values();
        Suit[] suits = Suit.values();
        BufferedImage[] cardImages = new SpriteLoader("cards").cardImages;
        this.cardsLeft = ranks.length * suits.length;
        this.deck = new Card[this.cardsLeft];
        
        int i = 0;
        for (Suit suit : suits) {
            for (Rank rank : ranks) {
                this.deck[i] = new Card(rank, suit, cardImages[i]);
                i++;
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
        return this.cardsLeft > 0 ? this.deck[--this.cardsLeft] : null;
    }

    /**
     * Reshuffles the entire deck, putting used cards back into deck
     * Resuffle is for used decks, shuffle is for unused decks
     * (Postcondition: the deck is shuffled)
     * (Precondition: deck is used (ie. there are cards taken out of deck))
     */
    public void reshuffle() {
        this.cardsLeft = this.deck.length;
        shuffle();
    }

    /**
     * Shuffles the deck
     * (Postcondition: Deck is shuffled (randomized))
     * (Precondition: Brand new deck (just initialized))
     */
    public void shuffle() {
        Random rand = new Random();

        for (int i = 0; i < this.deck.length; i++) {
            int index = rand.nextInt(this.deck.length - i) + i;

            Card temp = this.deck[i];
            this.deck[i] = this.deck[index];
            this.deck[index] = temp;
        }
    }

    public static void main(String[] args) { //just to check deck & card delete for production
        Deck d = new Deck();
        Card c;
        while ((c = d.deal()) != null) {
            System.out.println(c);
        }
        d.shuffle();

        while ((c = d.deal()) != null) {
            System.out.println(c);
        }
        System.out.println();

        d.reshuffle();

        while ((c = d.deal()) != null) {
            System.out.println(c);
        }
    }
}
