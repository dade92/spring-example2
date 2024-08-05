package webapp.documents;

import data.Post;

import java.util.List;

public record PostsResponse(List<Post> posts) {
}
