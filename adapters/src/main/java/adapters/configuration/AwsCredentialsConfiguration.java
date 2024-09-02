package adapters.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class AwsCredentialsConfiguration {

    @Bean
    public AWSCredentials awsCredentials(AwsProperties awsProperties) {
        return DefaultAWSCredentialsProviderChain.getInstance().getCredentials();
    }

    private AWSCredentials retrieveAWSCredentials(AwsProperties awsProperties) {
        if (Objects.equals(awsProperties.sessionToken, "${SESSION_TOKEN}")) {
            return new BasicAWSCredentials(
                awsProperties.accessKey,
                awsProperties.secretAccessKey
            );
        } else {
            return DefaultAWSCredentialsProviderChain.getInstance().getCredentials();
//            return new BasicSessionCredentials(
//                awsProperties.accessKey,
//                awsProperties.secretAccessKey,
//                awsProperties.sessionToken
//            );
        }
    }

}
