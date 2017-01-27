/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Player;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import util.SpriteLoader;

/**
 *
 * @author Darian
 */
public class PlayerPanel extends JComponent {
    private Controller controller;
    private Player player;
    public PlayerPanel(Player player) {
        init();
        this.player = player;
    }

    private void init() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500, 500));
        setMaximumSize(getPreferredSize());
        add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.drawImage(SpriteLoader.SOLARIZED_RECTANGLE, 0, 0, getWidth()/6, getHeight()/3 + 20, null);
            }
        });
    }
}
