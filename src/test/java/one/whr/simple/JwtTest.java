package one.whr.simple;

import one.whr.simple.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {
    @Autowired
    JwtUtils jwtUtils;

    @Test
    public void keygen() {
        System.out.println(jwtUtils.generateTokenByUsername("hello"));
    }

}
