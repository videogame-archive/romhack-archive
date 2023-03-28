package dokapon.entities;

public class LignePixel {

    private int offset = 0;
    private int[] data = new int[32];
    static int[] valeursPixel = new int[8];

    static {
        valeursPixel[0] = Integer.parseInt("80", 16);
        valeursPixel[1] = Integer.parseInt("40", 16);
        valeursPixel[2] = Integer.parseInt("20", 16);
        valeursPixel[3] = Integer.parseInt("10", 16);
        valeursPixel[4] = Integer.parseInt("8", 16);
        valeursPixel[5] = Integer.parseInt("4", 16);
        valeursPixel[6] = Integer.parseInt("2", 16);
        valeursPixel[7] = Integer.parseInt("1", 16);
    }

    public LignePixel(int offset, int[] data) {

    }

    public void setPixelColor(int ligne, int pixel, int couleur) {
        int val = valeursPixel[pixel];
        switch (couleur) {
            case 1:
                break;
            case 2: {
                data[ligne] += val;
                break;
            }
            case 3: {
                data[ligne + 1] += val;
                break;
            }
            case 4: {
                data[ligne] += val;
                data[ligne + 1] += val;
                break;
            }
            case 5: {
                data[ligne + 16] += val;
                break;
            }
            case 6: {
                data[ligne] += val;
                data[ligne + 16] += val;
                break;
            }
            case 7: {
                data[ligne + 1] += val;
                data[ligne + 16] += val;
                break;
            }
            case 8: {
                data[ligne] += val;
                data[ligne + 1] += val;
                data[ligne + 16] += val;
                break;
            }
            case 9:{
                data[ligne + 17] += val;
                break;
            }
            case 10: {
                data[ligne] += val;
                data[ligne + 17] += val;
                break;
            }
            case 11: {
                data[ligne + 1] += val;
                data[ligne + 17] += val;
                break;
            }
            case 12: {
                data[ligne] += val;
                data[ligne + 1] += val;
                data[ligne + 17] += val;
                break;
            }
            case 13: {
                data[ligne + 16] += val;
                data[ligne + 17] += val;
                break;
            }
            case 14: {
                data[ligne] += val;
                data[ligne + 16] += val;
                data[ligne + 17] += val;
                break;
            }
            case 15: {
                data[ligne + 1] += val;
                data[ligne + 16] += val;
                data[ligne + 17] += val;
                break;
            }
            case 16: {
                data[ligne] += val;
                data[ligne + 1] += val;
                data[ligne + 16] += val;
                data[ligne + 17] += val;
                break;
            }
        }
    }

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    @Override
    public String toString() {
        String s = "";
        for (int i:data) {
            s += (Integer.toHexString(i));
        }
        return s;
    }
}
