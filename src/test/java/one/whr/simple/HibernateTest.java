package one.whr.simple;

import one.whr.simple.entity.Comment;
import one.whr.simple.entity.User;
import one.whr.simple.repository.CommentRepository;
import one.whr.simple.service.RoleService;
import one.whr.simple.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HibernateTest {
    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;
    @Autowired
    private CommentRepository commentRepository;







    @Test
    public void commentGetUser() {
        Comment comment = commentRepository.findById(1L).get();
        User user = comment.getUser();
        assert user.getUserId() == 1;
    }
}
