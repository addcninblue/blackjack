/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Database;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import util.SpriteLoader;

/**
 *
 * @author Darian
 */
public class TestMenu extends JPanel {
    public Database database;

    private JButton startBtn;
    private JButton loadBtn;
    private JButton exitBtn;
    public TestMenu() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        add(new JComponent() {{
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            
            add(startBtn);
            add(loadBtn);
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
