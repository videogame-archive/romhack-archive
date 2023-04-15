package dokapon.characters;

import dokapon.enums.CharType;

import java.util.Objects;

public class LatinChar {

    String value;       // A    {DC-AZ}
    String igValue;     // A    AZ
    String code;        // e000
    SpriteLocation spriteLocation;
    LatinSprite sprite;
    CharType type;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIgValue() {
        return igValue;
    }

    public void setIgValue(String igValue) {
        this.igValue = igValue;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SpriteLocation getSpriteLocation() {
        return spriteLocation;
    }

    public void setSpriteLocation(SpriteLocation spriteLocation) {
        this.spriteLocation = spriteLocation;
    }

    public LatinSprite getSprite() {
        return sprite;
    }

    public void setSprite(LatinSprite sprite) {
        this.sprite = sprite;
    }

    public CharType getType() {
        return type;
    }

    public void setType(CharType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatinChar latinChar = (LatinChar) o;
        return Objects.equals(value, latinChar.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        String spriteLoc = "";
        String image = "";
        if (sprite!=null && type==CharType.SINGLE) {
            image = "\"sprite\": {\n" +
                    "\"image\": \""+sprite.getImage()+"\"\n" +
                    "}\n";
        }
        if (sprite!=null && type==CharType.DOUBLE_STRAIGHT) {
            image = "\"sprite\": {\n" +
                    "\"image-top\": \""+sprite.getImageTop()+"\",\n" +
                    "\"image-bot\": \""+sprite.getImageBot()+"\"\n" +
                    "}\n";
        }

        String s =
        "{\n" +
                "        \"value\": \""+value+"\",\n" +
                "        \"type\": \""+type+"\",\n" +
                spriteLoc+
                image+
                "      },";
        return s;
    }

    public String toPrintString() {
        return "LatinChar{" +
                "value='" + value + '\'' +
                ", igValue='" + igValue + '\'' +
                ", code='" + code + '\'' +
                ", spriteLocation=" + spriteLocation +
                ", sprite=" + sprite +
                ", type=" + type +
                '}';
    }
}
