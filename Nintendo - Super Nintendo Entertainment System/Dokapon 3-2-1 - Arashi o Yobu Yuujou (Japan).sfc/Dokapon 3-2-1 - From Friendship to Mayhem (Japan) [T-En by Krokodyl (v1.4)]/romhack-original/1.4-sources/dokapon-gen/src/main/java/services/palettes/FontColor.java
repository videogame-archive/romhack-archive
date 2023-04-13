package services.palettes;

public enum FontColor {


    MAP_1BPP_COLOR_01(Integer.parseInt("00000000",2)),
    MAP_1BPP_COLOR_02(Integer.parseInt("10000000",2)),
    
    MAP_2BPP_COLOR_01(Integer.parseInt("0000000000000000",2)),
    MAP_2BPP_COLOR_02(Integer.parseInt("1000000000000000",2)),
    MAP_2BPP_COLOR_03(Integer.parseInt("0000000010000000",2)),
    MAP_2BPP_COLOR_04(Integer.parseInt("1000000010000000",2)),

    MAP_COLOR_01(Long.parseLong("00000000000000000000000000000000",2)),
    MAP_COLOR_02(Long.parseLong("10000000000000000000000000000000",2)),
    MAP_COLOR_03(Long.parseLong("00000000100000000000000000000000",2)),
    MAP_COLOR_04(Long.parseLong("10000000100000000000000000000000",2)),

    MAP_COLOR_05(Long.parseLong("00000000000000001000000000000000",2)),
    MAP_COLOR_06(Long.parseLong("10000000000000001000000000000000",2)),
    MAP_COLOR_07(Long.parseLong("00000000100000001000000000000000",2)),
    MAP_COLOR_08(Long.parseLong("10000000100000001000000000000000",2)),

    MAP_COLOR_09(Long.parseLong("00000000000000000000000010000000",2)),
    MAP_COLOR_10(Long.parseLong("10000000000000000000000010000000",2)),
    MAP_COLOR_11(Long.parseLong("00000000100000000000000010000000",2)),
    MAP_COLOR_12(Long.parseLong("10000000100000000000000010000000",2)),

    MAP_COLOR_13(Long.parseLong("00000000000000001000000010000000",2)),
    MAP_COLOR_14(Long.parseLong("10000000000000001000000010000000",2)),
    MAP_COLOR_15(Long.parseLong("00000000100000001000000010000000",2)),
    MAP_COLOR_16(Long.parseLong("10000000100000001000000010000000",2));

    long mask;

    FontColor(int m) {
        mask = m;
    }

    FontColor(long m) {
        mask = m;
    }

    public int getMask() {
        return (int) mask;
    }

    public long getLongMask() {
        return mask;
    }
    
    public static FontColor valueOfPattern(String pattern) {
        long mask = Long.parseLong(pattern, 2);
        for (FontColor e : values()) {
            if (e.getLongMask() == mask) {
                return e;
            }
        }
        return null;
    }
}
