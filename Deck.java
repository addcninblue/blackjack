import java.util.Random;

public class Deck {
    public Deck() {
        Rank[] ranks = Rank.values();
        Suit[] suits = Suit.values();
        int i = 0;

        cardsLeft_ = ranks.length * suits.length;
        deck_ = new Card[cardsLeft_];
        for (Suit s : suits) {
            for (Rank r : ranks) {
                deck_[i++] = new Card(r, s);
            }
        }
    }

    /**
     * Deals a card if there are cards left. Else, null is returned.
     * (Postcondition: deck_ is unchanged.)
     * @return a card if cards are left in the deck, null otherwise
     * (Precondition: deck_ and cardsLeft_ are initialized, and deck_ is nonnull)
     */
    public Card deal() {
        return cardsLeft_ > 0 ? deck_[--cardsLeft_] : null;
    }

    public void reshuffle() {
        cardsLeft_ = deck_.length;
        shuffle();
    }

    public void shuffle() {
        Random rand = new Random();

        for (int i = 0; i < deck_.length; i++) {
            int index = rand.nextInt(deck_.length - i) + i;

            Card temp = deck_[i];
            deck_[i] = deck_[index];
            deck_[index] = temp;
        }
    }

    private Card[] deck_; //the array of cards
    private int cardsLeft_; //the number of cards left still not dealt

    public static void main(String[] args) { //just to check deck & card
        Deck d = new Deck();
        Card c;

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
