package dokapon.entities;

import static dokapon.services.Utils.bytesToHex;

public class CodePatch {

    String code;
    int offset;
    boolean debug = false;
    boolean file = false;

    public CodePatch(int offset) {
        this.offset = offset;
    }

    public CodePatch(String code, int offset) {
        this.code = code;
        this.offset = offset;
    }

    public CodePatch(byte[] code, int offset) {
        this.code = bytesToHex(code);
        this.offset = offset;
    }

    public byte[] writePatch(byte[] data) {
        String[] codeHexa = code.split(" ");
        int[] codeInt = new int[codeHexa.length];
        int z = 0;
        for (String s:codeHexa) {
            codeInt[z++]=Integer.parseInt(s, 16);
        }
        for (int i=0;i<codeInt.length;i++) data[offset+i]= (byte) (codeInt[i] & 0xFF);
        return data;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
