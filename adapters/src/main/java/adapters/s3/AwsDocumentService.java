package adapters.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class AwsDocumentService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public AwsDocumentService(AmazonS3 amazonS3, String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public void upload(MultipartFile file) {
        File localFile = convertMultipartFileToFile(file);

        amazonS3.putObject(new PutObjectRequest(bucketName, file.getOriginalFilename(), localFile));
    }

    public String readTextFile(String filename) throws IOException {
        S3Object amazonS3Object = amazonS3.getObject(new GetObjectRequest(bucketName, filename));
        S3ObjectInputStream objectContent = amazonS3Object.getObjectContent();
        int content;
        StringBuilder output = new StringBuilder();
        while ((content = objectContent.read()) != -1) {
            output.append((char) content);
        }
        return output.toString();
    }

    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Files.copy(file.getInputStream(), convertedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return convertedFile;
    }

}
