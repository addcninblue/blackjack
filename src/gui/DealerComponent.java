package gui;

import game.Card;
import game.Dealer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import util.SpriteLoader;

/**
 * The JFrame component for the dealer.
 *
 * @author Darian
 */
public class DealerComponent extends JComponent {
    private Dealer dealer;

    private EndRoundButton endRoundBtn;

    /**
     * Constructs a new DealerComponent with the given dealer.
     * @param dealer the dealer
     */
    public DealerComponent(Dealer dealer) {
        init();
        this.dealer = dealer;
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(350, 250));
        setMinimumSize(getPreferredSize());
        setMaximumSize(new Dimension(450, 250));

        endRoundBtn = new EndRoundButton();

        add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;

                g2.drawImage(SpriteLoader.SOLARIZED_RECTANGLE, 0, 0, getWidth(), getHeight(), null);
                for (int i = 0; i < dealer.getHand().getCards().size(); i++) {
                    Card card = dealer.getHand().getCard(i);
                    int xOffset = (int)(getWidth()/2 - 40);
                    int yOffset = (int)(getHeight()/4);
                    g2.drawImage(card.getImage(), xOffset + i*SpriteLoader.CARD_OFFSET, yOffset, null);
                }
            }
        });
        add(endRoundBtn);
    }

    public EndRoundButton getEndRoundBtn() {
        return endRoundBtn;
    }

    public class EndRoundButton extends JButton {
        public EndRoundButton() {
            init();
        }
        private void init() {
            setEnabled(false);
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setPreferredSize(new Dimension(80, 30));
            setText("Next Round");
            addActionListener((ActionEvent) -> {
                stop();
            });
        }

        public synchronized void start() {
            setEnabled(true);
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        public synchronized void stop() {
            setEnabled(false);
            notify();
        }
    }
}
