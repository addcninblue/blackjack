/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Darian
 */
public class Painter {
    public static final Color SOLARIZED_CYAN = new Color(42, 161, 152);
    public static final Color SOLARIZED_ORANGE = new Color(0, 0, 0);

    public static void drawCenteredString(Graphics g, String text, Color color, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (rect.width - metrics.stringWidth(text)) / 2;
        int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);
        g.setColor(color);
        g.drawString(text, rect.x + x, rect.y + y);
    }
}
