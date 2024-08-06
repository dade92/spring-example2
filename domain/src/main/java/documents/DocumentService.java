package documents;

import data.Post;

import java.io.File;
import java.util.List;

public interface DocumentService {
    ImageLocation upload(File file);

    List<Post> readPosts();
}


