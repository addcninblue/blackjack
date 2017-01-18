package game;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import util.SpriteLoader;

/**
 *
 * @author Darian
 */
public class Test extends JPanel {
    private Card[] cards;
    public Test() {
        super();
        cards = new Card[52];
        BufferedImage[] cardImages = new SpriteLoader("cards", 67, 95).cardImages;

        int i = 0;
        for (Suit s : Suit.values()) {
            for (Rank r : Rank.values()) {
                this.cards[i] = new Card(r, s, cardImages[i]);
                i++;
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 13; x++) {
                BufferedImage card = cards[x+y*13].IMAGE;
                g2.drawImage(card, x*card.getWidth(), y*card.getHeight(), null);
            }
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Test table = new Test();
        frame.add(table);
        frame.setVisible(true);
    }
}