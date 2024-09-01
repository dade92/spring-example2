package adapters.configuration;

import com.amazonaws.auth.AWSCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

public class CustomAwsCredentialsProvider implements AwsCredentialsProvider {
    private final AWSCredentials awsCredentials;

    public CustomAwsCredentialsProvider(AWSCredentials awsCredentials) {
        this.awsCredentials = awsCredentials;
    }

    @Override
    public AwsCredentials resolveCredentials() {
        return new AwsCredentials() {
            @Override
            public String accessKeyId() {
                return awsCredentials.getAWSAccessKeyId();
            }

            @Override
            public String secretAccessKey() {
                return awsCredentials.getAWSSecretKey();
            }
        };
    }
}

