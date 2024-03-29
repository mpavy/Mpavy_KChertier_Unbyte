package unbyte.compressor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;

import unbyte.compressor.Compressor;

public class HuffmanCompressor implements Compressor{

	private HashMap<Character, String> map;
	
	@Override
	public void decompress(InputStream stream, String filename) throws IOException {
		// Lire le stream
		byte[] input = stream.readAllBytes();
		//on suppose qu'on a une chaine de 0 et de 1.
		String translated = translateStream(input);
		
		// En extraire une map 
		
		//on suppose qu'on a une map avec les codes.		
		HashMap<String, Character> decoder = new HashMap<>();
		for(char c : map.keySet()) {
			decoder.put(map.get(c),c);
		}
		
		//on décode le message
		String decoded = decodeFromMap(decoder,translated);
		System.out.println(decoded);
		
	}

	public String translateStream(byte[] input) {
		StringBuilder result = new StringBuilder();
		for(byte b : input) {
			result.append(Integer.toBinaryString(b));
		}
		return result.toString();
	}

	public String decodeFromMap(HashMap<String, Character> map, String str) {
		String buffer = "";
		StringBuilder result = new StringBuilder();
		while(str.length()>0) {
			//System.out.println(buffer + " --- " + result);
			if(map.containsKey(buffer)) {
				result.append(map.get(buffer));
				buffer="";
			}else {
				buffer+=str.charAt(0);
				{
					str=str.substring(1);
				}
			}
		}
		return result.toString();
	}

	int effectifTotal;
	
	@Override
	public void compress(InputStream stream, String outputFileName) throws IOException {
		byte[] stb = stream.readAllBytes();
		//Lire le stream, en extraire l'effectif de chaque caractères.
		int[] effectif = getEffectif(stb);

		//transformer cet effectif en probabilité, construire un Noeud et le ranger dans une file de priorité
		PriorityQueue<Node> nodes = getNodeQueue(effectif);
		//Construire un arbre avec la file : en prendre deux dans la file, les combiner en tant que fils d'un nouveau noeud (probabilité sommées)
		//et remettre celui-ci dans la file.
		//le noeud restant dans la liste est la racine.
		Node root = getTree(nodes);
		//parcourir l'arbre pour déterminer le codage de chaque caractères
		String[] codage = getCodage(root);
		
		//enfin, parcourir de nouveau le stream d'entrée, pour convertir chaque caractère.
		String result =
				getCompressed(codage, stb);
		System.out.println(result);
		//résultat sous forme de chaîne

		//export de l'arbre sous forme d'une suite de charactère suivi de son codage
	//	String treetable = getTreeTable(codage);
				
		//export de l'arbre sous forme : 1 pour un noeud intermédiaire, 0 pour une feuille, suivi du charactère qu'il encode
	//	String treenodes = "";

	}

	public byte getByteFromCode(String code) {
		byte b = 0;
		for(char c : code.toCharArray()) {
			b*=2;
			if(c=='1') {
				b+=1;
			}
		}
		return b;
	}
	
	public String getCodeFromByte(byte b) {
		return Integer.toBinaryString(b);
	}
	
	public String getCompressed(String[] codage, byte[] stb) {
		StringBuilder result = new StringBuilder();
		for(byte b : stb) {
			result.append(codage[b]);
		}
		return result.toString();
	}

	public String[] getCodage(Node root) {
		HashMap<Character,String> map = new HashMap<>();
		map.putAll(getCodageRecu("1",root.left));
		map.putAll(getCodageRecu("0",root.right));
		String[] codage = new String[256];
		for(char key : map.keySet()) {
			codage[key]=map.get(key);
		}
		
		//TODO supprimer cette ligne, une fois l'implémentation du décodeur terminée.
		this.map = map;
		return codage;
	}

	private HashMap<Character,String> getCodageRecu(String code, Node node) {
		HashMap<Character,String> map = new HashMap<>();
		if(node.isLeaf()) {
			map.put(node.character, code);
		}else {
			map.putAll(getCodageRecu(code+"1", node.left));
			map.putAll(getCodageRecu(code+"0", node.right));
		}
		return map;
	}

	public Node getTree(PriorityQueue<Node> nodes) {
		while(nodes.size()>1) {
			Node left = nodes.poll();
			Node right = nodes.poll();
			Node node = new Node(left, right);
			node.probability = left.probability+ (right != null ? right.probability : 0);
			nodes.add(node);
		}
		
		return nodes.poll();
	}

	public int[] getEffectif(byte[] stream) {
		int[] effectif = new int[256];
		for(byte b : stream){
			effectif[b]++;
			effectifTotal++;
		}
		return effectif;
	}

	public PriorityQueue<Node> getNodeQueue(int[] effectif) {
		PriorityQueue<Node> nodes = new PriorityQueue<HuffmanCompressor.Node>(effectifTotal , new NodeComparator());

		for(int i=0;i<effectif.length;i++) {
			if((effectif[i]!=0)) {
				Node node = new Node((char) i, (float)effectif[i] / effectifTotal, null, null);
				nodes.add(node);
			}
		}

		return nodes;
	}

	public static class Node {
		private char character;
		private float probability;
		private Node left;
		private Node right;

		public Node(char character, float probability,Node right, Node left) {
			this.character = character;
			this.probability = probability;
			this.right = right; 
			this.left = left;
		}
		public Node(Node right, Node left) {
			this.right = right; 
			this.left = left;
		}


		public boolean isLeaf() {
			return (right == null && left == null);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Node node = (Node) o;
			return character == node.character &&
					Float.compare(node.probability, probability) == 0 &&
					Objects.equals(left, node.left) &&
					Objects.equals(right, node.right);
		}

		@Override
		public int hashCode() {
			return Objects.hash(character, probability, left, right);
		}

		@Override
		public String toString() {
			return "["+((character!=0)?character:"")+" : "+probability
					+(!isLeaf()? "\n(\n"+left+",\n"+right+"\n)\n":"")
							+ "]" ;
		}
	}
	public static class NodeComparator implements Comparator<HuffmanCompressor.Node>, Serializable {
		public int compare(HuffmanCompressor.Node a, HuffmanCompressor.Node b) {
			return Float.compare(a.probability, b.probability);
		}
	}

}
