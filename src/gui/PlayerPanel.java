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
 *
 * @author Darian
 */
public class PlayerPanel extends JComponent {
    private final Player player;
    private Controller controller;

    private String[] results;

    public PlayerPanel(Player player) {
        init();
        this.player = player;
        controller = null;
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
                    } else if (controller != null && controller.getHand() == hand) {
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

    public void setResults(String[] results) {
        this.results = results;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        //remove all but last 2 components
        for (int i = 0; i < getComponents().length - 2 ; i++) {
            remove(getComponent(i));
        }
        this.add(controller, 0); //add to top
        controller.startTurn();
        this.controller = null;
    }
}
