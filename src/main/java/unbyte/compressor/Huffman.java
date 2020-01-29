package unbyte.compressor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.BitSet;
import java.util.PriorityQueue;

import unbyte.compressor.utils.Node;
import unbyte.compressor.utils.NodeComparator;

public class Huffman implements Compressor{
	int[] charmap;
	int totalChar;
	PriorityQueue<Node> nodes;
	//HashMap<Character,String> keys = new HashMap<>();
	String[] keys;
	Node root;
	
	public Huffman() {
		charmap = new int[256];
		Arrays.fill(charmap, 0);
		totalChar = 0;
		keys = new String[256];
	}
	
	@Override
	public void compress(InputStream stream, String filename) throws IOException {
		//TODO 
		//parse the tokens into an array with quantities
		integrateStream(stream);
		//transforms the array into a tree of nodes
		char2Dist();
		//creates the prefix array, to avoid searching the tree for each byte
		arrayGeneration(root);
		System.out.println(root);
		
		//generates the tree seed to prepend to the compressed file
		BitSet tree = generateTree();
		
		print2File(tree, filename);
		//translate the input stream into it's compressed form
	//	stream.
		
	}

	
	
	
	
	private void print2File(BitSet bitset, String filename) throws IOException {
		File file = new File(filename+".ubh");
		if(file.createNewFile()) {
			FileOutputStream out = new FileOutputStream(file);
			out.write(bitset.toByteArray());
		}else {
			//TODO erreur fichier existe déjà
		}
		
	}

	public void integrateStream(InputStream inputStream) throws IOException{
		int c ;
		while((c = inputStream.read())!=-1) {
			charmap[c]=charmap[c]+1;
			totalChar++;
		}
	}
	
	private void char2Dist() {
		nodes = new PriorityQueue<Node>(totalChar, new NodeComparator());
		
		//We transform the symbol counts into proportions, and put the created nodes into a priority queue
		for(int key = 0; key < charmap.length; key++) {
			if(charmap[key]>0) {
				Node node = new Node(key, (float) ((charmap[key]+0f)/totalChar),null,null);
				nodes.add(node);
			}
		}
		//we take the first two and make a new node with both as children.
		while(nodes.size()>1) {
			Node l = nodes.poll();
			Node r = nodes.poll();
			Node node = new Node(l.getValue()+r.getValue(),l,r);
			nodes.add(node);
		}
		
		//the last element in the queue is the root of the tree
		root = nodes.poll();
		root.getLeft().updatePrefix("",true);
		root.getRight().updatePrefix("", false);
		System.out.println(root);
	}

	private void arrayGeneration(Node node) {
		if(node!=null) {
			if(node.isLeaf()) {
				keys[node.getKey()] = node.getPrefix();
			}else {
				arrayGeneration(node.getLeft());
				arrayGeneration(node.getRight());
			}
		}
	}

	private String translate(InputStream stream) throws IOException {
		String res = "";
		int c = -1;
		while((c = stream.read())!=-1) {
			res+=keys[c];
		}
		return res;
	}
	
	
	private BitSet generateTree() {
		//TODO REDO IT'S VERRRRRRRYYY UGLY :(-'(
		//AND IT DOESN'T QUITE WORKS (trailing 0 are removed ?)
		String tree = generateSubTree(root);
		BitSet set = new BitSet();
		for(int i = 0; i<tree.length(); i++) {
			set.set(i,tree.charAt(i)=='1');
		}
		return set; 
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
	
//	private Node parseTree(BitSet bitset) {
//		Node node = null;
//		
//		if(seed.charAt(0)=='1') {
//			//inter
//		//	node = new Node(0,);
//		}else {
//			//leaf
//			node = new Node(0,null,null);
//			//TODO
//			node.setPrefix("a");
//		}
//		
//		return node;
//	}


	@Override
	public void decompress(InputStream stream) {
		// TODO Auto-generated method stub
		
	}
	

}
