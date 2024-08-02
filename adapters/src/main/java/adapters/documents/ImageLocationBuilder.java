package adapters.documents;

import documents.ImageLocation;

public class ImageLocationBuilder {

    private static final String STATIC_BUCKET_NAME = ".s3.eu-central-1.amazonaws.com/";

    public ImageLocation build(String bucketName, String fileName) {
        return new ImageLocation("https://" + bucketName + STATIC_BUCKET_NAME + fileName);
    }

}
