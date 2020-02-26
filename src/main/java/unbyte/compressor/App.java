package unbyte.compressor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws IOException
	{

		try {
			InputStream inputStream = new FileInputStream("testToCompress.txt");
			InputStream stream = new BufferedInputStream(inputStream);
			
//			Compressor man = new HuffmanCompressor();
//			man.compress(stream,"result");
//			man.decompress(stream,"res");
			
			Compressor lzw = new LZWCompressor();
			lzw.compress(stream, "test");
			
			
			InputStream toDecompress = new FileInputStream("test.lzw");
			InputStream streamDecompress = new BufferedInputStream(toDecompress);
			
			lzw.decompress(streamDecompress, "uncompressed");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block FileNotFoundException
			e.printStackTrace();
		}
	     
	    
		//System.out.println(man.translate("A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED"));

		
	}
}
