package unbyte.compressor;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static unbyte.compressor.LZWCompressor.getNBitRepresentation;

public class LZWCompressorTest {

    private LZWCompressor lzw;

    @Before
    public void init() {
        lzw = new LZWCompressor();
    }

    @Test
    public void getNBitRepresentationTest() {
        System.out.println(getNBitRepresentation(32, 7));
        assertEquals("0100000", getNBitRepresentation(32, 7));
    }

    @Test
    public void getNegativeNBitTest() {
        assertEquals("11000011", lzw.getNegativeNBit("à".getBytes()[0], 8));
    }

    @Test
    public void transform() throws IOException {
        HashMap<String,String> dictionnaire = new HashMap<>();
        for(int i = 1 ; i<256; i++) {
            dictionnaire.put(Character.toString((char)i), getNBitRepresentation(i,lzw.NB_BIT));
        }
        String string = "0B/ ";
        InputStream stream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        byte[] input = stream.readAllBytes();
        assertEquals("000000110000000001000010000000101111000000100000", lzw.transform(dictionnaire, input));
    }

    @Test
    public void translateTest() throws IOException {
        String string = "000000110000000001000010000000101111000000100000";
        InputStream stream = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        byte[] input = stream.readAllBytes();
        String raw = "";
        for (byte value : input) {
            if (value < 0) {
                raw += lzw.getNegativeNBit(value, 8);
            } else {
                raw += getNBitRepresentation(value, 8);
            }
        }
        HashMap<String,String> dictionnaire = new HashMap<>();
        for(int i = 1 ; i<256; i++) {
            dictionnaire.put(getNBitRepresentation(i,lzw.NB_BIT), Character.toString((char)i));
        }
        assertEquals("0000000000110000000011110000000", lzw.translate(dictionnaire, raw));
    }
}
