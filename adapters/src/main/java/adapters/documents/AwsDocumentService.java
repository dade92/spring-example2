package adapters.documents;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import data.Post;
import documents.DocumentService;
import documents.ImageLocation;

import java.io.File;
import java.util.List;

public class AwsDocumentService implements DocumentService {

    private final AmazonS3 amazonS3;
    private final String bucketName;
    private final ImageLocationBuilder imageLocationBuilder;

    public AwsDocumentService(
        AmazonS3 amazonS3,
        String bucketName,
        ImageLocationBuilder imageLocationBuilder
    ) {
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
    public List<Post> readPosts() {
        ObjectListing objectListing = amazonS3.listObjects(bucketName);

        return objectListing.getObjectSummaries()
            .stream()
            .map(os -> new Post(os.getKey(), imageLocationBuilder.build(bucketName, os.getKey()))).toList();
    }

}
