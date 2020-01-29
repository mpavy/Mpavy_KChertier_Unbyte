package unbyte.compressor;

import java.io.IOException;
import java.io.InputStream;

public interface Compressor {
	public void decompress(InputStream stream);
	void compress(InputStream stream, String outputFileName) throws IOException;
}
