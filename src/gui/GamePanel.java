/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Card;
import game.Dealer;
import game.Game;
import game.Hand;
import game.Player;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import util.SpriteLoader;

import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author Darian
 */
public class GamePanel extends JPanel implements Runnable {
    private Game game;
    
    public GamePanel() {
        super();
        init();

        game = new Game();
        int playerCount = Integer.parseInt(JOptionPane.showInputDialog("How many players (1-4)?"));
        for(int i = 0; i < playerCount; i++){
            String playerName = JOptionPane.showInputDialog("What is player " + (i+1) + "'s name?");
            Player player = new Player(playerName);
            game.addPlayer(player);
        }
        start(); //make sure this is always the last line of the constructor!
    }
    public GamePanel(List<Player> players) {
        super();
        init();
        
        game = new Game(players);
        start();
    }
    
    private void init() {
        setLayout(null);
        setVisible(true);
    }
    private void start() {
        Thread logicThread = new Thread(this); 
        logicThread.start();
    }
    
    @Override
    public void run() {
        game.newRound();
        game.initialDeal();
        
        Dealer dealer = game.getDealer();
        List<Player> players = game.getPlayers();
        
        while (game.hasPlayers()) {
            game.newRound();
            for (Player player : players) {
                Scanner input = new Scanner(System.in);
                while (true) {
                    String betAmt = JOptionPane.showInputDialog(
                            String.format("\n\n%s's bet ($%d left): ", player.getName(), player.getMoney()));

                    try {
                        player.bet(Integer.parseInt(betAmt));
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
            
            game.initialDeal();
            update();
            
            //player turns
            for (Player player : players) {
                for (Hand hand : player.getHands()) {
                    Controller controller = new Controller(game, hand);
                    controller.setLocation(900, getHeight()/2);
                    this.add(controller);
                    
                    controller.startTurn();
                    System.out.println("HAND: " + hand.toString());
                }  
            }
            
            //dealer turn
            dealer.unhideHand();
            update();
            while (!dealer.getHand().isOver16()) {
                update();
                Card card = game.hit(dealer.getHand());
            }
            
            //get results
            for (Player player : players) {
                for (Hand hand : player.getHands()) {
                    JOptionPane.showMessageDialog(null, String.format("%s %s", player.getName(), game.getResult(hand)));
                    System.out.format("%s %s\n", player.getName(), game.getResult(hand));
                }
                game.payBet(player);
            }

            ArrayList<Player> peopleRemoved = game.removeMoneyless();
            for (Player player : peopleRemoved) {
                JOptionPane.showMessageDialog(null, String.format("%s removed from game.", player.getName()));
                // System.out.printf("%s removed from game.\n", player.getName());
            }
            update(500);
        }
    }
    
    private void update(int delay) {
        repaint();
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
    }
    
    private void update() {
        update(200);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(SpriteLoader.TABLE_TOP, 0, 0, getWidth(), getHeight(), null);
        
        final int CARD_OFFSET = 14;
        Dealer dealer = game.getDealer();
        for (int i = 0; i < dealer.getHand().getCards().size(); i++) {
            g2.drawImage(dealer.getHand().getCard(i).getImage(), getWidth()/2 + i*CARD_OFFSET, getHeight()/8, null);
        }
        
        int playerCounter = 0;
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            for (int j = 0; j < player.getHands().size(); j++) {
                Hand hand = player.getHand(j);
                for (int k = 0; k < hand.getCards().size(); k++) {
                    Card card = hand.getCard(k);
                    g2.drawImage(card.getImage(), 50 + i*60 + k*CARD_OFFSET, j * 50, null);
                }
            }
        }
    }
}
