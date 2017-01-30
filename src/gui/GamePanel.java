package gui;

import game.Card;
import game.Dealer;
import game.Game;
import game.Hand;
import game.Player;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import util.Painter;
import util.SpriteLoader;


/**
 * The GUI that runs a Blackjack Game. Logic runs on a different thread.
 * @author Darian
 */
public class GamePanel extends JPanel implements Runnable {
    private Game game;
    private Menu menu;

    private boolean drawingResults;
    private Hand activeHand;

    private DealerPanel dealerPnl;
    private JComponent playersCmp;
    private JToggleButton debugBtn;
    public GamePanel(Menu menu) {
        this.menu = menu;
        game = new Game();
        game.addPlayer(new Player("Darian"));

        initGUI();
    }

    public GamePanel(Menu menu, List<Player> players) {
        this.menu = menu;
        game = new Game(players);

        initGUI();
    }

    private void initGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        dealerPnl = new DealerPanel(game.getDealer());

        debugBtn = new JToggleButton("DEBUG");
        debugBtn.addActionListener((ActionEvent event) -> {
            Controller.setDebug(!Controller.isDebug());
        });
        debugBtn.setPreferredSize(new Dimension(80, 40));
        //debugBtn.setMaximumSize(getPreferredSize());

        playersCmp = new JComponent() {{
            this.setLayout(new FlowLayout());
        }};

        add(Box.createRigidArea(new Dimension(0, 40)));
        add(dealerPnl);
        add(debugBtn);
        //add(Box.createRigidArea(new Dimension(0, 50)));
        add(playersCmp);
    }

    public void start() {
        setVisible(true);

        Thread logicThread = new Thread(this);
        logicThread.start();
    }

    @Override
    public void run() {
        Dealer dealer = game.getDealer();
        List<Player> players = game.getPlayers();

        Map<Player, Integer> playerToPreviousBet = new HashMap<Player, Integer>();

        for (Player player : players) {
            playersCmp.add(new PlayerPanel(player));
        }
        while (game.hasPlayers()) {
            game.newRound();
            for (Component playerCmp : playersCmp.getComponents()) {
                ((PlayerPanel)playerCmp).setResults(null);
            }

            for (Player player : players) {
                getBetFromPlayer(player, playerToPreviousBet);
            }
            game.initialDeal();
            render();

            runPlayerTurns(players);
            unhideAllHands();
            runDealerTurn(dealer);

            for (int i = 0; i < players.size(); i++) {
                PlayerPanel playerPanel = (PlayerPanel)playersCmp.getComponent(i);
                List<String> results = new ArrayList<String>();
                for (Hand hand : players.get(i).getHands()) {
                    results.add(game.getResult(hand));
                }
                playerPanel.setResults(results.toArray(new String[0]));
            }
            for (Player player : players) {
                game.payBet(player);
            }

            List<Player> peopleRemoved = game.removeMoneyless();
            for (Player player : peopleRemoved) {
                JOptionPane.showMessageDialog(this, String.format("%s removed from game.", player.getName()),
                                              "Player Eliminated!", JOptionPane.WARNING_MESSAGE);
                render();
            }
            dealerPnl.start();
        }
        setVisible(false);
        menu.setVisible(true);
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

    private void runPlayerTurns(List<Player> players) {
        for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                for (int j = 0; j < player.getHands().size(); j++) {
                    Hand hand = player.getHand(j);
                    activeHand = hand;
                    Controller controller = new Controller(game, player, hand);

                    PlayerPanel playerPanel = (PlayerPanel)playersCmp.getComponent(i);
                    playerPanel.setController(controller);
                }
            }
    }

    private void unhideAllHands() {
        Dealer dealer = game.getDealer();
        List<Player> players = game.getPlayers();
        for (Player player : players) {
            for (Hand hand : player.getHands()) {
                hand.unhideCards();
            }
        }
        dealer.getHand().unhideCards();
    }

    private void runDealerTurn(Dealer dealer) {
        render();
        while (!dealer.getHand().isOver16()) {
            game.hit(dealer.getHand());
            render();
        }
    }

    private void render(int delay) {
        repaint();
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
    }

    private void render() {
        render(200);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(SpriteLoader.SOLARIZED_TABLE_TOP, 0, 0, getWidth(), getHeight(), null);

        drawDeck(g2);
        drawScoreboard(g2);
    }

    private void drawDeck(Graphics2D g2) {
        Dealer dealer = game.getDealer();

        //rotate graphics
        Graphics2D g3 = (Graphics2D)g2.create();
        AffineTransform at = new AffineTransform();
        at.setToRotation(0.785, 180, 160);
        g3.setTransform(at);

        g3.drawImage(SpriteLoader.SOLARIZED_RECTANGLE, 180, 140, 72, 100, null);
        for (int i = 0; i < dealer.getDeck().size(); i++) {
            Card card = dealer.getDeck().get(i);
            g3.drawImage(card.getBack(), (int)Math.floor(180-i/3), (int)Math.floor(140-i/3), null);
        }
        g3.dispose();
    }

    private void drawScoreboard(Graphics2D g2) {
        for (int i = 0; i < game.getPlayers().size(); i++) {
            Player player = game.getPlayers().get(i);
            Painter.drawCenteredString(g2, String.format("%8s: $%d ($%d)", player.getName(), player.getMoney(), player.getBet()), Color.ORANGE,
                    new Rectangle(9*getWidth()/10, 30 + i*30, 70, 10),
                    new Font("Courier", Font.PLAIN, 20));
        }
    }
}
