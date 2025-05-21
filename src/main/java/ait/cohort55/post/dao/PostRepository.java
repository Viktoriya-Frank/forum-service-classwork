package ait.cohort55.post.dao;

import ait.cohort55.post.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface PostRepository extends MongoRepository<Post, String> {

    Stream<Post> findPostsByTagsInIgnoreCase(List<String> tags);

    Stream<Post> findPostsByAuthorIgnoreCase(String author);

    Stream<Post> findPostsByDateCreatedBetween(LocalDate from, LocalDate to);
}
