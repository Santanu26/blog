package com.shantanu.blog.repository;

import com.shantanu.blog.model.Post;
import com.shantanu.blog.model.User;
import com.shantanu.blog.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
