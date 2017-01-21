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
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import util.SpriteLoader;

/**
 *
 * @author Darian
 */
public class GamePanel extends JPanel implements Runnable {
    private Game game;
    
    public GamePanel() {
        super();
        init();
        String playerName = JOptionPane.showInputDialog("What is your name?");
        Player player = new Player(playerName);
        game = new Game();
        game.addPlayer(player);
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
        for (Player player : game.getPlayers()) {
            for (Hand hand : player.getHands()) {
                Controller controller = new Controller(game, hand);
                controller.setLocation(0, getHeight()/2);
                
                this.add(controller);
            }  
        }
        
        while (game.hasPlayers()) {
            //TODO
        }
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
