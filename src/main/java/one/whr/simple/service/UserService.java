package one.whr.simple.service;

import one.whr.simple.entity.User;
import one.whr.simple.entity.projection.UserProjection;
import one.whr.simple.exceptions.UserNotFoundException;
import one.whr.simple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("cannot find user"));
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
