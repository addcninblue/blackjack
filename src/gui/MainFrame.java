package gui;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Start here.
 * @author Darian
 */
public class MainFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setMinimumSize(new Dimension(1280, 720));
        frame.setSize(frame.getMinimumSize());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Menu());
        frame.setVisible(true);
    }
}
