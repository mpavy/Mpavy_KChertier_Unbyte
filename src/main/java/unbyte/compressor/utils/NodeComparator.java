package unbyte.compressor.utils;
import java.util.Comparator;

public class NodeComparator implements Comparator<Node>{
	public int compare(Node a, Node b) {
		return ((Float)a.getValue()).compareTo(b.getValue());
	}
}
