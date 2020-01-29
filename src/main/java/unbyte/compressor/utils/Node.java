package unbyte.compressor.utils;

public class Node implements Comparable<Node>{
	private float value;
	private int key;
	private String prefix;
	
	public int getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	private Node left;
	private Node right;
	
	public Node(int key2, float value, Node right, Node left) {
		initialisation(key2, value, right, left);
	}
	
	public Node(float value, Node right, Node left) {
		initialisation(' ', value, right, left);
	}
	
	private void initialisation(int key2, float value, Node right, Node left) {
		this.prefix = "";
		this.key = key2;
		this.value = value;
		this.right = right;		
		this.left = left;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void updatePrefix(String pre, boolean value) {
		prefix =pre + (value?"1":"0");
		if(right!=null)	right.updatePrefix(prefix, false);
		if(left!=null) left.updatePrefix(prefix, true);
	}

	public float getValue() {
		return value;
	}
	public Node getLeft() {
		return left;
	}
	public Node getRight() {
		return right;
	}

	
	public void setValue(float value) {
		this.value = value;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	public boolean isLeaf() {
		return right == null && left == null ;
	}


	public int compareTo(Node node) {
		return new NodeComparator().compare(this,node);
	}
	
	public String toString() {
		return "\n["+((this.isLeaf())?key+":(leaf)":"")+prefix+"(\n	"+left+",\n	"+right+"\n)]";
	}
}
