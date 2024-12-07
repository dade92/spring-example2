package webapp.documents;

import data.Post;
import documents.DocumentService;
import documents.ImageLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@RestController
public class DocumentController {

    private final DocumentService documentService;
    private final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> saveDocument(@RequestParam("file") MultipartFile file) {
        try {
            ImageLocation imageLocation = documentService.upload(adaptFile(file));
            return ResponseEntity.ok(new UploadResponse(imageLocation.value()));
        } catch (Exception e) {
            logger.error("Error uploading post", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<PostsResponse> retrievePosts() {
        try {
            List<Post> posts = documentService.readPosts();
            return ResponseEntity.ok(new PostsResponse(
                adaptToJson(posts))
            );
        } catch (Exception e) {
            logger.error("Error retrieving posts", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private List<PostJson> adaptToJson(List<Post> posts) {
        return posts.stream().map(p -> new PostJson(p.name(), p.imageLocation().value())).toList();
    }

    private File adaptFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), convertedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }

    private record PostsResponse(List<PostJson> posts) {
    }

    private record UploadResponse(String imageLocation) {
    }

    private record PostJson(String name, String imageLocation) {}
}