package unbyte.compressor;

import picocli.CommandLine;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * Hello world!
 *
 */
public class App implements Callable<Integer>
{

	@CommandLine.Option(names = {"-i", "--input"}, description = "Enter the input file path")
    private
    String path;

	@CommandLine.Option(names = "-out", description = "Enter the output file name")
    String outputFileName;

	@CommandLine.Option(names = "-algo", description = "The algorithm you want to use : Huffman (default) or LZW")
	private
	String algorithm;

	@CommandLine.Option(names = "--compress", description ="Compress the file (default)")
	private
	boolean compression;

	@CommandLine.Option(names = "--decompress", description ="Decompress the file")
	private
	boolean decompression;

	@CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, description = "Display a help message")
	private boolean helpRequested = false;

	public static void main( String[] args ) throws IOException
	{
		int exitCode = new CommandLine(new App()).execute(args);
		System.exit(exitCode);
	}

	@Override
	public Integer call() throws IOException {
		Compressor man;
		if (algorithm == null)
		{
			System.out.println("No available algorithm was indicated, so Huffman is picked by default");
			man = new HuffmanCompressor();
		} else {
			man = getAlgorithm(algorithm);
		}

		InputStream stream;
		if (path == null)
		{
			System.out.println("Please provide an input path");
			return 1;
		} else {
			stream = getInput(path);
		}

		if (outputFileName == null){
			System.out.println("No output file name was provided. Default is 'result'");
			outputFileName = "result";
		}

        if (compressionIsChosen(compression, decompression))
		{
			man.compress(stream, outputFileName);
			return 0;
		}
		man.decompress(stream,outputFileName);
        return 0;
	}

    public Compressor getAlgorithm(String algo){
	    if (algo.equals("Huffman"))
	        return new HuffmanCompressor();
	    if (algo.equals("LZW"))
	        return new LZWCompressor();
        System.out.println("No available algorithm was indicated, so Huffman is picked by default");
        return new HuffmanCompressor();
        }

    public InputStream getInput(String path) throws FileNotFoundException{
        InputStream inputStream = new FileInputStream(path);
        return new BufferedInputStream(inputStream);
    }

    public boolean compressionIsChosen(boolean compression, boolean decompression){
		if (compression == decompression)
			return true;
		return compression;
	}

}
