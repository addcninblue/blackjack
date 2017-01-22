package gui;

import game.Card;
import game.Game;
import game.Hand;
import game.Player;
import game.Rank;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * Represents a Controller UI element, unique to each of a player's hand,
 * for a player to operate.
 * @author Darian
 */
public class Controller extends JComponent {
    private final Game game;
    private Player player;
    private Hand hand;
    private boolean running;

    private JButton doubleBtn;
    private JButton hitBtn;
    private JButton insureBtn;
    private JButton splitBtn;
    private JButton standBtn;

    public Controller(Game game, Player player, Hand hand) {
        init();
        this.game = game;
        this.player = player;
        this.hand = hand;

        this.running = false;
    }

    private void init() {
        this.setVisible(false);
        this.setOpaque(false);
        this.setSize(new Dimension(300, 100));
        this.setLayout(new FlowLayout());
        hitBtn = new JButton("HIT!!!");
        hitBtn.setPreferredSize(new Dimension(90, 50));

        standBtn = new JButton("Stand");
        standBtn.setPreferredSize(new Dimension(70, 40));

        doubleBtn = new JButton("Double");
        doubleBtn.setPreferredSize(new Dimension(80, 40));

        splitBtn = new JButton("Split");
        splitBtn.setPreferredSize(new Dimension(80, 40));

        insureBtn = new JButton("Insure");
        insureBtn.setPreferredSize(new Dimension(80, 40));

        hitBtn.addActionListener((ActionEvent event) -> {
            Card card = game.hit(hand);

            doubleBtn.setVisible(false); //can't double after first move!

            if (hand.getValue() == 21 || hand.isOver21()) {
                endTurn();
            }
            repaint();
        });

        standBtn.addActionListener((ActionEvent event) -> {
            endTurn();
        });

        doubleBtn.addActionListener((ActionEvent event) -> {
           game.doubleDown(player);
           endTurn();
        });

        splitBtn.addActionListener((ActionEvent event) -> {
            //TODO

            repaint();
        });

        insureBtn.addActionListener((ActionEvent event) -> {
            //TODO
            endTurn();
        });

        add(hitBtn);
        add(standBtn);
        add(doubleBtn);
        add(splitBtn);
        add(insureBtn);
    }

    private void showButtons() {
        doubleBtn.setVisible(false);
        splitBtn.setVisible(false);
        insureBtn.setVisible(false);

        if (hand.isBlackJack()) {
            running = false;
            endTurn();
        }

        if (player.canDoubleDown()) {
            doubleBtn.setVisible(true);
        }
        if (player.canSplitHand(hand)) {
            splitBtn.setVisible(true);
        }
        if (game.getDealer().getFaceUpCard().RANK == Rank.ACE) {
            insureBtn.setVisible(true);
        }
    }

    public synchronized void startTurn() {
        running = true;
        setVisible(true);
        showButtons();
        repaint();
        try {
            while (this.isRunning()) {
                wait();
            }
        } catch (InterruptedException e) {}
    }

    public synchronized void endTurn() {
        repaint();
        setVisible(false);
        if (this.isRunning()) {
            this.running = false;
            this.notify();
        }
    }

    @Override
    public void repaint() {
        //this is required because otherwise, repaint() will repaint Controller
        //instead, we want it to repaint GamePanel
        super.repaint();
        SwingUtilities.getWindowAncestor(this).repaint();
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
    public Hand getHand() {
        return hand;
    }
    public boolean isRunning() {
        return running;
    }
}
