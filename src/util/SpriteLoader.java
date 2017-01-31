package util;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Darian
 */
public class SpriteLoader {
    public static final BufferedImage MENU_BACKGROUND = loadPng("menu");
    public static final BufferedImage TABLE_TOP = loadPng("table");
    public static final BufferedImage RECTANGLE = loadPng("rectangle");
    public static final BufferedImage SOLARIZED_TABLE_TOP = loadPng("solarizedBackground");
    public static final BufferedImage SOLARIZED_RECTANGLE = loadPng("solarizedRectangle");

    public static final int CARD_OFFSET = 14;

    public final BufferedImage[] cardImages;
    public final int imageWidth;
    public final int imageHeight;

    private BufferedImage spriteSheet;

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
        try {
            return ImageIO.read(new File("res/"+fileName+".png"));
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to load " + fileName + ".png", e);
        }
    }
    private BufferedImage getImageAtGrid(int xGrid, int yGrid) {
        return spriteSheet.getSubimage(xGrid * imageWidth + xGrid, yGrid * imageHeight + yGrid, imageWidth, imageHeight);
    }
}
