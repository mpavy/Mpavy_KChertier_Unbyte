package unbyte.compressor;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args )
	{





		//Tests temporaires
		Huffman man = new Huffman();
		man.integrateLine("A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED");
		System.out.println(man.charmap);
		man.chartodist();
		System.out.println(man.keys);
		//System.out.println(man.translate("A_DEAD_DAD_CEDED_A_BAD_BABE_A_BEADED_ABACA_BED"));

		System.out.println(man.generateTree());
		
	}
}
