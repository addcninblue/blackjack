package gui;

import game.Game;
import game.Player;
import game.ServerPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private Game game;
    private List<Player> players;
    private List<Client> clients;

    public Server(){
        Scanner in = new Scanner(System.in);
        try {
            ServerSocket listener = new ServerSocket(25565);
            System.out.println("Server has started");
            System.out.println("Enter the number of players: ");
            int numOfPlayers = in.nextInt();
            in.nextLine();

             for(int i = 0; i < numOfPlayers; i++){
                 Player player = new Player();
                 this.clients.add(new ServerPlayer(listener.accept()), )
             }

        } catch (IOException e){
            e.printStackTrace();
        }

        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

    public void startGame(){
        this.game = new Game(players);
    }

    class Client extends Thread {
        Socket socket;
        BufferedReader input;
        PrintWriter output;
        Player player;

        Client(Socket socket, Player player){
            this.socket = socket;
            this.player = player;
            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("You have connected to the server");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
