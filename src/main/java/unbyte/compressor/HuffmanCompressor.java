package unbyte.compressor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

import unbyte.compressor.Compressor;
import unbyte.compressor.utils.Node;
import unbyte.compressor.utils.NodeComparator;

public class HuffmanCompressor implements Compressor{

	private HashMap<Character, String> map;
	
	@Override
	public void decompress(InputStream stream, String filename) throws IOException {
		// TODO Auto-generated method stub
		// Lire le stream
		byte[] stb = stream.readAllBytes();
		//on suppose qu'on a une chaine de 0 et de 1.
		
		
		// En extraire une map 
		
		//on suppose qu'on a le string avec les données à décompresser, et une map avec les codes.
		String str = "1001001101000010010000111101100011000011001111110000111111011111100110011111110100011000011011111011101001111111000\n";
		HashMap<String, Character> decoder = new HashMap<>();
		
		for(char c : map.keySet()) {
			decoder.put(map.get(c),c);
		}
		
		String decoded = decodeFromMap(decoder,str);
		System.out.println(decoded);		
		
	}

	private String decodeFromMap(HashMap<String, Character> map, String str) {
		String buffer = "";
		String result = "";
		while(str.length()>0) {
			//System.out.println(buffer + " --- " + result);
			if(map.containsKey(buffer)) {
				result+=map.get(buffer);
				buffer="";
			}else {
				buffer+=str.charAt(0);
				{
					str=str.substring(1);
				}
			}
		}
	//	System.out.println(result);
		// TODO Auto-generated method stub
		return result;
	}

	int effectifTotal;
	
	@Override
	public void compress(InputStream stream, String outputFileName) throws IOException {
		byte[] stb = stream.readAllBytes();
		//Lire le stream, en extraire l'effectif de chaque caractères.
		int effectif[] = getEffectif(stb);

		//transformer cet effectif en probabilité, construire un Noeud et le ranger dans une file de priorité
		PriorityQueue<Node> nodes = getNodeQueue(effectif);
		System.out.println(nodes);
		//Construire un arbre avec la file : en prendre deux dans la file, les combiner en tant que fils d'un nouveau noeud (probabilité sommées) 
		//et remettre celui-ci dans la file.
		//le noeud restant dans la liste est la racine.
		Node root = getTree(nodes);
		//System.out.println(root);
		//parcourir l'arbre pour déterminer le codage de chaque caractères
		String[] codage = getCodage(root);
		
		//enfin, parcourir de nouveau le stream d'entrée, pour convertir chaque caractère.
		String result = getCompressed(codage, stb);
		
		//résultat sous forme de chaîne
		System.out.println(result);
		
		//export de l'arbre sous forme d'une suite de charactère suivi de son codage
	//	String treetable = getTreeTable(codage);
				
		//export de l'arbre sous forme : 1 pour un noeud intermédiaire, 0 pour une feuille, suivi du charactère qu'il encode
	//	String treenodes = "";
		
	
		
	}

	private byte[] getTreeTable(String[] codage) {
		int nb = 1;
		byte[] array = new byte[513];
		int i=0;
		while(i< codage.length) {
			if(codage[i]!=null) {
				array[nb]=(byte) i;
				array[nb+1]=getByteFromCode(codage[i]);
				nb+=2;
			}
			i++;
		}
		array[0]=(byte) i;
		return array;
	}

	private byte getByteFromCode(String code) {
		byte b = 0;
		for(char c : code.toCharArray()) {
			b*=2;
			if(c=='1') {
				b+=1;
			}
		}
		return b;
	}
	
	private String getCodeFromByte(byte b) {
		return Integer.toBinaryString(b);
	}
	
	private String getCompressed(String[] codage, byte[] stb) {
		String result = "";
		for(byte b : stb) {
			result+=codage[b];
		}
		return result;
	}

	private String[] getCodage(Node root) {
		// TODO Auto-generated method stub
		HashMap<Character,String> map = new HashMap<>();
		map.putAll(getCodageRecu("1",root.left));
		map.putAll(getCodageRecu("0",root.right));
		String[] codage = new String[256];
		for(char key : map.keySet()) {
			codage[key]=map.get(key);
		}
		
		//TODO supprimer cette ligne, une fois l'implémentation du décodeur terminée.
		this.map = map;
	//	System.out.println(map);
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

	private Node getTree(PriorityQueue<Node> nodes) {
		while(nodes.size()>1) {
			Node left = nodes.poll();
			Node right = nodes.poll();
			Node node = new Node(left, right);
			node.probability = left.probability+right.probability;
			nodes.add(node); 
		}
		
		return nodes.poll();
	}

	private int[] getEffectif(byte[] stream) {
		int[] effectif = new int[256];
		for(byte b : stream){
			effectif[b]++;
			effectifTotal++;
		}
		return effectif;
	}

	private PriorityQueue<Node> getNodeQueue(int[] effectif) {
		PriorityQueue<Node> nodes = new PriorityQueue<HuffmanCompressor.Node>(effectifTotal ,new NodeComparator());

		for(int i=0;i<effectif.length;i++) {
			if((effectif[i]!=0)) {
				Node node = new Node((char)i,(effectif[i]+0f)/effectifTotal,null,null);
				nodes.add(node);
			}
		}

		return nodes;
	}

	private class Node {
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

		public String toString() {
			return "["+((character!=0)?character:"")+" : "+probability
					+(!isLeaf()? "\n(\n"+left+",\n"+right+"\n)\n":"")
							+ "]" ;
		}
	}
	private class NodeComparator implements Comparator<Node>{
		public int compare(Node a, Node b) {
			return ((Float)a.probability).compareTo(b.probability);
		}
	}

}