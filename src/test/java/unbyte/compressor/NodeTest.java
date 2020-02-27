package unbyte.compressor;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class NodeTest {

    private HuffmanCompressor.Node node;
    private HuffmanCompressor.Node node1 ;
    private HuffmanCompressor.Node node2 ;
    private HuffmanCompressor.Node node3 ;
    private HuffmanCompressor.Node node4 ;

    @Before
    public void init(){
        node = new HuffmanCompressor.Node((char)32, 1/4f, null, null);
        node1 = new HuffmanCompressor.Node((char)0, 1/4f, null, null);
        node2 = new HuffmanCompressor.Node((char)32, 1/3f, null, null);
        node3 = new HuffmanCompressor.Node((char)32, 1/4f, node, null);
        node4 = new HuffmanCompressor.Node((char)32, 1/4f, null, node);
    }

    @Test
    public void equalsTest(){
        assertNotEquals(node, node1);
        assertNotEquals(node, node2);
        assertNotEquals(node, node3);
        assertNotEquals(node, node4);
        assertEquals(node, node);
        assertNotEquals(node, null);
        assertNotEquals(node, "a");
    }

    @Test
    public void hashcodeTest() {
        assertEquals(node.hashCode(), Objects.hash((char)32, 1/4f, null, null));
    }

    @Test
    public void toStringTest() {
        assertEquals(node.toString(), "[  : 0.25]");
        assertEquals(node1.toString(), "[ : 0.25]");
        assertEquals(node3.toString(), "[  : 0.25\n(\nnull,\n[  : 0.25]\n)\n]");
    }

    @Test
    public void isLeafTest() {
        assertTrue(node.isLeaf());
        assertFalse(node3.isLeaf());
    }
}
