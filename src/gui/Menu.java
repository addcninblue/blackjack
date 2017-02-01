package gui;

import game.Game;
import game.Player;
import game.Database;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import util.SpriteLoader;

/**
 *
 * @author Darian
 */
public class Menu extends javax.swing.JPanel {

    public Database database;

    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        try {
            database = new Database("blackjack");
//            database.readyTable("testing1");
//            database.readyTable("testing2");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(SpriteLoader.MENU_BACKGROUND, 0, 0, getWidth(), getHeight(), null);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        startBtn = new javax.swing.JButton();
        exitBtn = new javax.swing.JButton();

        startBtn.setText("Start");
        startBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBtnActionPerformed(evt);
            }
        });

        exitBtn.setText("Exit");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(310, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(startBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(exitBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(214, Short.MAX_VALUE)
                .addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitBtn)
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBtnActionPerformed
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);

        //{"1 player", "2 players", "3 players", "4 players"};
        String[] options = {"I'm alone :(", "Company", "Crowd", "Gathering"};
        int playerCount = 1 + JOptionPane.showOptionDialog(this,
                        "How many people are playing?", "Blackjack",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null, options, options[0]);

        List<Player> players = new ArrayList<Player>(playerCount);
        for (int i = 0; i < playerCount; i++) {
            String playerName = JOptionPane.showInputDialog("What is player " + (i+1) + "'s name?");
            players.add(new Player(playerName));
        }

        this.setVisible(false);
        GamePanel gamePanel = new GamePanel(this, players);
        frame.add(gamePanel);
        frame.setResizable(false);
        gamePanel.start();
    }//GEN-LAST:event_startBtnActionPerformed

    // private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
    //     this.setVisible(false);
    //     System.exit(0);
    // }//GEN-LAST:event_exitBtnActionPerformed

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
        System.out.println("hi");
        String[] saveFiles = {};
        try {
            saveFiles = database.getSavefiles();
        } catch (SQLException e){
            e.printStackTrace();
        }
        if(saveFiles.length > 0) {
            int saveFile = JOptionPane.showOptionDialog(this,
                    "Choose your save file:", "Blackjack",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, saveFiles, saveFiles[0]);
            System.out.println(saveFiles[saveFile]);
            List<Player> players = new ArrayList<Player>();
            try {
                players = database.getPlayersFromSavefile(saveFiles[saveFile]);
            } catch (SQLException e){
                e.printStackTrace();
            }
            this.setVisible(false);
            GamePanel gamePanel = new GamePanel(this, players);
            frame.add(gamePanel);
            frame.setResizable(false);
            gamePanel.start();
        } else {

        }
    } // overridden by add


    public void loadSavefiles() {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitBtn;
    private javax.swing.JButton startBtn;
    // End of variables declaration//GEN-END:variables
}
