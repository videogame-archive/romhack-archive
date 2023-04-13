package dokapon.characters;

import java.util.Objects;

public class SpecialChar {

    String code;
    int inGameLength;
    int dataLength;

    public SpecialChar(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getInGameLength() {
        return inGameLength;
    }

    public void setInGameLength(int inGameLength) {
        this.inGameLength = inGameLength;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecialChar that = (SpecialChar) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
