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
        Deck deck = new Deck();
        cards = deck.getCards();
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        /*for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 13; x++) {
                BufferedImage card = cards[x+y*13].getImage();
                g2.drawImage(card, x*card.getWidth(), y*card.getHeight(), null);
            }
        }*/
        Card card = cards[0];
        g2.drawImage(card.getImage(), 0, 0, null);
        System.out.println(card);
        
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
