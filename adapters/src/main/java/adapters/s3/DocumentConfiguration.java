package adapters.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import documents.DocumentService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class DocumentConfiguration {

    @Bean
    public DocumentService documentService(
        AwsProperties awsProperties
    ) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsProperties.accessKey, awsProperties.secretAccessKey);

        AmazonS3 amazonS3 = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .withRegion("eu-central-1")
            .build();

        return new AwsDocumentService(
            amazonS3,
            awsProperties.bucketName
        );
    }

}
