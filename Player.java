import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Card> hand;

    public Player(String name){
        this.name = name;
        hand = new ArrayList<Card>();
    }

    /**
     * Returns name
     * (Postcondition: name is returned)
     * (Precondition: none)
     */
    public String getName(){
        return name;
    }

    /**
     * Adds a given card to a player's hand
     * (Postcondition: card is added to hand)
	 * @param card card to add
     * (Precondition: card is a valid card)
     */
    public void addCard(Card card){
        hand.add(card);
    }

    /**
     * Returns the player's hand
     * (Postcondition: player hand is returned)
     * (Precondition: hand is initialized)
     * @return player's hand
     */
    public ArrayList<Card> getHand(){
        return hand;
    }
}
