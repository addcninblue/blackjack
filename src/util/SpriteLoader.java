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
    
    public final BufferedImage[] cardImages;
    public final int imageWidth;
    public final int imageHeight;
    
    private BufferedImage spriteSheet;
    
    public SpriteLoader(String sheetFileName, int imageWidth, int imageHeight) {
        this.spriteSheet = loadPng(sheetFileName);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
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
