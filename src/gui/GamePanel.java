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
    private final Game game;
    private final Menu menu;

    private DealerComponent dealerCmp;
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

    public GamePanel(Menu menu, Game game){
        this.menu = menu;
        this.game = game;
        initGUI();

    }

    private void initGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        dealerCmp = new DealerComponent(game.getDealer());

        debugBtn = new JToggleButton("DEBUG");
        debugBtn.addActionListener((ActionEvent event) -> {
            // override here
            String gameName = JOptionPane.showInputDialog("Enter a one word name for your savefile:");
            System.out.println(gameName);
            game.saveGame(menu.database, gameName);
//            Controller.setDebug(!Controller.isDebug());
        });
        debugBtn.setPreferredSize(new Dimension(80, 40));
        //debugBtn.setMaximumSize(getPreferredSize());

        playersCmp = new JComponent() {{
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.setPreferredSize(new Dimension(1000, 360));
            this.setMinimumSize(getPreferredSize());
        }};

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(dealerCmp);
        add(debugBtn);
        add(playersCmp);
    }

    public void start() {
        setVisible(true);

        Thread logicThread = new Thread(this);
        logicThread.start();
    }

    /**
     * The main game loop.
     * NOTE: Generally, methods called from run() should not also call render().
     * - Only run() should call render().
     * - Single exception is when Dealer has his turn, that is OK.
     */
    @Override
    public void run() {
        Dealer dealer = game.getDealer();
        List<Player> players = game.getPlayers();
        Map<Player, Integer> playerToPreviousBet = new HashMap<Player, Integer>();
        Map<Player, PlayerComponent> playerToPlayerCmp = new HashMap<Player, PlayerComponent>();

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            PlayerComponent playerCmp = new PlayerComponent(player);
            playersCmp.add(playerCmp);
            if (i != players.size() - 1) { //create spacers between players
                playersCmp.add(Box.createRigidArea(new Dimension(20, 0)));
            }
            playerToPlayerCmp.put(player, playerCmp);
        }
        while (game.hasPlayers()) {
            game.saveGame(menu.database, "autosave");
            game.newRound();
            for (PlayerComponent playerCmp : playerToPlayerCmp.values()) {
                playerCmp.setResults(null);
            }

            for (Player player : players) {
                int previousBet = playerToPreviousBet.getOrDefault(player, 0);
                if (player.getMoney() < previousBet) {
                    previousBet = 0;
                }
                playerToPreviousBet.put(player, getBetFromPlayer(player, previousBet));
            }

            game.initialDeal();
            render();

            for (Player player : players) {
                runPlayerTurn(player, playerToPlayerCmp.get(player));
            }
            unhideAllHands();
            runDealerTurn(dealer);

            for (PlayerComponent playerCmp : playerToPlayerCmp.values()) {
                processBets(playerCmp);
            }
            render();

            List<Player> peopleRemoved = game.removeMoneyless();
            for (Player player : peopleRemoved) {
                JOptionPane.showMessageDialog(this, String.format("%s removed from game.",
                        player.getName()), "Player Eliminated!", JOptionPane.WARNING_MESSAGE);
                playersCmp.remove(playerToPlayerCmp.get(player));
                render();
            }
            dealerCmp.start();
        }
        setVisible(false);
        menu.setVisible(true);
    }


    private int getBetFromPlayer(Player player, int previousBet) {
        String betMsg = String.format("\n\n%s's bet ($%d left): ",
                                      player.getName(), player.getMoney());
        while (player.getBet() == 0) {
            try {
                int betAmt = Integer.parseInt(JOptionPane.showInputDialog(betMsg,
                                              previousBet == 0 ? null : previousBet));
                player.bet(betAmt);
                return betAmt;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "That is not a valid number!",
                        "Invalid bet!", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Invalid bet!", JOptionPane.ERROR_MESSAGE);
            }
        }
        return -1;
    }

    private void runPlayerTurn(Player player, PlayerComponent playerCmp) {
        for (int i = 0; i < player.getHands().size(); i++) { //foreach causes CM exception
            Hand hand = player.getHand(i);

            Controller controller = new Controller(game, player, hand);
            playerCmp.setController(controller);
        }
    }

    private void unhideAllHands() {
        Dealer dealer = game.getDealer();
        List<Player> players = game.getPlayers();

        dealer.getHand().unhideCards();
        for (Player player : players) {
            for (Hand hand : player.getHands()) {
                hand.unhideCards();
            }
        }
    }

    private void runDealerTurn(Dealer dealer) {
        render();
        while (!dealer.getHand().isOver16()) {
            game.hit(dealer.getHand());
            render();
        }
    }

    private void processBets(PlayerComponent playerCmp) {
        Player player = playerCmp.getPlayer();

        List<String> results = new ArrayList<String>(player.getHands().size());
        for (Hand hand : player.getHands()) {
            results.add(game.getResult(hand));
        }

        playerCmp.setResults(results.toArray(new String[0]));
        game.payBet(player);
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
