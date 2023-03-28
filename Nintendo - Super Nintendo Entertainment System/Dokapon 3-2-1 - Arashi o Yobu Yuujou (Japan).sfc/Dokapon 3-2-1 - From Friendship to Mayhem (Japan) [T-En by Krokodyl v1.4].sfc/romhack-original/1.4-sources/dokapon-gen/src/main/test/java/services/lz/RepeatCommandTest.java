package services.lz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepeatCommandTest {

    @Test
    void testRepeat3Bits() {
        RepeatCommand repeatCommand = new RepeatCommand(1, 14, REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_5BITS);
        System.out.println(repeatCommand);
        repeatCommand = new RepeatCommand(1, 14, REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_3BITS);
        System.out.println(repeatCommand);
        repeatCommand = REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_3BITS.buildRepeatCommand((byte) 0x80, (byte) 0x01);
        System.out.println(repeatCommand);
    }
    
    @Test
    void testShift() {
        REPEAT_ALGORITHM algorithm = REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_5BITS;
        RepeatCommand repeatCommand = new RepeatCommand(2057, 8, algorithm);
        System.out.println(repeatCommand);
        repeatCommand = algorithm.buildRepeatCommand((byte) 0x38, (byte) 0x09);
        System.out.println(repeatCommand);
    }
    
    @Test
    void testBytes() {
        REPEAT_ALGORITHM algorithm = REPEAT_ALGORITHM.REPEAT_ALGORITHM_SIZE_8BITS;
        RepeatCommand repeatCommand = algorithm.buildRepeatCommand((byte) 0x1D, (byte) 0x01);
        System.out.println(repeatCommand);
    }
}