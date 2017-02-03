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
    public static final BufferedImage MENU_BACKGROUND = loadPng("menu");
    public static final BufferedImage GREEN_TABLE_TOP = loadPng("table");
    public static final BufferedImage SOLARIZED_TABLE_TOP = loadPng("solarizedBackground");
    public static final BufferedImage SOLARIZED_RECTANGLE = loadPng("solarizedRectangle");

    /**
     * The offset for a card when drawing hands, to show only the suit + value.
     */
    public static final int CARD_OFFSET = 14;
    public final BufferedImage[] cardImages;
    public final int imageWidth;
    public final int imageHeight;

    private BufferedImage spriteSheet;

    /**
     * Constructs a new SpriteLoader with
     * the given spritesheet and card image width and height.
     * @param sheetFileName the spritesheet's name
     * @param imageWidth the width of an image cell
     * @param imageHeight the height of an image cell
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
