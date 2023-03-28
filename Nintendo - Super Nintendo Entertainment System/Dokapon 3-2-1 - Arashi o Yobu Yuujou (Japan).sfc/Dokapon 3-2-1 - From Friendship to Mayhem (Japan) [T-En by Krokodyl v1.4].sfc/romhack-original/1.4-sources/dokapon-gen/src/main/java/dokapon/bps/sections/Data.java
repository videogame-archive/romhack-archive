package dokapon.bps.sections;

import dokapon.bps.actions.ActionType;
import dokapon.bps.actions.Action;
import dokapon.bps.actions.SourceReadAction;
import dokapon.bps.actions.CopyAction;
import dokapon.bps.actions.TargetReadAction;
import dokapon.bps.utils.Utils;
import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {

    List<Action> actionList = new ArrayList<>();
    int length = 0;

    public Data(File sourceFile, File targetFile) throws IOException {
        byte[] sourceData = Files.readAllBytes(sourceFile.toPath());
        byte[] targetData = Files.readAllBytes(targetFile.toPath());
        int sourceOffset = 0;
        int targetOffset = 0;
        boolean end = false;
        ActionType type = null;
        int length = 0;
        List<Byte> data = new ArrayList<>();

        while (targetOffset<targetData.length) {
            while (targetOffset<sourceData.length && sourceData[targetOffset]==targetData[targetOffset]) {
                type = ActionType.SOURCE_READ;
                length++;
                targetOffset++;
            }
            if (type == ActionType.SOURCE_READ) {
                SourceReadAction sourceReadAction = new SourceReadAction(length);
                actionList.add(sourceReadAction);
                length=0;
                type=null;
            }
            while ((targetOffset>=sourceData.length && targetOffset<targetData.length) || (targetOffset<sourceData.length && sourceData[targetOffset]!=targetData[targetOffset])) {
                type = ActionType.TARGET_READ;
                length++;
                data.add(targetData[targetOffset]);
                targetOffset++;
            }
            if (type == ActionType.TARGET_READ) {
                TargetReadAction targetReadAction = new TargetReadAction(length);
                Byte[] bytes = data.toArray(new Byte[0]);
                targetReadAction.setData(ArrayUtils.toPrimitive(bytes));
                actionList.add(targetReadAction);
                data.clear();
                length=0;
                type=null;
            }
        }

        for (Action a:actionList) {
            if (a.getType()==ActionType.TARGET_READ) {
                /*TargetReadAction action = (TargetReadAction) a;
                int i = action.getData().length;
                if (i>10) System.out.println(Arrays.toString(action.getData()));*/
            }
        }
    }

    public Data(byte[] patchData, int startFrom) {
        int offset = startFrom;
        int targetOffset = 0;
        int sourceOffset = 0;
        while (offset<patchData.length-12) {
            //System.out.println("read action "+offset);
            long read = Utils.encode(patchData, offset);
            offset += Utils.byteSize(read);
            switch (Utils.getActionType(read)) {
                case SOURCE_READ:
                    SourceReadAction sourceReadAction = new SourceReadAction(Utils.getLength(read));
                    actionList.add(sourceReadAction);
                    break;
                case TARGET_READ:
                    TargetReadAction targetReadAction = new TargetReadAction(Utils.getLength(read));
                    byte[] data = Arrays.copyOfRange(patchData, offset, offset + targetReadAction.getLength());
                    targetReadAction.setData(data);
                    offset += targetReadAction.getLength();
                    actionList.add(targetReadAction);
                    break;
                case SOURCE_COPY:
                    CopyAction sourceCopyAction = new CopyAction(ActionType.SOURCE_COPY, Utils.getLength(read));
                    long copyOffset = Utils.encode(patchData, offset);
                    offset += Utils.byteSize(copyOffset);
                    sourceOffset += Utils.getShift(copyOffset);
                    sourceCopyAction.setShift(Utils.getShift(copyOffset));
                    sourceOffset += sourceCopyAction.getLength();
                    actionList.add(sourceCopyAction);
                    break;
                case TARGET_COPY:
                    CopyAction targetCopyAction = new CopyAction(ActionType.TARGET_COPY, Utils.getLength(read));
                    copyOffset = Utils.encode(patchData, offset);
                    offset += Utils.byteSize(copyOffset);
                    targetOffset += Utils.getShift(copyOffset);
                    targetCopyAction.setShift(Utils.getShift(copyOffset));
                    targetOffset += targetCopyAction.getLength();
                    actionList.add(targetCopyAction);
                    break;
            }
        }
        length = patchData.length - startFrom - 12;
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public int getLength() {
        return length;
    }
}
