package adapters.documents;

import documents.ImageLocation;

public class ImageLocationBuilder {

    public ImageLocation build(String bucketName, String fileName) {
        return new ImageLocation("http://" + bucketName + "/" + fileName);
    }

}
