package one.whr.simple.repository;

import one.whr.simple.entity.Category;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.projection.PostProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);

    // use interface-based projection to eliminate unnecessary info.
    // not work on findAll, workaround: findAllBy
    // https://stackoverflow.com/a/48195670/17382357
    Page<PostProjection> findAllBy(Pageable pageable);

    Page<PostProjection> findAllByCategory(Category category, Pageable pageable);

    Page<PostProjection> findAllByTagsId(Long id, Pageable pageable);

    List<PostProjection> findAllByPinned(boolean pinned);
}
