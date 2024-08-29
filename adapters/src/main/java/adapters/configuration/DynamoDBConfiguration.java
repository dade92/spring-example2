package adapters.configuration;

import com.amazonaws.auth.AWSCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

@Configuration
public class DynamoDBConfiguration {

    @Bean
    public DynamoDbClient dynamoDbClient(AWSCredentials awsCredentials) {
        DynamoDbClientBuilder builder = DynamoDbClient.builder();
        builder.region(Region.EU_CENTRAL_1);
        builder.credentialsProvider(new CustomAwsCredentialsProvider(awsCredentials));
        return builder.build();
    }

}

