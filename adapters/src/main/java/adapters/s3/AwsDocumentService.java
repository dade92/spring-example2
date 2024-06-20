package adapters.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

    public File read(String key) {
        S3Object amazonS3Object = amazonS3.getObject(new GetObjectRequest(bucketName, key));
        final InputStreamReader streamReader = new InputStreamReader(amazonS3Object.getObjectContent(), StandardCharsets.UTF_8);
        final BufferedReader reader = new BufferedReader(streamReader);
        return null;
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
