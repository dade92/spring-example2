package adapters.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class AwsConfiguration {

    @Bean
    public AmazonS3 amazonS3(AwsProperties awsProperties) {
        AWSCredentials awsCredentials = retrieveAWSCredentials(awsProperties);

        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
            .withRegion("eu-central-1")
            .build();
    }

    private AWSCredentials retrieveAWSCredentials(AwsProperties awsProperties) {
        if (Objects.equals(awsProperties.sessionToken, "${SESSION_TOKEN}")) {
            return new BasicAWSCredentials(
                awsProperties.accessKey,
                awsProperties.secretAccessKey
            );
        } else {
            return new BasicSessionCredentials(
                awsProperties.accessKey,
                awsProperties.secretAccessKey,
                awsProperties.sessionToken
            );
        }
    }

}
