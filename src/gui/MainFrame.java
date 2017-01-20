/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Game;
import javax.swing.JFrame;

/**
 *
 * @author Darian
 */
public class MainFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Menu());
        frame.setVisible(true);
    }
}
