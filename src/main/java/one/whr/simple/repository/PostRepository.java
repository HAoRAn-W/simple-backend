package one.whr.simple.repository;

import javafx.geometry.Pos;
import one.whr.simple.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);

    Page<Post> findAll(Pageable pageable);
}
