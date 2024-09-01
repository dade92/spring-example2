package adapters.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;

public class CustomAwsCredentialsProvider implements AwsCredentialsProvider {
    private final AWSCredentials awsCredentials;

    public CustomAwsCredentialsProvider(AWSCredentials awsCredentials) {
        this.awsCredentials = awsCredentials;
    }

    @Override
    public AwsCredentials resolveCredentials() {
        if(awsCredentials instanceof BasicSessionCredentials) {
            return AwsSessionCredentials.create(
                awsCredentials.getAWSAccessKeyId(),
                awsCredentials.getAWSSecretKey(),
                ((BasicSessionCredentials) awsCredentials).getSessionToken()
            );
        } else {
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
}

