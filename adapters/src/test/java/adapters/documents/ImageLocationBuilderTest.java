package adapters.documents;

import documents.ImageLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageLocationBuilderTest {

    private final ImageLocationBuilder imageLocationBuilder = new ImageLocationBuilder();

    @Test
    void build() {
        ImageLocation expected = new ImageLocation("https://my-bucket.s3.eu-central-1.amazonaws.com/my-file");
        ImageLocation actual = imageLocationBuilder.build("my-bucket", "my-file");

        assertEquals(expected, actual);
    }
}