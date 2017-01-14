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
     * Starts the game
     * (Postcondition: game is started)
     * (Precondition: game is initialized and people are added)
     */
    public ArrayList<Player> start(){
        Card card;
        for(Player player : players){
            for(int i = 0; i < 13; i++){
                player.addCard(deck.deal());
            }
        }
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
            if(players.get(i).getName() == name){
                Player playertoRemove = players.get(i);
                players.remove(i);
                return playertoRemove;
            }
        }
        return null;
    }

    public static void main (String [] args){
        Game game = new Game();
        game.addPlayer(new Player("add"));
        game.addPlayer(new Player("daniel"));
        game.addPlayer(new Player("darian"));
        game.addPlayer(new Player("lucy"));

        ArrayList<Player> players = game.start();

        for(Player player : players){
            for(Card card : player.getHand()){
                System.out.println(card.toString());
            }
            System.out.println();
        }

    }

}
