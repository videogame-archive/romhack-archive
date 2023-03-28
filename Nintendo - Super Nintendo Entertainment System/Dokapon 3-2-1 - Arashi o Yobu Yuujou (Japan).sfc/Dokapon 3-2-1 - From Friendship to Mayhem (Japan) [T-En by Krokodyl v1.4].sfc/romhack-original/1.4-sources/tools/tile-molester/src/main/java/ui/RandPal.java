package ui;

public class RandPal {

    public static void main(final String[] args) {
        for (int i=0; i<32; i++) {
            for (int j=0; j<8; j++) {
                if (j > 0) System.out.print(", ");
                int r = (int)(Math.random()*255.0) & 0xFF;
                int g = (int)(Math.random()*255.0) & 0xFF;
                int b = (int)(Math.random()*255.0) & 0xFF;
                r ^= (int)(Math.random()*255.0) & 0xFF;
                g ^= (int)(Math.random()*255.0) & 0xFF;
                b ^= (int)(Math.random()*255.0) & 0xFF;
                int rgb = (r << 16) | (g << 8) | b;
                System.out.print("0x"+Integer.toHexString(rgb).toUpperCase());
            }
            System.out.println(",");
        }
    }

}
