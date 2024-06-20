package adapters.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("aws")
public class AwsProperties {
    public String accessKey;
    public String secretAccessKey;
    public String bucketName;

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
