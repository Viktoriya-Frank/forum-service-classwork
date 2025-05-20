package ait.cohort55.post.dao;

import ait.cohort55.post.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    Collection<Object> findByTagsIn(List<String> tags);

    Collection<Object> findByAuthorIgnoreCase(String author);
}
