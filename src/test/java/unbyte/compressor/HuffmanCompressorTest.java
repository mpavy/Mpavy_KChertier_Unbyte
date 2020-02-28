package unbyte.compressor;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.PriorityQueue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HuffmanCompressorTest {

    private HuffmanCompressor huffman;
    private byte[] stream ;
    private  int[] effectif;

    @Before
    public void init() throws IOException {
        String string = "0B/ ";
        InputStream input = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
        huffman = new HuffmanCompressor();
        stream = input.readAllBytes();
        effectif = huffman.getEffectif(stream);
    }

    @Test
    public void getEffectifTest(){
        int[] result = new int[256];
        result[48] = 1;
        result[66] = 1;
        result[47] = 1;
        result[32] = 1;
        assertArrayEquals(result, effectif);
        assertEquals(4, huffman.effectifTotal);
    }

    @Test
    public void getNodeQueueTest(){
        int[] effectif0 = new int[0];
        assertEquals(0,huffman.getNodeQueue(effectif0).size());
        PriorityQueue<HuffmanCompressor.Node> nodes = huffman.getNodeQueue(effectif);
        assertEquals(4, nodes.size());
        HuffmanCompressor.Node node = nodes.poll();
        assertEquals(new HuffmanCompressor.Node((char)32, 1/4f, null, null), node);
    }

    @Test
    public void getTreeTest(){
        PriorityQueue<HuffmanCompressor.Node> nodes = huffman.getNodeQueue(effectif);
        HuffmanCompressor.Node left1 = nodes.poll();
        HuffmanCompressor.Node right1 = nodes.poll();
        HuffmanCompressor.Node node1 = new HuffmanCompressor.Node((char)0, 1/2f, left1, right1);
        HuffmanCompressor.Node left2 = nodes.poll();
        HuffmanCompressor.Node right2 = nodes.poll();
        HuffmanCompressor.Node node2 = new HuffmanCompressor.Node((char)0, 1/2f, left2, right2);
        HuffmanCompressor.Node node3 = new HuffmanCompressor.Node((char)0, 1f, node1, node2);
        assertEquals(node3, huffman.getTree(huffman.getNodeQueue(effectif)));
    }

    @Test
    public void getCodageTest(){
        String[] codage = new String[256];
        codage[48] = "10";
        codage[66] = "01";
        codage[47] = "11";
        codage[32] = "00";
        assertEquals(codage, huffman.getCodage(huffman.getTree(huffman.getNodeQueue(effectif))));
    }

    @Test
    public void getCompressedTest(){
        HuffmanCompressor.Node root = huffman.getTree(huffman.getNodeQueue(effectif));
        assertEquals("10011100", huffman.getCompressed(huffman.getCodage(root), stream));
    }

    @Test
    public void translateStreamTest() {
        assertEquals(huffman.translateStream(stream), "1100001000010101111100000");
    }

    @Test
    public void getByteFromCodeTest() {
        assertEquals(13, huffman.getByteFromCode("1101"));
    }

    @Test
    public void getCodeFromByte(){
        assertEquals("1101", huffman.getCodeFromByte((byte)13));
    }

    @Test
    public void decodeFromMap() {
        HashMap<String, Character> decoder = new HashMap<>();
        decoder.put("aa", 'b');
        decoder.put("bb", 'c');
        assertEquals("b", huffman.decodeFromMap(decoder, "aabb"));
    }
}
