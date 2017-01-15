package game;

import java.awt.image.BufferedImage;

public class Card {
    public final Rank RANK;
    public final Suit SUIT;
    public final BufferedImage IMAGE;
    private int value;

    public Card(Rank rank, Suit suit, BufferedImage image) {
        RANK = rank;
        SUIT = suit;
        IMAGE = image;
        value = rank.VALUE;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value; //all for that one ace
    }

    @Override
    public String toString() {
        return String.format("%s of %s", RANK, SUIT);
    }
}
