/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import game.Player;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Darian
 */
public class Better extends ButtonsComponent {
    private Player player;
    private int previousBet;

    private JTextField betTxt;
    private JButton betBtn;
    public Better(Player player, int previousBet) {
        super();
        this.player = player;
        this.previousBet = previousBet;

        betTxt.setText(previousBet == 0 ? null : previousBet + "");
    }

    @Override
    protected void addComponents() {
        Dimension size = getPreferredSize();
        betTxt = new JTextField();
        betTxt.setPreferredSize(new Dimension(180, 40));

        betBtn = new JButton("Bet");
        betBtn.setPreferredSize(new Dimension(80, 50));

        betBtn.addActionListener((ActionEvent event) -> {
            try {
                int betAmt = Integer.parseInt(betTxt.getText());
                player.bet(betAmt);
                endTurn();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "That is not a valid number!",
                        "Invalid bet!", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Invalid bet!", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(betBtn);
        add(betTxt);
        revalidate();
        repaint();
    }
}
