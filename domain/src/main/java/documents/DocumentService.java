package documents;

import java.io.File;
import java.io.IOException;

public interface DocumentService {
    ImageLocation upload(File file);
    String readTextFile(String filename) throws IOException;
}


