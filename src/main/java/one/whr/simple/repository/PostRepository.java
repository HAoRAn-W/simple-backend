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

    //    select post0_.id as col_0_0_, post0_.created_time as col_1_0_, post0_.updated_time as col_2_0_, post0_.title as col_3_0_, post0_.description as col_4_0_, category3_.id as col_5_0_, post0_.cover_url as col_6_0_, category3_.id as id1_0_, category3_.cover_url as cover_ur2_0_, category3_.name as name3_0_ from posts post0_
//    left outer join user_favorites favoriteus1_ on post0_.id=favoriteus1_.post_id
//    left outer join users user2_ on favoriteus1_.user_id=user2_.id
//    left outer join categories category3_ on post0_.category_id=category3_.id
//    where user2_.username=?
//    limit ?
    Page<PostProjection> findAllByFavoriteUsersUsername(String username, Pageable pageable);


}
