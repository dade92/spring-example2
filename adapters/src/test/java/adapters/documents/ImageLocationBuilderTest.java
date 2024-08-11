package adapters.documents;

import documents.ImageLocation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImageLocationBuilderTest {

    private final ImageLocationBuilder imageLocationBuilder = new ImageLocationBuilder();

    @Test
    void build() {
        ImageLocation expected = new ImageLocation("http://my-bucket/my-file");
        ImageLocation actual = imageLocationBuilder.build("my-bucket", "my-file");

        assertEquals(expected, actual);
    }
}