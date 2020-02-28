package unbyte.compressor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class LZWCompressor implements Compressor{
	int NB_BIT = 12;

	@Override
	public void decompress(InputStream stream, String outputFileName) throws IOException {
		//initialiser le dictionnaire avec les chaines de longueur 1.
		HashMap<String,String> dictionnaire = getDecompressDictionnary();
		String raw = "";
		byte[] bytes = stream.readAllBytes();
		//Transformer l'entrée en chaîne de 0 et de 1
		for(byte value : bytes) {
			if(value<0) {
				raw += getNegativeNBit(value,8);
			}else {
				raw+= getNBitRepresentation(value, 8);
			}
		}
		//décompresser en utilisant le dictionnaire et l'algo lzw
		String result =  translate(dictionnaire,raw);
		//ecrire le résultat dans un fichier
		printUncompressedToFile(result,outputFileName);
	}

	public HashMap<String,String> getDecompressDictionnary(){
		HashMap<String,String> dictionnaire = new HashMap<>();
		for(int i = 1 ; i<256; i++) {
			dictionnaire.put(getNBitRepresentation(i,NB_BIT), Character.toString((char)i));
		}
		return dictionnaire;
	}

	public HashMap<String,String> getCompressDictionnary(){
		//initialiser le dictionnaire avec les chaines de longueur 1.
		HashMap<String,String> dictionnaire = new HashMap<>();
		for(int i = 1 ; i<256; i++) {
			dictionnaire.put(Character.toString((char)i), getNBitRepresentation(i,NB_BIT));
		}
		return dictionnaire;
	}


	public void printUncompressedToFile(String result, String outputFileName) throws IOException {
		File file = new File(outputFileName + ".lzw");
		try (FileOutputStream out = new FileOutputStream(file)) {

			for (char b : result.toCharArray()) {
				out.write(b);
			}
		}
	}





	public String getNegativeNBit(byte value, int i) {
		String result = Integer.toBinaryString(value);

		return result.substring(result.length()-i);
	}



	public String translate(HashMap<String, String> dictionnaire, String input) {
		String result = "";
		String bitBuffer = "";

		String last = "";
		int nextEntry = dictionnaire.size();
		for(char c : input.toCharArray()) {
			if(bitBuffer.length()<NB_BIT) {
				bitBuffer+=c;
			}else {

				// 	on cherche le code
				//	on ajoute au dico : "décompressé avant + le premier caractère de ce qui est décompréssé maintenant"
				//			
				if(dictionnaire.containsKey(bitBuffer)) {
					if(last.length()>0) {
						dictionnaire.put(getNBitRepresentation(nextEntry, NB_BIT),last + dictionnaire.get(bitBuffer).charAt(0));
						nextEntry++;
					}

					last =  dictionnaire.get(bitBuffer);
				}

				result+= last;
				bitBuffer = ""+c;
			}
		}

		//on gère le reste du stream (possiblement incomplet)
		//completer avec des 0 devant pour avoir NB_BIT, et rechercher.

		while(bitBuffer.length()<NB_BIT) {
			bitBuffer= "0"+bitBuffer;
		}
		if(dictionnaire.containsKey(bitBuffer)) {
			result +=  dictionnaire.get(bitBuffer);
		}
		return result;
	}



	@Override
	public void compress(InputStream stream, String outputFileName) throws IOException {

		HashMap<String,String> dictionnaire = getCompressDictionnary();
		byte[] input = stream.readAllBytes();
		String result = transform(dictionnaire, input);
		printCompressedToFile(result,outputFileName);
	}



	private void printCompressedToFile(String result, String outputFileName) throws IOException {
		byte buffer = 0;
		File file = new File(outputFileName+".lzw");
		try (FileOutputStream out = new FileOutputStream(file)) {
			int i = 0;
			for (char c : result.toCharArray()) {
				if (i == 8) {
					out.write(buffer);
					buffer = 0;
					i = 0;
				}
				if (c == '0') {
					buffer = (byte) (buffer << 1);
				} else {
					buffer = (byte) ((buffer << 1) + 1);
				}

				i++;
			}
		}
	}

	/*
	 * Methode de compression 
	 */
	public String transform(HashMap<String, String> dictionnaire, byte[] input) {
		String result = "";
		String buffer = "";
		int nextEntry = dictionnaire.size();
		//		Find the longest string W in the dictionary that matches the current input.
		//		Emit the dictionary index for W to output and remove W from the input.
		//		Add W followed by the next symbol in the input to the dictionary.
		for(byte b : input){
			buffer+=(char)b;
			if(dictionnaire.containsKey(buffer)) {
				//continuer l'accumulation
			}else {
				dictionnaire.put(buffer, getNBitRepresentation(nextEntry++,NB_BIT));
				result+=dictionnaire.get(buffer.substring(0,buffer.length()-1));
				buffer=buffer.substring(buffer.length()-1);

			}

		}
		//traiter le reste du buffer
		if(dictionnaire.containsKey(buffer)) {
			result+=dictionnaire.get(buffer.substring(0,buffer.length()));
		}
		return result;
	}


	/*
	 * Cette fonction permet de transformer un entier en sa représentation binaire, préfixée par des zéros pour obtenir une longueur précise.
	 * 
	 * @param value, nbBit
	 * value : entier à transformer
	 * nbBit : taile finale à obtenir
	 */
	public static String getNBitRepresentation(int value, int nbBit) {

		String result = Integer.toBinaryString(value);

		while(result.length()<nbBit) {
			result = "0"+result;
		}
		return result;
	}

}
