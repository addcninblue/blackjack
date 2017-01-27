package game;


import gui.DealerPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Darian
 */
public class Test extends JPanel {
    private Card[] cards;
    public Test() {
        super();
        Deck deck = new Deck();
        cards = deck.stream().toArray(Card[]::new);
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

        Game game = new Game();
        game.newRound();
        game.initialDeal();
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 1.0;
      //  c.weightx = 1;
       // c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        frame.add(new DealerPanel(game.getDealer()), c);
        c.gridx = 0;
        frame.add(new DealerPanel(game.getDealer()), c);
        frame.setVisible(true);
    }
}
