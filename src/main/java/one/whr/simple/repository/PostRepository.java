package one.whr.simple.repository;

import one.whr.simple.entity.Comment;
import one.whr.simple.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}