package unbyte.compressor;

import java.io.IOException;
import java.io.InputStream;

public interface Compressor {
	void decompress(InputStream stream, String string) throws IOException;
	void compress(InputStream stream, String outputFileName) throws IOException;
}
