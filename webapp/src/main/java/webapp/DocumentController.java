package webapp;

import adapters.s3.AwsDocumentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

}
