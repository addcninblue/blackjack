/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Card;
import game.Dealer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import util.SpriteLoader;

/**
 *
 * @author Darian
 */
public class DealerPanel extends JComponent {
    private Dealer dealer;

    private EndRoundButton endRoundBtn;
    public DealerPanel(Dealer dealer) {
        init();
        this.dealer = dealer;
    }

    private void init() {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(350, 170));
        endRoundBtn = new EndRoundButton();

        add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                final int CARD_OFFSET = 14;

                g2.drawImage(SpriteLoader.SOLARIZED_RECTANGLE, 0, 0, getWidth(), getHeight(), null);
                for (int i = 0; i < dealer.getHand().getCards().size(); i++) {
                    Card card = dealer.getHand().getCard(i);
                    int xOffset = (int)(getWidth()/2 - 30);
                    int yOffset = (int)(getHeight()/3);
                    g2.drawImage(card.getImage(), xOffset + i*CARD_OFFSET, yOffset, null);
                }
            }
        });
        add(endRoundBtn);
    }

    public synchronized void start() {
        repaint();
        endRoundBtn.start();
    }

    private class EndRoundButton extends JButton {
        public EndRoundButton() {
            init();
        }
        private void init() {
            setVisible(false);
            setSize(new Dimension(120, 60));
            setText("Next Round");
            addActionListener((ActionEvent) -> {
                stop();
            });
        }

        public synchronized void start() {
            setVisible(true);
            repaint();
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        public synchronized void stop() {
            setVisible(false);
            notify();
        }
    }
}
