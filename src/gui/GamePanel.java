/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Dealer;
import game.Game;
import game.Player;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import util.SpriteLoader;

/**
 *
 * @author Darian
 */
public class GamePanel extends JPanel {
    private final static int CARD_OFFSET = 14;
    private Game game;
    public GamePanel() {
        setLayout(null);
        game = new Game();
        game.addPlayer(new Player("DAR"));
        
        game.newRound();
        for (Player player : game.getPlayers()) {
            player.resetTurn();
            Controller controller = new Controller(game, player.getHand(0));
            this.add(controller);
            Dimension size = controller.getPreferredSize();
            controller.setBounds(0, 0, size.width, size.height);
            
            
        }
        game.initialDeal();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(SpriteLoader.TABLE_TOP, 0, 0, getWidth(), getHeight(), null);
        
        Dealer dealer = game.getDealer();
        for (int i = 0; i < dealer.getHand().getCards().size(); i++) {
            g2.drawImage(dealer.getHand().getCard(i).getImage(), getWidth()/2 + i*CARD_OFFSET, getHeight()/8, null);
        }
    }
}
