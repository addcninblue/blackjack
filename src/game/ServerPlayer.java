package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringJoiner;

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

    public ServerPlayer(Socket socket){
        this.socket = socket;
        String name = "";
        try{
            name = input.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.print(name);
        super(name);
    }
}
