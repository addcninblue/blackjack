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
 * A utility class to help paint on the JFrame.
 *
 * @author Darian
 * 2.1.17
 */
public class Painter {
    /**
     * The solarized cyan color.
     */
    public static final Color SOLARIZED_CYAN = new Color(42, 161, 152);
    /**
     * It's actually black.
     */
    public static final Color SOLARIZED_ORANGE = new Color(0, 0, 0);

    /**
     * Draws a centered string in a rectangle.
     * @param g the graphical utility to draw
     * @param text the string to draw
     * @param color the color of the string
     * @param rect the rectangle to center the string in
     * @param font the font to draw the string in
     */
    public static void drawCenteredString(Graphics g, String text, Color color, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (rect.width - metrics.stringWidth(text)) / 2;
        int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();

        g.setFont(font);
        g.setColor(color);
        g.drawString(text, rect.x + x, rect.y + y);
    }
}
