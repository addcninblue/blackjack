package util;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.MissingResourceException;
import javax.imageio.ImageIO;

/**
 *
 * @author Darian
 */
public class SpriteLoader {
    private BufferedImage spriteSheet;
    public final BufferedImage[] cardImages;
    public final int CARD_WIDTH = 67;
    public final int CARD_HEIGHT = 95;
    public SpriteLoader(String sheetFileName) {
        try {
            this.spriteSheet = loadPng(sheetFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.cardImages = new BufferedImage[52];
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 11; x++) {
                int index = x + 11*y;
                if (index == 52) {
                    break;
                } else {
                    cardImages[11*y+x] = getImageAtGrid(x, y);
                }
            }
        }
    }
    public static BufferedImage loadPng(String fileName) throws IOException {
        return ImageIO.read(new File("res/"+fileName+".png"));
    }
    private BufferedImage getImageAtGrid(int xGrid, int yGrid) {
        return spriteSheet.getSubimage(xGrid * CARD_WIDTH + xGrid, yGrid * CARD_HEIGHT + yGrid, CARD_WIDTH, CARD_HEIGHT);
    }
}
