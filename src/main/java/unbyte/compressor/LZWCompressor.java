package unbyte.compressor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class LZWCompressor implements Compressor{
	int NB_BIT = 9;

	@Override
	public void decompress(InputStream stream, String outputFileName) throws IOException {
		//initialiser le dictionnaire avec les chaines de longueur 1.
		HashMap<String,String> dictionnaire = new HashMap<>();
		for(int i = 0 ; i<256; i++) {
			dictionnaire.put(getNBitRepresentation(i,NB_BIT), Character.toString((char)i));
		}
		String raw = "";
		byte[] bytes = stream.readAllBytes();
		
		//TODO changer pour vraiment lire l'entrée.
		//TODO correct this :
		for(byte value : bytes) {
			raw += Integer.toBinaryString(value);
		}
		System.out.println("rawed : "+raw);
		
		
		//on suppose qu'on a le stream sous forme de chaine de 0 et de 1.
		 raw = "001010100001001111001000010001000101001001111001010010001001110001001111001010100100000000100000010100000100100001001100000011100000101100000111";
		System.out.println("r___d : "+raw);
		 String result =  translate(dictionnaire,raw);
		System.out.println("uncompressed : "+result);
		//TODO print to file
	}



	private String translate(HashMap<String, String> dictionnaire, String input) {
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

				if(last.length()>0) {
					dictionnaire.put(getNBitRepresentation(nextEntry, NB_BIT),last + dictionnaire.get(bitBuffer).charAt(0));
					nextEntry++;
				}
				if(dictionnaire.containsKey(bitBuffer)) {
					last =  dictionnaire.get(bitBuffer);
				}
				result+= last;
				bitBuffer = ""+c;
			}
		}
		//on gère le reste du stream (possiblement incomplet)
		if(dictionnaire.containsKey(bitBuffer)) {
			result +=  dictionnaire.get(bitBuffer);
		}else {
			//completer avec des 0 devant pour avoir NB_BIT, et rechercher.
			//TODO
		}
		return result;
	}



	@Override
	public void compress(InputStream stream, String outputFileName) throws IOException {
		//initialiser le dictionnaire avec les chaines de longueur 1.

		HashMap<String,String> dictionnaire = new HashMap<>();
		for(int i = 0 ; i<256; i++) {
			dictionnaire.put(Character.toString((char)i), getNBitRepresentation(i,NB_BIT));
		}

		byte[] input = stream.readAllBytes();

		String result = transform(dictionnaire, input);
		
		printCompressedToFile(result,outputFileName);
		System.out.println("Compressed form : "+result);
	}



	private void printCompressedToFile(String result, String outputFileName) throws IOException {
		byte buffer = 0;
		File file = new File(outputFileName+".lzw");
			FileOutputStream out = new FileOutputStream(file);
			int i = 0;
			for(char c : result.toCharArray()) {
				if(c == '0') {
					buffer =  (byte) (buffer << 1);
				}else {
					buffer =  (byte) (buffer +1);
				}
				
				if(i==7) {
					out.write(buffer);
					buffer = 0;
					i=0;
				}
				i++;
			}
			out.close();
			
	}



	private String transform(HashMap<String, String> dictionnaire, byte[] input) {
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



	private String getNBitRepresentation(int value, int nbBit) {
		String result = Integer.toBinaryString(value);
		while(result.length()<nbBit) {
			result = "0"+result;
		}
		return result;
	}

}
