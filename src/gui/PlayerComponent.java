/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Card;
import game.Hand;
import game.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import util.Painter;
import static util.Painter.drawCenteredString;
import util.SpriteLoader;

/**
 * The JFrame component for a player.
 *
 * @author Darian
 * @version 2.1.17
 */
public class PlayerComponent extends JComponent {
    private final Player player;
    private ButtonsComponent buttonsCmp;

    private String[] results;

    /**
     * Constructs a new PlayerComponent with the given player.
     * @param player the component's player
     */
    public PlayerComponent(Player player) {
        init();
        this.player = player;
        buttonsCmp = null;
        results = null;
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 400));
        setMinimumSize(new Dimension(300, 360));
        setMaximumSize(getPreferredSize());

        add(Box.createRigidArea(new Dimension(200, 100))); //placeholder for controller
        add(Box.createRigidArea(new Dimension(0, 10))); //spacer
        add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;

                for (int i = 0; i < player.getHands().size(); i++) {
                    Hand hand = player.getHand(i);
                    final int Y_OFFSET = 10 + i*50;
                    //draw hand
                    for (int j = 0; j < hand.getCards().size(); j++) {
                        Card card = hand.getCard(j);
                        g2.drawImage(card.getImage(), getWidth()/4 + j*SpriteLoader.CARD_OFFSET,
                                     Y_OFFSET, null);
                    }
                    //draw results or pointer
                    if (results != null) {
                        drawCenteredString(g2, results[i], Color.ORANGE,
                            new Rectangle(0, Y_OFFSET, getWidth()/4, 50),
                            new Font("Courier", Font.BOLD, 25));
                    } else if (buttonsCmp instanceof Controller && ((Controller)buttonsCmp).getHand() == hand) {
                        g2.setColor(Painter.SOLARIZED_CYAN);
                        g2.fill(new Ellipse2D.Double(25, Y_OFFSET + 10, 30, 30));
                    }
                }
                //draw name
                Painter.drawCenteredString(g2, player.getName(), Color.ORANGE,
                        new Rectangle(0, getHeight() - 50, getWidth(), 50),
                        new Font("Courier", Font.PLAIN, 30));
                g2.drawImage(SpriteLoader.SOLARIZED_RECTANGLE, 0, 0,
                             getWidth(), getHeight(), null);
            }
        });
    }

    /**
     * Prepares the controller for the next blackjack round.
     */
    public void resetTurn() {
        player.resetTurn();
        results = null;
        buttonsCmp = null;
    }

    /**
     * Returns the component's player.
     * @return the component's player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Updates the PlayerComponent's controller.
     * @param buttonsCmp the new controller
     */
    public void setButtonsCmp(ButtonsComponent buttonsCmp) {
        this.buttonsCmp = buttonsCmp;
        this.remove(0);
        this.add(buttonsCmp, 0); //add to top
        buttonsCmp.startTurn();
        this.buttonsCmp = null;
    }

    /**
     * Sets the player's round results for this component.
     * @param results the player's round results
     */
    public void setResults(String[] results) {
        this.results = results;
    }
}
