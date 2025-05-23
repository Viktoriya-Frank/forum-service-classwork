package ait.cohort55.security;

import ait.cohort55.post.dao.PostRepository;
import ait.cohort55.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomWebSecurity {


    private final PostRepository postRepository;
    public boolean checkPostAuthor(String postId, String userName){
        Post post = postRepository.findById(postId).orElse(null);
        return post != null && post.getAuthor().equalsIgnoreCase(userName);
    }
}
