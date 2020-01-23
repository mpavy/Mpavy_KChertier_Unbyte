package unbyte.compressor.utils;

public class Node implements Comparable<Node>{
	private float value;
	private char key;
	private String prefix;
	
	public char getKey() {
		return key;
	}

	public void setKey(char key) {
		this.key = key;
	}

	private Node left;
	private Node right;
	
	public Node(char key, float value, Node right, Node left) {
		initialisation(key, value, right, left);
	}
	
	public Node(float value, Node right, Node left) {
		initialisation(' ', value, right, left);
	}
	
	private void initialisation(char key, float value, Node right, Node left) {
		this.prefix = "";
		this.key = key;
		this.value = value;
		this.right = right;
		if(right!=null)	this.right.setPrefix(right.getPrefix()+"0"); // 0 + prefix
		this.left = left;
		if(left!=null) this.left.setPrefix(left.getPrefix()+"1");
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
		if(right!=null)	this.right.setPrefix(prefix+"0");
		if(left!=null) this.left.setPrefix(prefix+"1");
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
		return "["+((this.isLeaf())?key+":":"")+value+"-"+prefix+"-"+
				"(\n	"+left+",\n	"+right+")]";
	}
}
