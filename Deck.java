import java.util.Random;

public class Deck {
    private Card[] deck; //the array of cards
    private int cardsLeft; //the number of cards left still not dealt

    public Deck() {
        Rank[] ranks = Rank.values();
        Suit[] suits = Suit.values();
        int i = 0;

        this.cardsLeft = ranks.length * suits.length;
        this.deck = new Card[this.cardsLeft];
        for (Suit s : suits) {
            for (Rank r : ranks) {
                this.deck[i++] = new Card(r, s, null);
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
        return this.cardsLeft > 0 ? this.deck[--this.cardsLeft] : null;
    }

    public void reshuffle() {
        this.cardsLeft = this.deck.length;
        shuffle();
    }

    public void shuffle() {
        Random rand = new Random();

        for (int i = 0; i < this.deck.length; i++) {
            int index = rand.nextInt(this.deck.length - i) + i;

            Card temp = this.deck[i];
            this.deck[i] = this.deck[index];
            this.deck[index] = temp;
        }
    }

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
