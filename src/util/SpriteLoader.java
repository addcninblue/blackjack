package util;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * The utility class to load image files.
 *
 * @author Darian
 * @version 2.1.17
 */
public class SpriteLoader {
    /**
     * The image for the menu background.
     */
    public static final BufferedImage MENU_BACKGROUND = loadPng("menu");
    /**
     * The table top image.
     */
    public static final BufferedImage TABLE_TOP = loadPng("table");
    /**
     * The image for the rectangle that a hand is placed in.
     */
    public static final BufferedImage RECTANGLE = loadPng("rectangle");
    /**
     * The table top image, solarized-dark theme.
     */
    public static final BufferedImage SOLARIZED_TABLE_TOP = loadPng("solarizedBackground");
    /**
     * The image for the rectangle to place a hand in, solarized-dark theme.
     */
    public static final BufferedImage SOLARIZED_RECTANGLE = loadPng("solarizedRectangle");

    /**
     * The offset for a card when drawing hands.
     */
    public static final int CARD_OFFSET = 14;

    /**
     * The images for the fronts of the standard 52 cards.
     */
    public final BufferedImage[] cardImages;
    /**
     * The width of a card image.
     */
    public final int imageWidth;
    /**
     * The height of a card image.
     */
    public final int imageHeight;

    private BufferedImage spriteSheet;

    /**
     * Constructs a new SpriteLoader with
     * the given spritesheet and card image width and height.
     * @param sheetFileName the spritesheet's name
     * @param imageWidth the width of a card image
     * @param imageHeight the height of a card image
     */
    public SpriteLoader(String sheetFileName, int imageWidth, int imageHeight) {
        this.spriteSheet = loadPng(sheetFileName);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.cardImages = new BufferedImage[53];
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 11; x++) {
                int index = x + 11*y;
                if (index == cardImages.length) {
                    break;
                } else {
                    cardImages[index] = getImageAtGrid(x, y);
                }
            }
        }
    }

    /**
     * Loads an image file.
     * @param fileName the image file to load
     * @return the image file as a BufferedImage
     */
    public static BufferedImage loadPng(String fileName) {
        URL url = SpriteLoader.class.getResource("/res/" + fileName + ".png");
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to load " + fileName + ".png", e);
        }
    }
    private BufferedImage getImageAtGrid(int xGrid, int yGrid) {
        return spriteSheet.getSubimage(xGrid * imageWidth + xGrid, yGrid * imageHeight + yGrid, imageWidth, imageHeight);
    }
}
