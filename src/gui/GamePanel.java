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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import util.SpriteLoader;


/**
 *
 * @author Darian
 */
public class GamePanel extends JPanel implements Runnable {
    private Game game;
    private EndRoundButton endRoundBtn;
    public GamePanel() {
        super();
        init();

        game = new Game();
        game.addPlayer(new Player("Darian"));
    }
    public GamePanel(List<Player> players) {
        super();
        init();

        game = new Game(players);
    }

    private void init() {
        setLayout(null);

        endRoundBtn = new EndRoundButton();
        add(endRoundBtn);
    }

    public void start() {
        Thread logicThread = new Thread(this);
        logicThread.start();
    }

    @Override
    public void run() {
        setVisible(true);

        Dealer dealer = game.getDealer();
        List<Player> players = game.getPlayers();

        Map<Player, Integer> playerToPreviousBet = new HashMap<Player, Integer>();
        
        while (game.hasPlayers()) {
            game.newRound();

            for (Player player : players) {
                getBetFromPlayer(player, playerToPreviousBet);
            }

            game.initialDeal();
            update();

            //player turns
            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                for (int j = 0; j < player.getHands().size(); j++) {
                    Hand hand = player.getHand(j);
                    Controller controller = new Controller(game, player, hand);

                    int xOffset = (int)(0.015625*getWidth());
                    int yOffset = getHeight()/2 - 30;
                    controller.setLocation(xOffset + i*300, yOffset);

                    this.add(controller);
                    controller.startTurn();
                    this.remove(controller);
                }
            }

            //dealer turn
            for (Player player : players) {
                player.unhideHand();
            }
            dealer.unhideHand();

            update();
            while (!dealer.getHand().isOver16()) {
                Card card = game.hit(dealer.getHand());
                update();
            }
            //get results
            for (Player player : players) {
                for (Hand hand : player.getHands()) {
                    //JOptionPane.showMessageDialog(null, String.format("%s %s", player.getName(), game.getResult(hand)));
                    System.out.format("%s %s\n", player.getName(), game.getResult(hand));
                }
                game.payBet(player);
            }

            List<Player> peopleRemoved = game.removeMoneyless();
            for (Player player : peopleRemoved) {
                JOptionPane.showMessageDialog(null, String.format("%s removed from game.", player.getName()));
                // System.out.printf("%s removed from game.\n", player.getName());
            }

            endRoundBtn.start();
        }
    }

    private void getBetFromPlayer(Player player, Map<Player, Integer> playerToPreviousBet) {
        String betMsg = String.format("\n\n%s's bet ($%d left): ",
                                      player.getName(), player.getMoney());

        int previousBet = playerToPreviousBet.getOrDefault(player, 0);
        if (player.getMoney() < previousBet) {
            previousBet = 0;
        }

        while (player.getBet() == 0) {
            try {
                int betAmt = Integer.parseInt(JOptionPane.showInputDialog(betMsg,
                         previousBet == 0 ? null : previousBet));
                player.bet(betAmt);
                playerToPreviousBet.put(player, betAmt);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "That is not a valid number!",
                        "Invalid bet!", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Invalid bet!", JOptionPane.ERROR_MESSAGE);
            }
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
        endRoundBtn.setLocation(getWidth()/2 - 80, getHeight()/3);
        g2.drawImage(SpriteLoader.TABLE_TOP, 0, 0, getWidth(), getHeight(), null);

        //draw deck
        final int CARD_OFFSET = 14;
        Dealer dealer = game.getDealer();
        for (int i = 0; i < dealer.getDeck().getCardCount(); i++) {
            Card card = dealer.getDeck().getCards()[i];

            //rotate graphics to draw
            Graphics2D g3 = (Graphics2D)g2.create();
            AffineTransform at = new AffineTransform();
            at.setToRotation(0.785, 180, 160);
            g3.setTransform(at);
            g3.drawImage(card.getBack(), (int)Math.floor(180-i/3), (int)Math.floor(140-i/3), null);
            g3.dispose();
        }

        //draw dealer's hand
        for (int i = 0; i < dealer.getHand().getCards().size(); i++) {
            int xOffset = (int)(getWidth()/2 - 50);
            int yOffset = (int)(getHeight()/8);
            g2.drawImage(dealer.getHand().getCard(i).getImage(), xOffset + i*CARD_OFFSET, yOffset, null);
	}
        int playerCounter = 0;
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            for (int j = 0; j < player.getHands().size(); j++) {
                Hand hand = player.getHand(j);
                for (int k = 0; k < hand.getCards().size(); k++) {
                    Card card = hand.getCard(k);

                    int xOffset = (int)(0.0625 * getWidth());
                    int yOffset = (int)(0.125*getHeight()) + getHeight()/2;
                    g2.drawImage(card.getImage(), xOffset + i*300 + k*CARD_OFFSET, yOffset + j * 50, null);
                }
            }
        }
        /*drawCenteredString(g2, game.getResult(dealer.getHand()),
                new Rectangle(0, 0, getWidth(), getHeight()), new Font("Cambria", Font.PLAIN, 20));*/
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (rect.width - metrics.stringWidth(text)) / 2;
        int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
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
