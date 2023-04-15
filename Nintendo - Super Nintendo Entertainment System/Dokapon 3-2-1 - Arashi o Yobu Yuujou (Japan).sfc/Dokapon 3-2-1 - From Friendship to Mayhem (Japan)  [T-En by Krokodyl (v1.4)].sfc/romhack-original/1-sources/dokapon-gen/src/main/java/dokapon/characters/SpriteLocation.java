package dokapon.characters;

import dokapon.enums.CharSide;
import dokapon.services.Utils;

import java.util.Objects;

public class SpriteLocation {

    int offset;
    CharSide side;

    public SpriteLocation(int offset) {
        this.offset = offset;
    }

    public SpriteLocation(int offset, CharSide side) {
        this.offset = offset;
        this.side = side;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public CharSide getSide() {
        return side;
    }

    public void setSide(CharSide side) {
        this.side = side;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpriteLocation that = (SpriteLocation) o;
        return offset == that.offset &&
                side == that.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, side);
    }

    @Override
    public String toString() {
        return "SpriteLocation{" +
                "offset=" + Utils.toHexString(offset,6) +
                ", side=" + side +
                '}';
    }
}
