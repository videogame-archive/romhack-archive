package dokapon.bps.utils;

import dokapon.bps.actions.ActionType;
import dokapon.bps.actions.Action;
import dokapon.bps.actions.CopyAction;
import dokapon.bps.actions.TargetReadAction;
import dokapon.bps.Patcher;
import dokapon.services.DataWriter;
import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    /**
     * Reads a variable-length number from data starting at offset.
     */
    public static long encode(byte[] data, int offset) {
        long res = 0;
        long shift = 1;
        int index = offset;
        while(true) {
            short x = (short)(data[index++] & 0xFF);
            res += (x & 0x7f) * shift;
            if(x >= 0x80) break;
            shift <<= 7;
            res += shift;
        }
        return res;
    }

    /**
     * Returns a variable-length number (as a byte array) representing data.
     */
    public static byte[] decode(long data) {
        List<Byte> byteList = new ArrayList<>();
        while(true) {
            short x = (short) (data & 0x7f);
            data >>= 7;
            if(data == 0) {
                byteList.add((byte) (0x80 | x));
                break;
            }
            byteList.add((byte) x);
            data--;
        }
        Byte[] bytes = byteList.toArray(new Byte[0]);
        return (ArrayUtils.toPrimitive(bytes));
    }

    /**
     * Returns the number of bytes required to represent number as a variable-length number
     */
    public static int byteSize(long number) {
        int size = 1;
        while (number>0) {
            number >>= 7;
            if (number>0) size++;
            number--;
        }
        return size;
    }

    /**
     * Returns the length of the action represented by data
     */
    public static int getLength(long data) {
        return (int) ((data >> 2) +1);
    }

    /**
     * Returns the action type of the action represented by data
     */
    public static ActionType getActionType(long data) {
        return ActionType.getAction((int) (data & 3));
    }

    /**
     * Returns the shift value (a signed int) represented by data.
     * This shift is only used by SourceCopyAction and TargetCopyAction
     */
    public static int getShift(long data) {
        int sign = (int) (data & 1);
        if (sign==0) sign=1;
        else sign=-1;
        data = data >> 1;
        return (int) (data * sign);
    }

    public static long readCrc(byte[] data) {
        long crc = 0;
        for (int index=0;index<data.length;index++) {
            crc += (data[index] & 0xFF) * (Math.pow(256, index));
        }
        return crc;
    }

    public static byte[] getCrcBytes(long crc) {
        byte[] data = new byte[4];
        for (int index=0;index<data.length;index++) {
            data[index] = (byte) (crc & 0xFF);
            crc >>= 8;
        }
        return data;
    }

    public static void saveData(File file, byte[] data) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            stream.write(data);
            stream.close();
        } catch (IOException ex) {
            Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    Logger.getLogger(DataWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static boolean containsOneByte(byte[] data) {
        for (int i = 0; i < data.length-1; i++) {
            if (data[i]!=data[i+1]) return false;
        }
        return true;
    }

    public static List<Action> optimizeTargetReadActions(TargetReadAction targetReadAction, int outputOffset) {
        List<Action> actionList = new ArrayList<>();
            if (targetReadAction.getData().length> Patcher.MAXIMUM_TARGET_READ_CHUNK_SIZE) {
                CopyAction.setTargetRelativeOffset(0);
                List<byte[]> list = splitArray(targetReadAction.getData(), Patcher.MINIMUM_TARGET_COPY_CHUNK_SIZE);
                for (byte[] bytes:list) {
                    if (containsOneByte(bytes)) {
                        TargetReadAction action = new TargetReadAction(1);
                        action.setData(Arrays.copyOfRange(bytes, 0, 1));
                        actionList.add(action);
                        outputOffset += action.getLength();
                        int shift = -(CopyAction.getTargetRelativeOffset() - (outputOffset-1));
                        CopyAction targetCopyAction = new CopyAction(ActionType.TARGET_COPY, bytes.length-1);
                        targetCopyAction.setShift(shift);
                        CopyAction.setTargetRelativeOffset(CopyAction.getTargetRelativeOffset()+shift+targetCopyAction.getLength());
                        actionList.add(targetCopyAction);
                        outputOffset += targetCopyAction.getLength();
                    } else {
                        TargetReadAction action = new TargetReadAction(bytes.length);
                        action.setData(bytes);
                        actionList.add(action);
                        outputOffset += action.getLength();
                    }
                }
            }
            else actionList.add(targetReadAction);
        return actionList;
    }

    public static byte[] concat(byte[] a, byte[] b) {
        byte[] tmp = new byte[a.length + b.length];
        System.arraycopy(a, 0, tmp, 0, a.length);
        System.arraycopy(b, 0, tmp, a.length, b.length);
        return tmp;
    }

    public static List<byte[]> splitArray(byte[] data, int chunkSize) {
        List<byte[]> rawCuts = new ArrayList<>();
        if (data.length > 0) {
            if (data.length==1) {
                rawCuts.add(data);
            }
            else {
                int index = 0;
                int cutStart = index;
                while (index < data.length-1) {
                    if (data[index]==data[index+1]) {
                        while (index < data.length-1 && data[index]==data[index+1]) {
                            index++;
                        }
                        byte[] copy = Arrays.copyOfRange(data, cutStart, index + 1);
                        rawCuts.add(copy);
                        cutStart = index+1;
                        index++;
                    }
                    else {
                        while (index < data.length-1 && data[index]!=data[index+1]) {
                            index++;
                        }
                        byte[] copy = Arrays.copyOfRange(data, cutStart, index);
                        rawCuts.add(copy);
                        cutStart = index;
                    }
                    if (index == data.length-1) {
                        byte[] copy = Arrays.copyOfRange(data, cutStart, index+1);
                        rawCuts.add(copy);
                    }
                }
            }
        }
        List<byte[]> cuts = new ArrayList<>();
        byte[] tmp = null;
        for (int i = 0; i < rawCuts.size(); i++) {
            byte[] cut = rawCuts.get(i);
            if (containsOneByte(cut) && cut.length>=chunkSize) {
                if (tmp!=null) {
                    cuts.add(tmp);
                    tmp = null;
                }
                cuts.add(cut);
            }
            else {
                if (tmp==null) tmp = cut;
                else {
                    tmp = concat(tmp, cut);
                }
                if (i == rawCuts.size()-1) {
                    cuts.add(tmp);
                }
            }
        }
        return cuts;
    }

}
