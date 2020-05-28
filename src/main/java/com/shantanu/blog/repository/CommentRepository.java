package com.shantanu.blog.repository;

import com.shantanu.blog.model.Comment;
import com.shantanu.blog.model.Post;
import com.shantanu.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
