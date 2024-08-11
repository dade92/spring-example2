package adapters.documents;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import documents.ImageLocation;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.junit5.JUnit5Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AwsDocumentServiceTest {

    @RegisterExtension public final JUnit5Mockery context = new JUnit5Mockery();

    @Mock private AmazonS3 amazonS3;

    private AwsDocumentService awsDocumentService;

    private static final String BUCKET_NAME = "bucketName";

    @BeforeEach
    void setUp() {
        awsDocumentService = new AwsDocumentService(
            amazonS3, BUCKET_NAME, new ImageLocationBuilder()
        );
    }

    @Test
    void upload() {
        File file = new File("filename");

        context.checking(new Expectations(){{
            oneOf(amazonS3).putObject(with(any(PutObjectRequest.class)));
        }});

        ImageLocation actual = awsDocumentService.upload(file);

        assertEquals(new ImageLocation("http://bucketName/filename"), actual);
    }

}