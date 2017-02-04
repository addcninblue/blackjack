package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringJoiner;
import game.Player;

/**
 * Represents a blackjack player.
 *
 * @author Addison Chan / Darian Nguyen / Daniel Phan
 * @version 2.1.17
 */
public class ServerPlayer extends Player implements Runnable {
    private Socket socket;
    BufferedReader input;
    PrintWriter output;
    int playerNumber;

    public ServerPlayer(Socket socket, int playerNumber){
        super();
        this.socket = socket;
        this.playerNumber = playerNumber;
        try {
            input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // gets name of player
        output.println("NAME");
        String name = "Player";
        try {
            name = input.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        super.setName(name);
        System.out.print(name);
    }

    public void bet(){
    }
}
