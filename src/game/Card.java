package game;

import java.awt.image.BufferedImage;

public class Card {
    public final Rank RANK;
    public final Suit SUIT;
    private int value;
    private boolean hidden;

    public Card(Rank rank, Suit suit) {
        RANK = rank;
        SUIT = suit;

        value = rank.VALUE;
        hidden = false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value; //all for that one ace
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return String.format("%s of %s", RANK, SUIT);
    }
}
