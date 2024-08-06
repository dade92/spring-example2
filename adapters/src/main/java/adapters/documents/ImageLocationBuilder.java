package adapters.documents;

import documents.ImageLocation;

public class ImageLocationBuilder {

    private static final String AMAZON_PREFIX = ".s3.eu-central-1.amazonaws.com/";

    public ImageLocation build(String bucketName, String fileName) {
        return new ImageLocation("https://" + bucketName + AMAZON_PREFIX + fileName);
    }

}
