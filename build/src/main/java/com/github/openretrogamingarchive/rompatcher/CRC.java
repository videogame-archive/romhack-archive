/* Apache 2 License, Copyright (c) 2023 Juan Fuentes, based on Rom Patcher JS by Marc Robledo */
package com.github.openretrogamingarchive.rompatcher;

import java.util.Arrays;
public class CRC {
    private static int[] CRC32_TABLE = new int[256];

    static {
        int c;
        for(int n=0;n<256;n++){
            c=n;
            for(var k=0;k<8;k++)
                c=(((c&1) > 0)?(0xedb88320^(c>>>1)):(c>>>1));
            CRC32_TABLE[n]=c;
        }
    }

    public static long crc32(MarcFile marcFile){
        return crc32(marcFile, 0, false);
    }

    public static long crc32(MarcFile marcFile, int headerSize, boolean ignoreLast4Bytes){
        int[] data= Arrays.copyOfRange(marcFile._u8array, headerSize, ((ignoreLast4Bytes)?marcFile._u8array.length-4:marcFile._u8array.length));

        int crc=0^(-1);

        int len=data.length;
        for(var i=0;i<len;i++)
            crc=(crc>>>8)^CRC32_TABLE[((int)(crc^data[i]))&0xff];

        return ((crc^(-1))>>>0);
    }
}
