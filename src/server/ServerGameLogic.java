package server;

import game.*;
import gui.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import util.Painter;
import util.SpriteLoader;


/**
 * The GUI that runs a Blackjack ServerGame. Logic runs on a different thread.
 * @author Darian
 */
public class ServerGameLogic implements Runnable {
    private final ServerGame serverGame;

    public ServerGameLogic(ServerGame serverGame){
        this.serverGame = serverGame;
    }

    /**
     * Starts the ServerGameLogic
     */
    public void start() {
        Thread logicThread = new Thread(this);
        logicThread.start();
    }

    /**
     * The main serverGame loop.
     * NOTE: Generally, methods called from run() should not also call render().
     * - Only run() should call render().
     * - Single exception is when Dealer has his turn, that is OK.
     */
    @Override
    public void run() {
        Dealer dealer = serverGame.getDealer();
        List<ServerPlayer> players = serverGame.getPlayers();
        Map<ServerPlayer, Integer> playerToPreviousBet = new HashMap<ServerPlayer, Integer>();

        for (int i = 0; i < players.size(); i++) {
            ServerPlayer player = players.get(i);
        }

        while (serverGame.hasPlayers()) {

            // gets new hand for everyone
            serverGame.newRound();
            for(ServerPlayer player : serverGame.getPlayers()) {
                player.getOutput().println("NEW ROUND"); // TODO implies hand is empty
            }

            // gets bets from users
            for (ServerPlayer player : players) {
                int previousBet = playerToPreviousBet.getOrDefault(player, 0);
                if (player.getMoney() < previousBet) {
                    previousBet = 0;
                }
                playerToPreviousBet.put(player, getBetFromPlayer(player, previousBet));
            }

            // send bets to users
            for (ServerPlayer player : serverGame.getPlayers()) {
                sendToAllPlayers(String.format("PLAYER%d BET %d", player.getPlayerNumber(), player.getBet()));
            }

            serverGame.initialDeal();

            // send all cards to users
            sendToAllPlayers("HANDS");
            for (ServerPlayer player : serverGame.getPlayers()) {
                sendToAllPlayers("PLAYER" + player.getPlayerNumber()); // PLAYER#
                int indexOfHand = 1;
                for (Hand hand : player.getHands()){
                    sendToAllPlayers("HAND" + indexOfHand); // HAND#
                    for (Card card : hand) {
                        player.getOutput().format("CARD %s\n", card.toSuitRank()); // CARD[SUIT][VALUE]
                    }
                }
            }

//            for (ServerPlayer player : players) {
//                runPlayerTurn(player);
//            }
//            unhideAllHands();

            runDealerTurn(dealer);
//            sendDealerCards(); // TODO

            for (ServerPlayer player : players){
                processBets(player);
            }
//            sendBetResult();

            List<ServerPlayer> peopleRemoved = serverGame.removeMoneyless();
            for (ServerPlayer player : peopleRemoved) {
                player.getOutput().println(String.format("%s removed from serverGame.", player.getPlayerName()));
            }
//            serverGame.saveGame(menu.database, "autosave");
//            dealerCmp.getEndRoundBtn().start();
        }

//        try {
//            menu.database.deleteTable("autosave");
//        } catch(Exception e){
//            e.printStackTrace();
//        }

    }


    private int getBetFromPlayer(ServerPlayer player, int previousBet) {
        String betMsg = String.format("\n\n%s's bet ($%d left): ",
                                      player.getPlayerName(), player.getMoney());
        while (player.getBet() == 0) {
            try {
                player.getOutput().format("PLAYER MONEY %d LEFT\n", player.getMoney());
                int betAmt = Integer.parseInt(player.getInput().readLine());
                player.bet(betAmt);
                return betAmt;
            } catch (IOException e){
                player.getOutput().println("ILLEGAL");
            } catch (IllegalArgumentException e) {
                player.getOutput().println("ILLEGAL");
            }
        }
        return -1;
    }

    private void runPlayerTurn(ServerPlayer player) {
        for (int i = 0; i < player.getHands().size(); i++) { //foreach causes CM exception
            Hand hand = player.getHand(i);
            // TODO implement logic for getting user actions
        }
    }

    private void unhideAllHands() {
        Dealer dealer = serverGame.getDealer();
        List<ServerPlayer> players = serverGame.getPlayers();

        dealer.getHand().unhideCards();
        for (ServerPlayer player : players) {
            for (Hand hand : player.getHands()) {
                hand.unhideCards();
            }
        }
    }

    private void runDealerTurn(Dealer dealer) {
        while (!dealer.getHand().isOver16()) {
            serverGame.hit(dealer.getHand());
        }
    }

    private void processBets(ServerPlayer player) {
        List<String> results = new ArrayList<String>(player.getHands().size());
        for (Hand hand : player.getHands()) {
            results.add(serverGame.getResult(hand));
        }

        //TODO logic to set results of bets and send to clients
        serverGame.payBet(player);
    }

    private void sendToAllPlayers(String str){
        for(ServerPlayer player : serverGame.getPlayers()){
            player.getOutput().println(str);
        }
    }
}
