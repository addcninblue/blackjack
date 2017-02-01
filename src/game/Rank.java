package game;

/**
 * The possible ranks for cards.
 */
public enum Rank {
    ACE(1),
    KING(10),
    QUEEN(10),
    JACK(10),
    TEN(10),
    NINE(9),
    EIGHT(8),
    SEVEN(7),
    SIX(6),
    FIVE(5),
    FOUR(4),
    THREE(3),
    TWO(2);

    /**
     * The rank's value.
     */
    public final int VALUE;

    /**
     * Constructs a new rank with the given value.
     * @param value the rank's value
     */
    Rank(int value) {
        VALUE = value;
    }
}
