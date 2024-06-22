package webapp;

import adapters.s3.AwsDocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class DocumentController {

    private final AwsDocumentService documentService;

    public DocumentController(AwsDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public void saveDocument(@RequestParam("file") MultipartFile file) {
        documentService.upload(file);
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

}

record ReadResponse(
    String content
) {
}