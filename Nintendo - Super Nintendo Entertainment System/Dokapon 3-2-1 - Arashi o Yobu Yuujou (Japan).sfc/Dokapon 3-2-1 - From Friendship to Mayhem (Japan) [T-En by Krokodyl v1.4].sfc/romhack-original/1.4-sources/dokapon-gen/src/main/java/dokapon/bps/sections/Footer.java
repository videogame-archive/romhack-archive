package dokapon.bps.sections;

import dokapon.bps.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.zip.CRC32;

public class Footer {

    private long crcSource;
    private long crcTarget;
    private long crcPatch;

    public Footer(byte[] patchData, int startFrom) {
        crcSource = Utils.readCrc(Arrays.copyOfRange(patchData, startFrom, startFrom+4));
        crcTarget = Utils.readCrc(Arrays.copyOfRange(patchData, startFrom+4, startFrom+8));
        crcPatch = Utils.readCrc(Arrays.copyOfRange(patchData, startFrom+8, startFrom+12));
    }

    public Footer(File sourceFile, File targetFile) throws IOException {
        byte[] data = Files.readAllBytes(sourceFile.toPath());
        CRC32 crc = new CRC32();
        crc.update(data);
        crcSource = crc.getValue();
        data = Files.readAllBytes(targetFile.toPath());
        crc = new CRC32();
        crc.update(data);
        crcTarget = crc.getValue();
    }

    public void generateCrcPatch(File patchFile) throws IOException {
        byte[] data = Files.readAllBytes(patchFile.toPath());
        CRC32 crc = new CRC32();
        crc.update(data);
        crcPatch = crc.getValue();
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[8];
        System.arraycopy(Utils.getCrcBytes(crcSource), 0, bytes, 0, 4);
        System.arraycopy(Utils.getCrcBytes(crcTarget), 0, bytes, 4, 4);
        return bytes;
    }

    public byte[] getPatchBytes() {
        return Utils.getCrcBytes(crcPatch);
    }

    @Override
    public String toString() {
        return "Footer{" +
                "crcSource=" + crcSource +
                ", crcTarget=" + crcTarget +
                ", crcPatch=" + crcPatch +
                '}';
    }
}
