package dokapon.services;

import dokapon.characters.LatinChar;
import dokapon.characters.SpriteLocation;
import dokapon.enums.CharSide;
import dokapon.enums.CharType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SpriteWriter {

    public void writeLatinChars(List<LatinChar> latinChars, byte[] data) {
        System.out.println("Write Latin characters");
        List<LatinChar> localLatinChars = new ArrayList<>(latinChars);
        while (!localLatinChars.isEmpty()) {
            LatinChar latinChar = localLatinChars.get(0);
            if  (latinChar.getType()!= CharType.NO_SPRITE) {
                LatinChar otherSide = findOtherSide(localLatinChars, latinChar);
                if (otherSide == null) {
                    write(latinChar, data);
                    localLatinChars.remove(latinChar);
                } else {
                    write(latinChar, otherSide, data);
                    localLatinChars.remove(latinChar);
                    localLatinChars.remove(otherSide);
                }
            } else {
                localLatinChars.remove(latinChar);
            }
        }
    }

    private void write(LatinChar latinChar, LatinChar otherSide, byte[] data) {
        LatinChar sideOne = latinChar;
        LatinChar sideTwo = otherSide;
        if (otherSide.getSpriteLocation().getSide()== CharSide.TWO) {
            sideOne = otherSide;
            sideTwo = latinChar;
        }
        BufferedImage imageTopA = null;
        BufferedImage imageBotA = null;
        BufferedImage imageTopB = null;
        BufferedImage imageBotB = null;
        if (sideOne.getType()==CharType.SINGLE) {
            imageTopA = loadImageTop(sideOne.getSprite().getImage());
            imageBotA = loadImageBot(sideOne.getSprite().getImage());
        }
        if (sideOne.getType()==CharType.DOUBLE_STRAIGHT || sideOne.getType()==CharType.DOUBLE_SLANTED) {
            imageTopA = loadImage(sideOne.getSprite().getImageTop());
            imageBotA = loadImage(sideOne.getSprite().getImageBot());
        }
        if (sideTwo.getType()==CharType.SINGLE) {
            imageTopB = loadImageTop(sideTwo.getSprite().getImage());
            imageBotB = loadImageBot(sideTwo.getSprite().getImage());
        }
        if (sideTwo.getType()==CharType.DOUBLE_STRAIGHT || sideTwo.getType()==CharType.DOUBLE_SLANTED) {
            imageTopB = loadImage(sideTwo.getSprite().getImageTop());
            imageBotB = loadImage(sideTwo.getSprite().getImageBot());
        }
        FontManager.writeImagesAtOffset(imageTopA,imageTopB, sideOne.getSpriteLocation().getOffset(), data);
        FontManager.writeImagesAtOffset(imageBotA,imageBotB, sideOne.getSpriteLocation().getOffset()+Integer.parseInt("100",16), data);
    }

    private void write(LatinChar latinChar, byte[] data) {
        if (latinChar.getType()==CharType.SINGLE) {
            BufferedImage imageTop = loadImageTop(latinChar.getSprite().getImage());
            BufferedImage imageBot = loadImageBot(latinChar.getSprite().getImage());
            FontManager.writeImageAtOffset(imageTop, latinChar.getSpriteLocation().getOffset(), latinChar.getSpriteLocation().getSide(), data);
            FontManager.writeImageAtOffset(imageBot, latinChar.getSpriteLocation().getOffset() + Integer.parseInt("100", 16), latinChar.getSpriteLocation().getSide(), data);
        }
        if (latinChar.getType()==CharType.DOUBLE_STRAIGHT || latinChar.getType()==CharType.DOUBLE_SLANTED) {
            BufferedImage imageTop = loadImage(latinChar.getSprite().getImageTop());
            BufferedImage imageBot = loadImage(latinChar.getSprite().getImageBot());
            FontManager.writeImageAtOffset(imageTop, latinChar.getSpriteLocation().getOffset(), latinChar.getSpriteLocation().getSide(), data);
            FontManager.writeImageAtOffset(imageBot, latinChar.getSpriteLocation().getOffset() + Integer.parseInt("100", 16), latinChar.getSpriteLocation().getSide(), data);
        }
    }

    private LatinChar findOtherSide(List<LatinChar> latinChars, LatinChar latinChar) {
        for (LatinChar lc:latinChars) {
            SpriteLocation spriteLocation = lc.getSpriteLocation();
            if (lc.getType()!=CharType.NO_SPRITE && spriteLocation!=null) {
                if (spriteLocation.getOffset()==latinChar.getSpriteLocation().getOffset() &&
                        spriteLocation.getSide()!=latinChar.getSpriteLocation().getSide()) {
                    return lc;
                }
            }
        }
        return null;
    }

    private BufferedImage loadImage(String filename) {
        InputStream inputStream = SpriteWriter.class.getClassLoader().getResourceAsStream(filename);
        BufferedImage image = null;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private BufferedImage loadImageTop(String filename) {
        InputStream inputStream = SpriteWriter.class.getClassLoader().getResourceAsStream(filename);
        BufferedImage image = null;
        if (inputStream==null) {
            System.out.println();
        }
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image.getSubimage(0,0,image.getWidth(), image.getHeight() / 2);
    }

    private BufferedImage loadImageBot(String filename) {
        BufferedImage image = null;
        try {
            InputStream inputStream = SpriteWriter.class.getClassLoader().getResourceAsStream(filename);
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image.getSubimage(0,(int)image.getHeight()/2,image.getWidth(), image.getHeight() / 2);
    }
}
