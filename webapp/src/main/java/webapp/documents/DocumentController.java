package webapp.documents;

import adapters.s3.AwsDocumentService;
import documents.DocumentService;
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
import java.util.Objects;

@RestController
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public void saveDocument(@RequestParam("file") MultipartFile file) {
        documentService.upload(adapt(file));
    }

    @GetMapping("/read")
    public ResponseEntity readDocument(@RequestParam String fileName) {
        try {
            String text = documentService.readTextFile(fileName);
            return ResponseEntity.ok(new ReadResponse(text));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    public File adapt(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), convertedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }

}

record ReadResponse(
    String content
) {
}