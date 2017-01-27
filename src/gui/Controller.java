package gui;

import game.Card;
import game.Game;
import game.Hand;
import game.Player;
import game.Rank;
import game.Suit;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Represents a Controller UI element, unique to each of a player's hand,
 * for a player to operate.
 * @author Darian
 */
public class Controller extends JComponent {
    private static boolean debug;

    private final Game game;
    private Player player;
    private Hand hand;

    private JButton doubleBtn;
    private JButton hitBtn;
    private JButton insureBtn;
    private JButton splitBtn;
    private JButton standBtn;

    private JButton debugPlayerBtn;
    private JButton debugDealerBtn;

    public Controller(Game game, Player player, Hand hand) {
        init();
        this.game = game;
        this.player = player;
        this.hand = hand;
    }

    private void init() {
        this.setOpaque(false);
        this.setSize(new Dimension(300, 100));
        this.setLayout(new FlowLayout());
        hitBtn = new JButton("HIT");
        hitBtn.setPreferredSize(new Dimension(80, 50));

        standBtn = new JButton("Stand");
        standBtn.setPreferredSize(new Dimension(70, 40));

        doubleBtn = new JButton("Double");
        doubleBtn.setPreferredSize(new Dimension(80, 40));

        splitBtn = new JButton("Split");
        splitBtn.setPreferredSize(new Dimension(80, 40));

        insureBtn = new JButton("Insure");
        insureBtn.setPreferredSize(new Dimension(80, 40));

        debugPlayerBtn = new JButton("P");
        debugPlayerBtn.setPreferredSize(new Dimension(50, 40));

        debugDealerBtn = new JButton("D");
        debugDealerBtn.setPreferredSize(new Dimension(50, 40));

        hitBtn.addActionListener((ActionEvent event) -> {
            Card card = game.hit(hand);

            doubleBtn.setEnabled(false); //can't double after first move!

            if (hand.getValue() == 21 || hand.isOver21()) {
                endTurn();
                return;
            }

            showButtons();
        });

        standBtn.addActionListener((ActionEvent event) -> {
            endTurn();
        });

        doubleBtn.addActionListener((ActionEvent event) -> {
           game.doubleDown(player);

           endTurn();
        });

        splitBtn.addActionListener((ActionEvent event) -> {
            game.split(player, hand);
            // System.out.printf(" -> Total: %d\n", player.getHand(0).getValue());

            showButtons();
        });

        insureBtn.addActionListener((ActionEvent event) -> {
            game.insure(player);

            showButtons();
        });

        debugPlayerBtn.addActionListener((ActionEvent event) -> {
            debugHand(hand);

            showButtons();
        });

        debugDealerBtn.addActionListener((ActionEvent event) -> {
            debugHand(game.getDealer().getHand());

            showButtons();
        });

        add(hitBtn);
        add(standBtn);
        add(doubleBtn);
        add(splitBtn);
        add(insureBtn);

        add(debugPlayerBtn);
        add(debugDealerBtn);
    }

    private void debugHand(Hand hand) {
        hand.getCards().clear();
        BufferedImage[] cardImages = game.getDealer().getDeck().getCardImages();
        for (int i = 0; i < 2; i++) {
            int cardId = JOptionPane.showOptionDialog(this, "Choose Card", "DEBUG PLAYER",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, Rank.values(), null);
            if (cardId != JOptionPane.CLOSED_OPTION) {
                hand.addCard(new Card(Rank.values()[cardId], Suit.CLUBS,
                                      cardImages[cardId], cardImages[52]));
            }
        }

        showButtons();
    }

    private void showButtons() {
        doubleBtn.setEnabled(false);
        splitBtn.setEnabled(false);
        insureBtn.setEnabled(false);

        debugPlayerBtn.setVisible(false);
        debugDealerBtn.setVisible(false);
        if (isDebug()) {
            debugPlayerBtn.setVisible(true);
            debugDealerBtn.setVisible(true);
        }

        if (hand.isBlackJack()) {
            hitBtn.setEnabled(false);
        }

        if (player.canDoubleDown()) {
            doubleBtn.setEnabled(true);
        }
        if (player.canSplitHand(hand)) {
            splitBtn.setEnabled(true);
        }
        if (game.canInsure(player)) {
            insureBtn.setEnabled(true);
        }

        repaint();
    }

    public synchronized void startTurn() {
        showButtons();
        try {
            wait();
        } catch (InterruptedException e) {}
    }

    public synchronized void endTurn() {
        repaint();
        this.notify();
    }

    @Override
    public void repaint() {
        //this is required because otherwise, repaint() will repaint Controller
        //instead, we want it to repaint GamePanel
        super.repaint();
        Window parent = SwingUtilities.getWindowAncestor(this);
        if (parent != null) {
            SwingUtilities.getWindowAncestor(this).repaint();
        }
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public static boolean isDebug() {
        return debug;
    }
    public static void setDebug(boolean debug) {
        Controller.debug = debug;
    }
}
