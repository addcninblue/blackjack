//ADD IMAGES LATER
public class Card {
    public Card(Rank rank, Suit suit) {
        rank_ = rank;
        suit_ = suit;
    }

    public Rank getRank() {
        return rank_;
    }

    public Suit getSuit() {
        return suit_;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", rank_, suit_);
    }

    Rank rank_;
    Suit suit_;
}
