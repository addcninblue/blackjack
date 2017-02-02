/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author Darian
 */
public abstract class ButtonsComponent extends JComponent {
    public ButtonsComponent() {
        init();
        addComponents();
    }

    private void init() {
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(300, 100));
        this.setMinimumSize(getPreferredSize());
        this.setMaximumSize(getPreferredSize());
        this.setLayout(new FlowLayout());
    }

    protected abstract void addComponents();

    public synchronized void startTurn() {
        setVisible(true);
        repaint();
        try {
            wait();
        } catch (InterruptedException e) {}
    }

    public synchronized void endTurn() {
        repaint();
        for (Component cmp : getComponents()) {
            if (cmp instanceof JButton || cmp instanceof JTextField) {
                cmp.setEnabled(false);
            }
        }
        this.notify();
    }

    @Override
    public void repaint() {
        //this is required because otherwise, repaint() will repaint component only
        //instead, we want it to repaint GamePanel as well
        super.repaint();
        Window parent = SwingUtilities.getWindowAncestor(this);
        if (parent != null) {
            SwingUtilities.getWindowAncestor(this).repaint();
        }
    }

}
