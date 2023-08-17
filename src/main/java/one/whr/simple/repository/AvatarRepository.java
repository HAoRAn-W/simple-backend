package one.whr.simple.repository;

import one.whr.simple.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    List<Avatar> findAll();

    Optional<Avatar> findById(Long id);
}
