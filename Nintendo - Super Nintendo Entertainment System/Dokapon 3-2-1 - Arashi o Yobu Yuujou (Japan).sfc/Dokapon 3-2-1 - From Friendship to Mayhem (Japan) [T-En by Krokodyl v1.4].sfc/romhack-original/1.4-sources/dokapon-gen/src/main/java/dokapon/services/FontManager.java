
package dokapon.services;

import dokapon.entities.LignePixel;
import dokapon.enums.CharSide;
import dokapon.enums.InEditorColor;
import dokapon.enums.InFileColor;
import dokapon.enums.InGameColor;

import java.awt.image.BufferedImage;

public class FontManager {

    public static byte[] writeImageAtOffset(BufferedImage image, int offset, CharSide side, byte[] data) {
        int[] ligneData = new int[32];
        for (int i=0;i<32;i++) {
            ligneData[i] = (data[offset+i] & 0xFF);
        }
        LignePixel lignePixel = new LignePixel(offset,ligneData);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int rgb = image.getRGB(x, y);
                InFileColor fileColor = InFileColor.getInFileColor(rgb);
                InGameColor inGameColor = fileColor.getInGameColor();
                int color = InEditorColor.getColor(inGameColor, InGameColor.BLUE);
                if (side==CharSide.ONE) color = InEditorColor.getColor(InGameColor.BLUE, inGameColor);
                lignePixel.setPixelColor(y*2, x, color);
            }
        }
        for (int i=0;i<32;i++) {
            data[offset+i] = (byte)lignePixel.getData()[i];
        }
        return data;
    }

    public static byte[] writeImagesAtOffset(BufferedImage imageSide1, BufferedImage imageSide2, int offset, byte[] data) {
        int[] ligneData = new int[32];
        for (int i=0;i<32;i++) {
            ligneData[i] = (data[offset+i] & 0xFF);
        }
        LignePixel lignePixel = new LignePixel(offset,ligneData);
        for (int y = 0; y < imageSide1.getHeight(); y++) {
            for (int x = 0; x < imageSide1.getWidth(); x++) {
                int b1 = imageSide1.getRGB(x, y);
                int b2 = imageSide2.getRGB(x, y);
                InFileColor fileColor1 = InFileColor.getInFileColor(b1);
                InFileColor fileColor2 = InFileColor.getInFileColor(b2);
                int color = InEditorColor.getColor(fileColor1.getInGameColor(), fileColor2.getInGameColor());

                lignePixel.setPixelColor(y*2, x, color);
            }
        }
        for (int i=0;i<32;i++) {
            data[offset+i] = (byte)lignePixel.getData()[i];
        }
        return data;
    }

}
