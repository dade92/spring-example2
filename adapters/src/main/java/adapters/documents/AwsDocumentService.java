package adapters.documents;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import documents.DocumentService;
import documents.ImageLocation;

import java.io.File;
import java.io.IOException;

public class AwsDocumentService implements DocumentService {

    private final AmazonS3 amazonS3;
    private final String bucketName;
    private final ImageLocationBuilder imageLocationBuilder;

    public AwsDocumentService(AmazonS3 amazonS3, String bucketName, ImageLocationBuilder imageLocationBuilder) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
        this.imageLocationBuilder = imageLocationBuilder;
    }

    @Override
    public ImageLocation upload(File file) {
        amazonS3.putObject(new PutObjectRequest(bucketName, file.getName(), file));
        return imageLocationBuilder.build(bucketName, file.getName());
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

    @Override
    public void readFiles() {
        ObjectListing objectListing = amazonS3.listObjects(bucketName);
        for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            String key = objectSummary.getKey();
            System.out.println(key);
        }
    }

}
