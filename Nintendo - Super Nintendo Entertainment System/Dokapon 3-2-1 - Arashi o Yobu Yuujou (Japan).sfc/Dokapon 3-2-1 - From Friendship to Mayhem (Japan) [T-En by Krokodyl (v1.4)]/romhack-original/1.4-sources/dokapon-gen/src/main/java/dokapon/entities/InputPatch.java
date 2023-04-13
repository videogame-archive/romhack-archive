package dokapon.entities;

import dokapon.characters.LatinChar;

import java.util.List;

public class InputPatch extends CodePatch {

    String latin;
    InputPatchType type;


    public InputPatch(int offset) {
        super(offset);
    }

    public String getLatin() {
        return latin;
    }

    public void setLatin(String latin) {
        this.latin = latin;
    }

    public InputPatchType getType() {
        return type;
    }

    public void setType(InputPatchType type) {
        this.type = type;
    }

    public void generateCode(List<LatinChar> latinChars) {
        code = "";
        if (type==InputPatchType.INPUT_LETTERS_SHOWN) {
            for (int k=0;k<latin.length();k++) {
                String c = ""+latin.charAt(k);
                if (c.equals("#")) {
                    code += "00 30 ";
                }
                else if (c.equals("*")) {
                    code += "ff ff ";
                } else {
                    for (LatinChar lc : latinChars) {
                        if (lc.getValue().equals(c)) {
                            code += lc.getCode().substring(0, 2) + " " + lc.getCode().substring(2, 4) + " ";
                        }
                    }
                }
            }
        } else if (type==InputPatchType.INPUT_LETTERS_SAVED){
            code = "";
            for (String s:latin.split(" ")) {
                if (s.length()==1) {
                    for (LatinChar l:latinChars) {
                        if (l.getValue()!=null && l.getValue().equals(s)) {
                            code += l.getCode().substring(0,2)+" ";
                        }
                    }
                } else code+=s+" ";
            }
        } else if (type==InputPatchType.PLAYER) {
            code = "";
            for (String s:latin.split(" ")) {
                if (s.length()==1) {
                    for (LatinChar l:latinChars) {
                        if (l.getValue()!=null && l.getValue().equals(s)) {
                            code += l.getCode().substring(0,2)+" "+l.getCode().substring(2,4)+" ";
                        }
                    }
                } else code+=s+" ";
            }
        }
        code = code.trim();
    }

    public enum InputPatchType {
        PLAYER,
        INPUT_LETTERS_SAVED,
        INPUT_LETTERS_SHOWN
    }
}
