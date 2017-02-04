package game;

import java.awt.image.BufferedImage;

/**
 * Represents a standard card.
 *
 * @author Addison Chan / Darian Nguyen / Daniel Phan
 * @version 1.28.17
 */
public class Card {
    /**
     * The card's rank.
     */
    public final Rank RANK;
    /**
     * The card's suit.
     */
    public final Suit SUIT;
    private final BufferedImage FACE;
    private final BufferedImage BACK;
    private int value;
    private String suitName;
    private boolean hidden;

    /**
     * Constructs a new card with the given rank, suit, and images.
     * @param rank the card's rank
     * @param suit the card's suit
     * @param image the card's front image
     * @param back the card's back image
     */
    public Card(Rank rank, Suit suit, BufferedImage image, BufferedImage back) {
        RANK = rank;
        SUIT = suit;
        FACE = image;
        BACK = back;

        value = rank.VALUE;
        suitName = suit.toString().substring(0,1);
        hidden = false;
    }

    /**
     * Returns the card's value.
     * @return the card's value
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the card's value
     * @param value the card's new value
     */
    public void setValue(int value) {
        this.value = value; //all for that one ace
    }

    /**
     * Returns the card's front or back image, depending on if it's hidden.
     * @return the card's front or back image, depending on if it's hidden
     */
    public BufferedImage getImage() {
        return isHidden() ? BACK : FACE;
    }

    /**
     * Returns the card's back image.
     * @return the card's back image
     */
    public BufferedImage getBack() {
        return BACK;
    }

    /**
     * Returns whether or not the card is hidden.
     * @return whether or not the card is hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Changes whether or not the card is hidden.
     * @param hidden whether or not the card is hidden
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    /**
     * Returns the card as a string.
     * @return the card as a string
     */
    @Override
    public String toString() {
        return String.format("%s of %s", RANK, SUIT);
    }

    public String toSuitRank() {
        return suitName + value;
    }
}
