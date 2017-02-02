package gui;

import game.Card;
import game.Game;
import game.Hand;
import game.Player;
import game.Rank;
import game.Suit;
import java.awt.Component;
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
public class Controller extends ButtonsComponent {
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

    /**
     * Constructs a new controller with the given game, player, and hand.
     * @param game the game
     * @param player the player
     * @param hand the hand
     */
    public Controller(Game game, Player player, Hand hand) {
        super();
        this.game = game;
        this.player = player;
        this.hand = hand;
        
    }

    @Override
    protected void addComponents() {
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

    @Override
    public void startTurn() {
        showButtons();
        super.startTurn();
    }

    /**
     * Sets the controller's hand.
     * @param hand the new hand
     */
    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Hand getHand() {
        return hand;
    }

    /**
     * Returns whether or not the controller is in debug state.
     * @return whether or not the controller is in debug state
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * Updates the controller's debug state.
     * @param debug true if in debug state, false otherwise
     */
    public static void setDebug(boolean debug) {
        Controller.debug = debug;
    }
}
