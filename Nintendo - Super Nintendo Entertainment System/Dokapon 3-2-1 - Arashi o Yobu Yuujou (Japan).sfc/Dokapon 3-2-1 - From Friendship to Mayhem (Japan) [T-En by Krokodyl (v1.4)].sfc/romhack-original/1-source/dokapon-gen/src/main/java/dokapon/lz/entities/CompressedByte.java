package dokapon.lz.entities;

import dokapon.services.Utils;

public abstract class CompressedByte {

    public abstract byte[] getBytes();

    public String getBytesHex() {
        String res = "";
        for (byte b:getBytes()) {
            res += Utils.toHexString(b)+" ";
        }
        return res.trim();
    }
}
