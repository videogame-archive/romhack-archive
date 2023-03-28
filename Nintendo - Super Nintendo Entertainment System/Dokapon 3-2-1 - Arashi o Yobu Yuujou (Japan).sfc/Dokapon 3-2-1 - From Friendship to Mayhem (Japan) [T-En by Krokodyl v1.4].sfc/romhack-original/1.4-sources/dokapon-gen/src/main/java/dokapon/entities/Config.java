package dokapon.entities;

public class Config {

    String romInput;
    String romOutput;
    String bpsPatchOutput;
    String fileDicoJap;
    String fileDicoLatin;
    String fileDicoNames;

    public String getRomInput() {
        return romInput;
    }

    public void setRomInput(String romInput) {
        this.romInput = romInput;
    }

    public String getRomOutput() {
        return romOutput;
    }

    public void setRomOutput(String romOutput) {
        this.romOutput = romOutput;
    }

    public String getBpsPatchOutput() {
        return bpsPatchOutput;
    }

    public void setBpsPatchOutput(String bpsPatchOutput) {
        this.bpsPatchOutput = bpsPatchOutput;
    }

    public String getFileDicoJap() {
        return fileDicoJap;
    }

    public void setFileDicoJap(String fileDicoJap) {
        this.fileDicoJap = fileDicoJap;
    }

    public String getFileDicoLatin() {
        return fileDicoLatin;
    }

    public void setFileDicoLatin(String fileDicoLatin) {
        this.fileDicoLatin = fileDicoLatin;
    }

    public String getFileDicoNames() {
        return fileDicoNames;
    }

    public void setFileDicoNames(String fileDicoNames) {
        this.fileDicoNames = fileDicoNames;
    }
}
