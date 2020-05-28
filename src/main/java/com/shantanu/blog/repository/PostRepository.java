package com.shantanu.blog.repository;

import com.shantanu.blog.model.Post;
import com.shantanu.blog.model.Subreddit;
import com.shantanu.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
