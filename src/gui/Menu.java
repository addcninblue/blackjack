package gui;

import game.Database;
import game.Player;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import util.SpriteLoader;

/**
 * The Main menu screen. Don't read the code until it's documented.
 * @author Darian
 */
public class Menu extends JPanel {
    public Database database;

    private JButton startBtn;
    private JButton loadBtn;
    private JButton exitBtn;
    public Menu() {
        init();
        try {
            database = new Database("blackjack");
        } catch (SQLException e) {
            System.err.println("Database not found!");
            e.printStackTrace();
        }
    }
    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        startBtn = new JButton("New ServerGame");
        startBtn.setMaximumSize(new Dimension(100, 50));
        startBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);

        loadBtn = new JButton("Load ServerGame");
        loadBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);
        exitBtn = new JButton("Exit");
        exitBtn.setAlignmentX(Component.RIGHT_ALIGNMENT);

        startBtn.addActionListener((ActionEvent event) -> {
            JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);

            //{"1 player", "2 players", "3 players", "4 players"};
            String[] options = {"I'm alone :(", "Company", "Crowd", "Gathering"};
            int playerCount = 1 + JOptionPane.showOptionDialog(this,
                            "How many people are playing?", "Blackjack",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, options, options[0]);
            if (playerCount == 0) {
                return;
            }

            List<Player> players = new ArrayList<Player>(playerCount);
            for (int i = 0; i < playerCount; i++) {
                String playerName = JOptionPane.showInputDialog("What is player " + (i+1) + "'s name?");
                players.add(new Player(playerName));
            }

            this.setVisible(false);
            gui.GamePanel gamePanel = new gui.GamePanel(this, players);
            frame.add(gamePanel);
            frame.setResizable(false);
            gamePanel.start();
        });

        loadBtn.addActionListener((ActionEvent) -> {
            JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
            String[] saveFiles = {};
            try {
                saveFiles = database.getSavefiles();
            } catch (SQLException e){
                e.printStackTrace();
            }
            if(saveFiles.length <= 0) {
                JOptionPane.showMessageDialog(this, "You have no save files.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int saveFile = JOptionPane.showOptionDialog(this,
                    "Choose your save file:", "Blackjack",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, saveFiles, saveFiles[0]);
            if (saveFile == JOptionPane.CLOSED_OPTION) {
                return;
            }
            
            List<Player> players = new ArrayList<Player>();
            try {
                players = database.getPlayersFromSavefile(saveFiles[saveFile]);
            } catch (SQLException e){
                e.printStackTrace();
            }
            this.setVisible(false);
            gui.GamePanel gamePanel = new gui.GamePanel(this, players);
            frame.add(gamePanel);
            frame.setResizable(false);
            gamePanel.start();
        });

        exitBtn.addActionListener((ActionEvent) -> {
            this.setVisible(false);
            System.exit(0);
        });

        add(Box.createRigidArea(new Dimension(0, 550)));
        add(new JComponent() {{
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setAlignmentX(0.98f);
            add(startBtn);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(loadBtn);
            add(Box.createRigidArea(new Dimension(0, 10)));
            add(exitBtn);
        }});
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(SpriteLoader.MENU_BACKGROUND, 0, 0, getWidth(), getHeight(), null);
    }
}
