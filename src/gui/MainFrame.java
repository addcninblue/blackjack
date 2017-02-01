package gui;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * The main class to run the application.
 *
 * @author Darian
 */
public class MainFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(1280, 720));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Menu());
        frame.setVisible(true);
    }
}
