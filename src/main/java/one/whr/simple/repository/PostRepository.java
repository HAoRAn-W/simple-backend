package one.whr.simple.repository;

import one.whr.simple.entity.Post;
import one.whr.simple.entity.projection.PostInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;


import java.util.Optional;

@org.springframework.stereotype.Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);

    // use interface-based projection to eliminate unnecessary info.
    // not work on findAll, workaround: findAllBy
    // https://stackoverflow.com/a/48195670/17382357
    Page<PostInfo> findAllBy(Pageable pageable);

}
