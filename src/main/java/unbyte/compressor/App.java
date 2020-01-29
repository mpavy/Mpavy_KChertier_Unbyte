package unbyte.compressor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws IOException
	{

		
		try {
			InputStream inputStream = new FileInputStream("test.txt");
			InputStream stream = new BufferedInputStream(inputStream);
			
			Huffman man = new Huffman();
			man.compress(stream,"result");

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block FileNotFoundException
			e.printStackTrace();
		}
	     
	    
		//System.out.println(man.translate("A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED"));

		
	}
}
