package unbyte.compressor;

import java.util.BitSet;
import java.util.HashMap;
import java.util.PriorityQueue;

import unbyte.compressor.utils.Node;
import unbyte.compressor.utils.NodeComparator;

public class Huffman {
	HashMap<Character,Integer> charmap;
	int totalChar;
	PriorityQueue<Node> nodes;
	HashMap<Character,String> keys = new HashMap<>();
	Node root;
	
	public Huffman() {
		charmap = new HashMap<>();
		totalChar = 0;
	}
	
	public void integrateLine(String line){
		for(char c : line.toCharArray()) {
			charmap.put(c, ((charmap.containsKey(c))? charmap.get(c)+1:1));
			totalChar++;
		}
	}
	
	public void chartodist() {
		nodes = new PriorityQueue<Node>(totalChar, new NodeComparator());
		
		//We transform the symbols count into proportions, and put the created nodes into a priority queue
		for(char key : charmap.keySet()) {
			Node node = new Node(key, (float) ((charmap.get(key)+0f)/totalChar),null,null);
			nodes.add(node);
		}

		while(nodes.size()>1) {
			Node l = nodes.poll();
			Node r = nodes.poll();
			Node node = new Node(l.getValue()+r.getValue(),l,r);
			System.out.println(node);
			nodes.add(node);
		}
		
		//the last element is the root
		root = nodes.poll();
		
		//we generate the keymap from a tree course
		arrayGeneration(root);
		
	}

	private void arrayGeneration(Node node) {
		if(node!=null) {
			if(node.isLeaf()) {
				keys.put(node.getKey(), node.getPrefix());
			}else {
				arrayGeneration(node.getLeft());
				arrayGeneration(node.getRight());
			}
		}
	}

	public String translate(String string) {
		String res = "";
		for(char c : string.toCharArray()) {
			res+=keys.get(c);
		}
		return res;
	}
	
	
	public String generateTree() {
		return generateSubTree(root);
	}

	private String generateSubTree(Node node) {
		// 1 si intermediaire, 0 si leaf, 8 prochains bits : ascii du char key.
		String res = "";
		if(node.isLeaf()) {
			res += "0" + Integer.toBinaryString(node.getKey());
		}else {
			return "1"+generateSubTree(node.getLeft())+generateSubTree(node.getRight());
		}
		return res;
	}
	
	public Node parseTree(String seed) {
		Node node = null;
		if(seed.charAt(0)=='1') {
			//inter
		//	node = new Node(0,);
		}else {
			//leaf
			node = new Node(0,null,null);
			node.setPrefix("aaaaaaaaaaaaaaah");
		}
		
		return node;
	}
	

}
