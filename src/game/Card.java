package game;

import java.awt.image.BufferedImage;

public class Card {
    public final Rank RANK;
    public final Suit SUIT;
    private final BufferedImage FACE;
    private final BufferedImage BACK;
    private int value;
    private boolean hidden;
    
    public Card(Rank rank, Suit suit, BufferedImage image, BufferedImage back) {
        RANK = rank;
        SUIT = suit;
        FACE = image;
        BACK = back;
        
        value = rank.VALUE;
        hidden = false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value; //all for that one ace
    }
    
    public BufferedImage getImage() {
        return isHidden() ? BACK : FACE;
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
