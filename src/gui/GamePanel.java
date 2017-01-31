package gui;

import game.Card;
import game.Dealer;
import game.Game;
import game.Hand;
import game.Player;
import java.awt.Color;
import java.awt.Dimension;
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
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.setPreferredSize(new Dimension(1000, 360));
            this.setMinimumSize(getPreferredSize());
        }};

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(dealerPnl);
        add(debugBtn);
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
        Map<Player, PlayerPanel> playerToPlayerPanel = new HashMap<Player, PlayerPanel>();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            PlayerPanel playerPanel = new PlayerPanel(player);
            playersCmp.add(playerPanel);
            if (i != players.size() - 1) {
                playersCmp.add(Box.createRigidArea(new Dimension(20, 0))); //spacer
            }
            playerToPlayerPanel.put(player, playerPanel);
        }
        while (game.hasPlayers()) {
            game.newRound();
            for (PlayerPanel playerPanel : playerToPlayerPanel.values()) {
                playerPanel.setResults(null);
            }

            for (Player player : players) {
                getBetFromPlayer(player, playerToPreviousBet);
            }
            game.initialDeal();
            render();

            runPlayerTurns(players, playerToPlayerPanel);
            unhideAllHands();
            runDealerTurn(dealer);

            for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                PlayerPanel playerPanel = playerToPlayerPanel.get(player);

                List<String> results = new ArrayList<String>();
                for (Hand hand : player.getHands()) {
                    results.add(game.getResult(hand));
                }
                playerPanel.setResults(results.toArray(new String[0]));
                render(0);
            }
            for (Player player : players) {
                game.payBet(player);
            }

            List<Player> peopleRemoved = game.removeMoneyless();
            for (int i = 0; i < peopleRemoved.size(); i++) {
                Player player = peopleRemoved.get(i);
                JOptionPane.showMessageDialog(this, String.format("%s removed from game.",
                        player.getName()), "Player Eliminated!", JOptionPane.WARNING_MESSAGE);
                playersCmp.remove(playerToPlayerPanel.get(player));
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

    private void runPlayerTurns(List<Player> players, Map<Player, PlayerPanel> playerToPlayerPanel) {
        for (int i = 0; i < players.size(); i++) {
                Player player = players.get(i);
                for (int j = 0; j < player.getHands().size(); j++) {
                    Hand hand = player.getHand(j);
                    Controller controller = new Controller(game, player, hand);

                    PlayerPanel playerPanel = playerToPlayerPanel.get(player);
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
        //magic numbers galore :(
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
