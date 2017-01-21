package gui;

import game.Card;
import game.Game;
import game.Hand;
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
    private Hand hand;
    private boolean running;
    
    private JButton doubleBtn;
    private JButton hitBtn;
    private JButton insureBtn;
    private JButton splitBtn;
    private JButton standBtn;

    public Controller(Game game, Hand hand) {
        init();
        this.game = game;
        this.hand = hand;
        this.running = false;
        
        showButtons();
    }
    
    private void init() {
        this.setVisible(false);
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(300, 100));
        Dimension size = getPreferredSize();
        this.setBounds(0, 0, size.width, size.height);
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
            
            System.out.println(hand.getValue()); //DEBUG
            if (hand.getValue() == 21 || hand.isOver21()) {
                endTurn();
            } 
            
            repaint();
        });
        
        standBtn.addActionListener((ActionEvent event) -> {
            endTurn();
        });
        
        doubleBtn.addActionListener((ActionEvent event) -> {
           //invoke hit
           hitBtn.getActionListeners()[0].actionPerformed(event);
           endTurn();
        });
        
        splitBtn.addActionListener((ActionEvent event) -> {
            //TODO
        });
        
        insureBtn.addActionListener((ActionEvent event) -> {
            //TODO
        });
        add(hitBtn);
        add(standBtn);
        add(doubleBtn);
        add(splitBtn);
        add(insureBtn);
    }
    
    private void showButtons() {
        doubleBtn.setVisible(true);
        splitBtn.setVisible(false);
        insureBtn.setVisible(false);
        
        if (hand.isBlackJack()) {
            running = false;
            endTurn();
        } else if (hand.isSplittable()) {
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
        SwingUtilities.getWindowAncestor(this).repaint();
    }
    
    public void showInsureBtn(boolean show) {
        insureBtn.setVisible(show);
    }
    
    public void showSplitBtn(boolean show) {
        splitBtn.setVisible(show);
    }
    
    public boolean isRunning() {
        return running;
    }
}
