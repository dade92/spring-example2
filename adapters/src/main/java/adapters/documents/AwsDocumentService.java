package adapters.documents;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import documents.DocumentService;
import documents.ImageLocation;

import java.io.File;
import java.io.IOException;

public class AwsDocumentService implements DocumentService {

    private final String STATIC_BUCKET_NAME = "https://s3-bucket-example-1234599.s3.eu-central-1.amazonaws.com/";

    private final AmazonS3 amazonS3;
    private final String bucketName;

    public AwsDocumentService(AmazonS3 amazonS3, String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    @Override
    public ImageLocation upload(File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, file.getName(), file));
        return new ImageLocation(STATIC_BUCKET_NAME + file.getName());
    }

    @Override
    public String readTextFile(String filename) throws IOException {
        S3ObjectInputStream objectContent = amazonS3
            .getObject(new GetObjectRequest(bucketName, filename))
            .getObjectContent();
        int content;
        StringBuilder output = new StringBuilder();
        while ((content = objectContent.read()) != -1) {
            output.append((char) content);
        }
        return output.toString();
    }

}
