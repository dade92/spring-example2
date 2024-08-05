package documents;

import data.Post;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface DocumentService {
    ImageLocation upload(File file);
    String readTextFile(String filename) throws IOException;
    List<Post> readPosts();
}


