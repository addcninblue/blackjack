package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private ServerGame serverGame;
    private ServerGameLogic serverGameLogic;
    private List<ServerPlayer> serverPlayers;

    public Server(){
        Scanner in = new Scanner(System.in);
        try {
            System.out.println("Server has started");
//            System.out.println("Enter the number of players: ");
//            int numOfPlayers = in.nextInt();
//            in.nextLine();
            int numOfPlayers = 2; // change later
            ServerSocket listener = new ServerSocket(25565);
            this.serverPlayers = new ArrayList<>();

            for(int i = 0; i < numOfPlayers; i++){
                this.serverPlayers.add(new ServerPlayer(listener.accept(), i + 1));
            }

            // need 2 for loops because one to initialize all players and one to start
            for(ServerPlayer serverplayer : serverPlayers){
                Thread thread = new Thread(serverplayer);
                thread.start();
            }

            serverPlayers.get(0).bet();

        } catch (IOException e){
            e.printStackTrace();
        }
        this.serverGame = new ServerGame(serverPlayers);
        this.serverGameLogic = new ServerGameLogic(serverGame);
        this.serverGameLogic.run();
    }

    public void addPlayer(ServerPlayer player){
        this.serverPlayers.add(player);
    }

    public void startGame(){
        this.serverGame = new ServerGame(serverPlayers);
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
