package com.example.postservice.repository;

import com.example.postservice.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "select p from Post p join fetch p.user where p.user.id = :userId"
//            , countQuery = "select p from Post p where p.user.id = :userId"
    )
    Page<Post> findAllByUserId(Integer userId, Pageable pageable);

    @Query(value = "select p from Post p join fetch p.user"
//            , countQuery = "select p from Post p"
    )
    Page<Post> findAllPosts(Pageable pageable);
}
