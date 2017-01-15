import java.util.ArrayList;

public class Game {
    private Deck deck;
    private ArrayList<Player> players;

    public Game(){
        deck = new Deck();
        deck.shuffle();
        this.players = new ArrayList<Player>();
    }

    /**
     * Returns an arraylist of the game's players
     * (Postcondition: an arraylist of the game's players is returned)
     * @return an arraylist of the game's players
     * (Precondition: game is initialized and people are added)
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }

    /**
     * Adds player to the game
     * (Postcondition: added to arraylist players)
	 * @param player player to add
     * (Precondition: player is a valid player)
     */
    public void addPlayer(Player player){
        players.add(player);
    }

    /**
     * Removes player with name name
     * (Postcondition: person is removed from players)
	 * @param name name of player to remove
     * (Precondition: name is a valid name of a person)
     * @return the Player removed.
     *         null => couldn't find the player
     */
    public Player removePlayer(String name){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getName().equals(name)){
                Player playertoRemove = players.get(i);
                players.remove(i);
                return playertoRemove;
            }
        }
        return null;
    }

    /**
     * Hits a player
     * (Postcondition: card is added to a player)
     * @param the player to hit
     * (Precondition: none)
     * @return the card added
     */
    public Card hit(Player playertoHit){
        for(Player player : players){
            if(player == playertoHit){
                Card card = deck.deal();
                player.addCard(card);
                return card;
            }
        }
        return null;
    }

    public static void main (String [] args){
        Game game = new Game();

        Player player1 = new Player("add");
        Player player2 = new Player("daniel");
        Player player3 = new Player("darian");
        Player player4 = new Player("lucy");
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);

        ArrayList<Player> players = game.getPlayers();

        for(Player player : players){
            Card c = game.hit(player);
            System.out.format("hit: %s\n", c.toString());
            System.out.println(player.getName());
            for(Card card : player.getHand()){
                System.out.println(card.toString());
            }
            System.out.println();
        }

    }

}
