package unbyte.compressor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class AppTest {

    private App app;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Before
    public void init(){
        app = new App();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restore(){
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testContinuousIntegrationTrue(){
        assertTrue(true);
    }

    @Test
    public void getAlgorithmTest(){
        String huffman = "Huffman";
        assertEquals(app.getAlgorithm(huffman).getClass(), HuffmanCompressor.class);
        String lzw = "LZW";
        assertEquals(app.getAlgorithm(lzw).getClass(), LZWCompressor.class);
        assertEquals(app.getAlgorithm("").getClass(), HuffmanCompressor.class);
        assertEquals("No available algorithm was indicated, so Huffman is picked by default\n", outContent.toString());
    }

    @Test(expected = FileNotFoundException.class)
    public void getInputFileNotFound() throws IOException {
        String path = "input.txt";
        app.getInput(path);
    }

    @Test
    public void getCompressionIsChosenTest() {
        assertTrue(app.compressionIsChosen(false, false));
        assertFalse(app.compressionIsChosen(false, true));
        assertTrue(app.compressionIsChosen(true, false));
    }

    /*@Test
    public void callTest() throws IOException {
        CommandLine commandLine = new CommandLine(app);
        commandLine.parseArgs("--input", "testToCompress.txt", "-out", "output.txt", "--compress");
        assertEquals("No available algorithm was indicated, so Huffman is picked by default\n", outContent.toString());
      new CommandLine(app).parseArgs("--input", "testToCompress.txt", "-out", "output.txt","-algo", "Huffman", "--compress");
        assertNotEquals("No available algorithm was indicated, so Huffman is picked by default\n", outContent.toString());
    }*/
}

