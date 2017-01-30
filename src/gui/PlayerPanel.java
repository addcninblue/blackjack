/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Card;
import game.Game;
import game.Hand;
import game.Player;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.Queue;
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
    private final Queue<Controller> controllerQueue;
    private Controller controller;

    private String[] results;
    private boolean drawingResults;

    public PlayerPanel(Player player) {
        init();
        this.player = player;
        controllerQueue = new LinkedList<Controller>();
        controller = null;
        results = null;
        drawingResults = false;
    }

    private void init() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(256, 360));
        setMinimumSize(new Dimension(256, 360));
        setMaximumSize(getPreferredSize());
        add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.drawImage(SpriteLoader.SOLARIZED_RECTANGLE, 0, 0, getWidth(), getHeight(), null);
                //draw name
                Painter.drawCenteredString(g2, player.getName(), Color.ORANGE,
                        new Rectangle(0, getHeight() - 50, 150, 150),
                        new Font("Courier", Font.PLAIN, 30));

                for (int i = 0; i < player.getHands().size(); i++) {
                    Hand hand = player.getHand(i);
                    final int Y_OFFSET = 20 + i*50;
                    //draw hand
                    for (int k = 0; k < hand.getCards().size(); k++) {
                        Card card = hand.getCard(k);
                        g2.drawImage(card.getImage(), getWidth()/5+ k*SpriteLoader.CARD_OFFSET, Y_OFFSET, null);
                    }

                    if (results != null) {
                        drawCenteredString(g2, results[i], Color.ORANGE,
                            new Rectangle(0, Y_OFFSET, 80, 80),
                            new Font("Courier", Font.BOLD, 25));
                    } else if (controller != null && controller.isRunning()) {
                        g2.setColor(Painter.SOLARIZED_CYAN);
                        g2.fill(new Ellipse2D.Double(10, Y_OFFSET, 30, 30));
                    }
                }
            }
        }, BorderLayout.CENTER);
    }

    public void setResults(final String[] results) {
        this.results = results;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        this.add(controller, BorderLayout.PAGE_START);
        controller.startTurn();
        this.remove(controller);
    }

    public Queue<Controller> getControllerQueue() {
        return controllerQueue;
    }
}
