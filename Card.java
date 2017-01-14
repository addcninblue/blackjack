import java.awt.Image;

public class Card {
    Rank rank;
    Suit suit;
    Image image;

    public Card(Rank rank, Suit suit, Image image) {
        this.rank = rank;
        this.suit = suit;
        this.image = image;
    }

    public Rank getRank() {
        return this.rank;
    }

    public Suit getSuit() {
        return this.suit;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.rank, this.suit);
    }
}
