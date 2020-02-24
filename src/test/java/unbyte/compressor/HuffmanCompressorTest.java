package unbyte.compressor;

import org.junit.Before;
import org.junit.Test;

import java.util.PriorityQueue;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class HuffmanCompressorTest {

    private HuffmanCompressor huffman;
    private byte[] stream ;
    private  int[] effectif;

    @Before
    public void init() {
        huffman = new HuffmanCompressor();
        stream = new byte[]{0, 1, 0, 1, 1}; // TODO change those values
        effectif = huffman.getEffectif(stream);
    }

    @Test
    public void getEffectifTest(){
        int[] result = new int[256];
        result[0] = 2;
        result[1] = 3;
        assertArrayEquals(effectif, result);
        assertEquals(huffman.effectifTotal, 5);
    }

    @Test
    public void getNodeQueueTest(){
        int[] effectif0 = new int[0];
        assertEquals(huffman.getNodeQueue(effectif0).size(), 0);
        PriorityQueue<HuffmanCompressor.Node> nodes = huffman.getNodeQueue(effectif);
        assertEquals(nodes.size(), 2);
        HuffmanCompressor.Node node = nodes.poll();
        assertEquals(node, new HuffmanCompressor.Node((char) 0, 0.4f, null, null));
    }

    @Test
    public void getTreeTest(){
        PriorityQueue<HuffmanCompressor.Node> nodes = huffman.getNodeQueue(effectif);
        HuffmanCompressor.Node left = nodes.poll();
        HuffmanCompressor.Node right = nodes.poll();
        assertEquals(huffman.getTree(huffman.getNodeQueue(effectif)), new HuffmanCompressor.Node((char)0, 1f, left, right));
    }

    @Test
    public void getCodageTest(){
        String[] codage = new String[256];
        codage[0] = "0";
        codage[1] = "1";
        assertEquals(huffman.getCodage(huffman.getTree(huffman.getNodeQueue(effectif))), codage);
    }

    @Test
    public void getCompressedTest(){
        HuffmanCompressor.Node root = huffman.getTree(huffman.getNodeQueue(effectif));
        assertEquals(huffman.getCompressed(huffman.getCodage(root), stream), "01011");
    }
}
