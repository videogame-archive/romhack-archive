package dokapon.lz.entities;

import java.util.Objects;

public class CompressionPattern implements Comparable {

    String pattern;
    int countConsecutive;
    int countNonOverlapping;

    public CompressionPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getCompressionGain() {
        return (pattern.length() / 2) * (getCountNonOverlapping() - 1);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public int getCountConsecutive() {
        return countConsecutive;
    }

    public void setCountConsecutive(int countConsecutive) {
        this.countConsecutive = countConsecutive;
    }

    public int getCountNonOverlapping() {
        return countNonOverlapping;
    }

    public void setCountNonOverlapping(int countNonOverlapping) {
        this.countNonOverlapping = countNonOverlapping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompressionPattern that = (CompressionPattern) o;
        return countConsecutive == that.countConsecutive && countNonOverlapping == that.countNonOverlapping && Objects.equals(pattern, that.pattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, countConsecutive, countNonOverlapping);
    }

    @Override
    public String toString() {
        return "CompressionPattern{" +
                "pattern='" + pattern + '\'' +
                "length='" + pattern.length() + '\'' +
                ", countConsecutive=" + countConsecutive +
                ", countNonOverlapping=" + countNonOverlapping +
                ", compression=" + getCompressionGain() +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        CompressionPattern cp = (CompressionPattern)o;
        int result = cp.getPattern().length()-getPattern().length();
        if (result==0) {
            result = cp.getCountNonOverlapping()-getCountNonOverlapping();
            if (result==0) {
                result = cp.getCountConsecutive()-getCountConsecutive();
                if (result==0) {
                    result = cp.getCompressionGain()-getCompressionGain();
                    if (result==0) {
                        return 0;
                    }
                }
            }
        }
        return result;
    }
}
