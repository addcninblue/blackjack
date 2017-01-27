/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Game;
import game.Hand;
import game.Player;
import java.awt.FlowLayout;
import javax.swing.JComponent;

/**
 *
 * @author Darian
 */
public class PlayersPanel extends JComponent {

    public PlayersPanel(Game game, Player player, Hand hand) {
        init(game, player, hand);
    }

    private void init(Game game, Player player, Hand hand) {
        setLayout(new FlowLayout());
        Controller c = new Controller(game, player, hand);
        add(c);
        c.startTurn();

    }
}
