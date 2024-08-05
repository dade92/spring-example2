package webapp.documents;

import java.util.List;

public record PostsResponse(List<PostJson> posts) {
}

record PostJson(String name, String imageLocation) {}
