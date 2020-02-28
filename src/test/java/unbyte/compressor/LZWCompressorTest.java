package unbyte.compressor;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LZWCompressorTest {

    private LZWCompressor lzw;

    @Before
    public void init(){
        lzw = new LZWCompressor();
    }

    @Test
    public void getNBitRepresentationTest() {
        System.out.println(LZWCompressor.getNBitRepresentation(32, 7));
        assertEquals("0100000", LZWCompressor.getNBitRepresentation(32, 7));
    }

    @Test
    public void getNegativeNBitTest() {
        assertEquals("11000011", lzw.getNegativeNBit("à".getBytes()[0], 8));
    }
}
