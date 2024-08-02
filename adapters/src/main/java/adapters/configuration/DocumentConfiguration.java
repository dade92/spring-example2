package adapters.configuration;

import adapters.documents.AwsDocumentService;
import adapters.documents.ImageLocationBuilder;
import com.amazonaws.services.s3.AmazonS3;
import documents.DocumentService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class DocumentConfiguration {

    @Bean
    public DocumentService documentService(
        AmazonS3 amazonS3,
        AwsProperties awsProperties
    ) {
        return new AwsDocumentService(
            amazonS3,
            awsProperties.bucketName,
            new ImageLocationBuilder()
        );
    }
}
