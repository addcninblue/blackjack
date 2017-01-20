package gui;

import game.Card;
import game.Game;
import game.Hand;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 *
 * @author Darian
 */
public class Controller extends JComponent {
    private final Game game;
    private Hand hand;
    
    private JButton doubleBtn;
    private JButton hitBtn;
    private JButton insureBtn;
    private JButton splitBtn;
    private JButton standBtn;
    /**
     * Creates new form Controller
     */
    public Controller(Game game, Hand hand) {
        init();
        this.game = game;
        this.hand = hand;
        //hide special buttons until ready
        insureBtn.setVisible(false);
        splitBtn.setVisible(false);
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
            if (hand.isOver21()) {
                endTurn();
            }
            update();
        });
        
        standBtn.addActionListener((ActionEvent event) -> {
            endTurn();
        });
        
        add(hitBtn);
        add(standBtn);
        add(doubleBtn);
        add(splitBtn);
        add(insureBtn);
    }
    
    public synchronized void startTurn() {
        try {
            this.wait();
        } catch (InterruptedException e) {}
    }
    public synchronized void endTurn() {
        update();
        this.setVisible(false);
        this.notify();
    }
    
    private void update() {
        //this is required because repaint() alone will repaint Controller
        //we want it to repaint GamePanel
        SwingUtilities.getWindowAncestor(this).repaint();
    }
    public void showInsureBtn(boolean show) {
        insureBtn.setVisible(show);
    }
    
    public void showSplitBtn(boolean show) {
        splitBtn.setVisible(show);
    }
}
