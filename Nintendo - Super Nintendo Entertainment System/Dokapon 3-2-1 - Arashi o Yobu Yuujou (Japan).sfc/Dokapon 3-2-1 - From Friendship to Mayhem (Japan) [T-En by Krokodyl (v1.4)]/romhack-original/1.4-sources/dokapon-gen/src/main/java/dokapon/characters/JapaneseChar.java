package dokapon.characters;

import dokapon.enums.CharType;

public class JapaneseChar {

    String value;
    String code;
    CharType type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CharType getType() {
        return type;
    }

    public void setType(CharType type) {
        this.type = type;
    }
}
