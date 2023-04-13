package dokapon.bps.sections;

import dokapon.bps.utils.Utils;

import java.util.Arrays;

public class Header {

    private static String HEADER_BSP1 = "BPS1";

    private final long originalFileSize;
    private final long modifiedFileSize;
    private final long metadataSize;
    private final String metadata;

    private final int length;

    public Header(byte[] patchData) {
        Integer offset = 0;
        byte[] bsp1 = Arrays.copyOfRange(patchData, offset, offset + HEADER_BSP1.length());
        offset += HEADER_BSP1.length();
        originalFileSize = Utils.encode(patchData, offset);
        offset += Utils.byteSize(originalFileSize);
        modifiedFileSize = Utils.encode(patchData, offset);
        offset += Utils.byteSize(modifiedFileSize);
        metadataSize = Utils.encode(patchData, offset);
        offset += Utils.byteSize(metadataSize);
        if (metadataSize>0) {
            metadata = new String(Arrays.copyOfRange(patchData, offset, (int) (offset + metadataSize)));
        } else metadata = "";
        length = (int) (offset + metadataSize);
    }

    public Header(long originalFileSize, long modifiedFileSize, String metadata) {
        this.originalFileSize = originalFileSize;
        this.modifiedFileSize = modifiedFileSize;
        this.metadata = metadata;
        this.metadataSize = metadata.length();
        length = (int) (HEADER_BSP1.length() + Utils.byteSize(originalFileSize) + Utils.byteSize(modifiedFileSize) + Utils.byteSize(metadataSize) + metadataSize);
    }

    public byte[] getBytes() {
        int index = 0;
        byte[] bytes = new byte[length];
        System.arraycopy(HEADER_BSP1.getBytes(), 0, bytes, index, HEADER_BSP1.length());
        index += HEADER_BSP1.length();
        byte[] data = Utils.decode(originalFileSize);
        System.arraycopy(data, 0, bytes, index, data.length);
        index += data.length;
        data = Utils.decode(modifiedFileSize);
        System.arraycopy(data, 0, bytes, index, data.length);
        index += data.length;
        data = Utils.decode(metadataSize);
        System.arraycopy(data, 0, bytes, index, data.length);
        index += data.length;
        if (metadataSize>0) {
            System.arraycopy(metadata.getBytes(), 0, bytes, index, metadata.length());
            index += metadata.length();
        }
        return bytes;
    }

    public long getOriginalFileSize() {
        return originalFileSize;
    }

    public long getModifiedFileSize() {
        return modifiedFileSize;
    }

    public long getMetadataSize() {
        return metadataSize;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Header{" +
                "originalFileSize=" + originalFileSize +
                ", modifiedFileSize=" + modifiedFileSize +
                ", metadataSize=" + metadataSize +
                ", metadata='" + metadata + '\'' +
                ", length=" + length +
                '}';
    }
}
