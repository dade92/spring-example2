package documents;

import java.io.File;
import java.io.IOException;

public interface DocumentService {
    void upload(File file);
    ImageLocation upload2(File file);
    String readTextFile(String filename) throws IOException;
}


