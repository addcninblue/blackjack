package game;

import java.awt.image.BufferedImage;

public class Card {
    public final Rank rank;
    public final Suit suit;
    public final BufferedImage image;

    public Card(Rank rank, Suit suit, BufferedImage image) {
        this.rank = rank;
        this.suit = suit;
        this.image = image;
    }
    
    @Override
    public String toString() {
        return String.format("%s of %s", this.rank, this.suit);
    }
}
