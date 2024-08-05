package data;

import documents.ImageLocation;

public record Post(
    String name,
    ImageLocation imageLocation
) {
}
