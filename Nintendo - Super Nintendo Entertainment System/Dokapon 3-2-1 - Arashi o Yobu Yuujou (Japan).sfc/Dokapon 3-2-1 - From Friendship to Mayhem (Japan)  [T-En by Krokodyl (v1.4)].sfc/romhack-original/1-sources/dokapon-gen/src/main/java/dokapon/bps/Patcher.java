package dokapon.bps;

import dokapon.bps.actions.ActionType;
import dokapon.bps.actions.Action;
import dokapon.bps.actions.CopyAction;
import dokapon.bps.actions.TargetReadAction;
import dokapon.bps.sections.Data;
import dokapon.bps.sections.Footer;
import dokapon.bps.sections.Header;
import dokapon.bps.utils.Utils;
import dokapon.services.DataWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that can generate or apply BPS files.
 */
public class Patcher {

    public static final int MINIMUM_TARGET_COPY_CHUNK_SIZE = 20;
    public static final int MAXIMUM_TARGET_READ_CHUNK_SIZE = 200;

    Header header;
    Data data;
    Footer footer;

    private void parsePatchFile(File patchFile) throws IOException {
        byte[] patchData = Files.readAllBytes(patchFile.toPath());
        header = new Header(patchData);
        data = new Data(patchData, header.getLength());
        footer = new Footer(patchData, header.getLength()+data.getLength());
    }

    public void applyPatch(File patchFile, File sourceFile, File outputFile) throws IOException {
        parsePatchFile(patchFile);
        byte[] sourceData = Files.readAllBytes(sourceFile.toPath());
        byte[] patchData = Files.readAllBytes(patchFile.toPath());
        byte[] outputData = new byte[(int) header.getModifiedFileSize()];
        int offset = 0;
        for (Action a:data.getActionList()) {
            offset = a.applyAction(patchData, sourceData, offset, outputData);
        }
        Utils.saveData(outputFile, outputData);
    }

    public static void generatePatch(File sourceFile, File targetFile, File patchFile, String metadata) {

        FileOutputStream stream = null;
        try {
        byte[] sourceData = Files.readAllBytes(sourceFile.toPath());
        byte[] targetData = Files.readAllBytes(targetFile.toPath());


        Header header = new Header(sourceData.length, targetData.length, metadata);
        Data data = new Data(sourceFile, targetFile);
        Footer footer = new Footer(sourceFile, targetFile);

            stream = new FileOutputStream(patchFile);

            stream.write(header.getBytes());

            List<Action> actions = data.getActionList();
            CopyAction.setTargetRelativeOffset(header.getLength());

            /*
             * Optimization
             */
            actions = addTargetCopyActions(actions);
            actions = mergeConsecutiveTargetReadActions(actions);

            for (Action action: actions) {
                byte[] actionBytes = action.getBytes();
                stream.write(actionBytes);
            }
            stream.write(footer.getBytes());
            footer.generateCrcPatch(patchFile);
            stream.write(footer.getPatchBytes());
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

    private static List<Action> addTargetCopyActions(List<Action> actionList) {
        List<Action> res = new ArrayList<>();
        int outputOffset = 0;
        for (Action action:actionList) {
            if (action.getType()== ActionType.TARGET_READ) {
                List<Action> optimizeActionList = Utils.optimizeTargetReadActions((TargetReadAction) action, outputOffset);
                for (Action optimizedAction:optimizeActionList) {
                    res.add(optimizedAction);
                    outputOffset += action.getLength();
                }
            }
            else {
                res.add(action);
                outputOffset += action.getLength();
            }
        }
        return res;
    }

    private static List<Action> mergeConsecutiveTargetReadActions(List<Action> actionList){
        List<Action> res = new ArrayList<>();
        int index = 0;
        while (index<actionList.size()) {
            Action currentAction = actionList.get(index);
            if (currentAction.getType()!=ActionType.TARGET_READ) {
                res.add(currentAction);
                index++;
            }
            else {
                TargetReadAction currentTargetAction = (TargetReadAction)currentAction;
                int nextIndex = index +1 ;
                Action nextAction = actionList.get(nextIndex);
                while (nextAction.getType()==ActionType.TARGET_READ) {
                    TargetReadAction nextTargetAction = (TargetReadAction)nextAction;
                    currentTargetAction.appendData(nextTargetAction.getData());
                    nextIndex++;
                    index++;
                    nextAction = actionList.get(nextIndex);
                }
                res.add(currentTargetAction);
                index++;
            }
        }
        return res;

    }


}
