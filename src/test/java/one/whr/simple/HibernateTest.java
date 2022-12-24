package one.whr.simple;

import one.whr.simple.entity.Role;
import one.whr.simple.service.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class HibernateTest {
    @Autowired
    RoleService roleService;

    @Test
    public void findRole() {
        Optional<Role> roleOpt = roleService.findById(1L);
        System.out.println(roleOpt.get().getRoleName());
        assert roleOpt.get().getRoleName().equals("ROLE_ADMIN");
    }
}
