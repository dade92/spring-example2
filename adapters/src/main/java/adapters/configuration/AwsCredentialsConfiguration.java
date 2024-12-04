package adapters.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class AwsCredentialsConfiguration {

    private Logger logger = LoggerFactory.getLogger(AwsCredentialsConfiguration.class);

    @Bean
    @Profile("prod")
    public AWSCredentials awsCredentialsProd(AwsProperties awsProperties) {
        try {
            return DefaultAWSCredentialsProviderChain.getInstance().getCredentials();
        } catch (Exception e) {
            logger.error("Cannot retrieve AWS credentials, falling back to default one from environment variables", e);
            return new BasicAWSCredentials(
                awsProperties.accessKey,
                awsProperties.secretAccessKey
            );
        }
    }

    @Bean
    @Profile("default")
    public AWSCredentials awsCredentialsLocal(AwsProperties awsProperties) {
        return new BasicAWSCredentials(
            awsProperties.accessKey,
            awsProperties.secretAccessKey
        );
    }

}
