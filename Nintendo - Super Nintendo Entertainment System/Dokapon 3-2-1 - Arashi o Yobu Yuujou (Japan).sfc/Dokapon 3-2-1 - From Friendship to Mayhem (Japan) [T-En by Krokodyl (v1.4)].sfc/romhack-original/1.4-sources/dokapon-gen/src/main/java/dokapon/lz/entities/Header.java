package dokapon.lz.entities;

public class Header {

    int flagLengthBits = 8;

    byte decompressedLengthHighByte;
    byte decompressedLengthLowByte;
    byte repeatLengthBits;

    boolean optionStopFirstPattern = true;

    public static Header INTRO_QUOTE_SPRITE_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("10", 16),
            (byte) Integer.parseInt("05", 16),
            false);
    public static Header INTRO_QUOTE_SPRITE_ORDER_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("08", 16),
            (byte) Integer.parseInt("07", 16));
    public static Header INTRO_TEXTS_SPRITE_HEADER = new Header(
            (byte) Integer.parseInt("20", 16),
            (byte) Integer.parseInt("06", 16),
            (byte) Integer.parseInt("06", 16),
            false);
    public static Header SCORE_TITLE_SPRITE_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("20", 16),
            (byte) Integer.parseInt("06", 16));
    public static Header MAP_SPRITE_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("5A", 16),
            (byte) Integer.parseInt("03", 16));
    public static Header MAP_TILES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("12", 16),
            (byte) Integer.parseInt("04", 16));
    public static Header MAP_ORDER_HEADER = new Header(
            (byte) Integer.parseInt("04", 16),
            (byte) Integer.parseInt("70", 16),
            (byte) Integer.parseInt("04", 16));
    public static Header TRAINING_MAP_SPRITE_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("40", 16),
            (byte) Integer.parseInt("03", 16));
    public static Header TRAINING_MAP_SPRITE_ORDER_HEADER = new Header(
            (byte) Integer.parseInt("04", 16),
            (byte) Integer.parseInt("1C", 16),
            (byte) Integer.parseInt("05", 16));
    public static Header TRAINING_MAP_SPRITE_TILES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("12", 16),
            (byte) Integer.parseInt("07", 16));
    public static Header TITLE_SCREEN_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("26", 16),
            (byte) Integer.parseInt("04", 16));
    public static Header TITLE_SCREEN_SPRITES_ORDER_HEADER = new Header(
            (byte) Integer.parseInt("80", 16),
            (byte) Integer.parseInt("14", 16),
            (byte) Integer.parseInt("08", 16));
    public static Header FREETOWN_BANNER_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("14", 16),
            (byte) Integer.parseInt("05", 16));
    public static Header DOC_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("14", 16),
            (byte) Integer.parseInt("06", 16));
    public static Header SALESMAN_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("0C", 16),
            (byte) Integer.parseInt("05", 16));
    public static Header ROSHAMBO_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("1C", 16),
            (byte) Integer.parseInt("05", 16));
    public static Header PENNY_OMG_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("10", 16),
            (byte) Integer.parseInt("05", 16));
    public static Header CHAPTER_END_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("20", 16),
            (byte) Integer.parseInt("05", 16));
    public static Header CHAPTER_START_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("20", 16),
            (byte) Integer.parseInt("05", 16));
    public static Header CHAPTER_START_SPRITES_ORDER_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("08", 16),
            (byte) Integer.parseInt("08", 16));
    public static Header RICO_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("20", 16),
            (byte) Integer.parseInt("04", 16));
    public static Header BATTLE_CARDS_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("80", 16),
            (byte) Integer.parseInt("0A", 16),
            (byte) Integer.parseInt("05", 16));
    public static Header BILL_SPRITES_HEADER = new Header(
            (byte) Integer.parseInt("00", 16),
            (byte) Integer.parseInt("12", 16),
            (byte) Integer.parseInt("04", 16));

    public Header(byte decompressedLengthLowByte, byte decompressedLengthHighByte, byte repeatLengthBits) {
        this.decompressedLengthLowByte = decompressedLengthLowByte;
        this.decompressedLengthHighByte = decompressedLengthHighByte;
        this.repeatLengthBits = repeatLengthBits;
    }

    public Header(byte decompressedLengthLowByte, byte decompressedLengthHighByte, byte repeatLengthBits, boolean optionStopFirstPattern) {
        this(decompressedLengthLowByte, decompressedLengthHighByte, repeatLengthBits);
        this.optionStopFirstPattern = optionStopFirstPattern;
    }

    public int getDecompressedLength() {
        int length = (decompressedLengthHighByte & 0xFF) << 8;
        length = (length & 0xFFFF) | (decompressedLengthLowByte & 0xFF);
        return length;
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[3];
        bytes[0] = decompressedLengthLowByte;
        bytes[1] = decompressedLengthHighByte;
        bytes[2] = repeatLengthBits;
        return bytes;
    }

    public int getPositionMask() {
        int i = 0xFF;
        i = i << getSizeShift();
        return i | 0xFF;
    }

    public int getSizeShift() {
        return 8 - repeatLengthBits;
    }

    public int getMaxSize() {
        int maxSize = (0xFF >> getSizeShift())+2;
        if (maxSize%2!=0) maxSize--;
        return maxSize;
    }

    public int getMaxPosition() {
        return getPositionMask();
    }

    public void setFlagLength(int flagLengthBits) {
        this.flagLengthBits = flagLengthBits;
    }

    public int getFlagLength() {
        return flagLengthBits;
    }

    public boolean isOptionStopFirstPattern() {
        return optionStopFirstPattern;
    }

    public void setOptionStopFirstPattern(boolean optionStopFirstPattern) {
        this.optionStopFirstPattern = optionStopFirstPattern;
    }
}
