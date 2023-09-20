package one.whr.simple.service;

import one.whr.simple.entity.Avatar;
import one.whr.simple.entity.User;
import one.whr.simple.exceptions.AvatarNotFoundException;
import one.whr.simple.repository.AvatarRepository;
import one.whr.simple.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvatarService {

    AvatarRepository avatarRepository;

    UserRepository userRepository;

    @Autowired
    public AvatarService(AvatarRepository avatarRepository, UserRepository userRepository) {
        this.avatarRepository = avatarRepository;
        this.userRepository = userRepository;
    }

    private volatile Avatar defaultAvatar;

    public Avatar findById(Long id) throws AvatarNotFoundException {
        return avatarRepository.findById(id).orElseThrow(() -> new AvatarNotFoundException("cannot find avatar with id"));
    }

    public Avatar getDefaultAvatar() throws AvatarNotFoundException {
        if (defaultAvatar == null) {
            synchronized (this) {
                if (defaultAvatar == null) {
                    defaultAvatar = avatarRepository.findById(1L).orElseThrow(() -> new AvatarNotFoundException("cannot find default avatar"));
                }
            }
        }
        return defaultAvatar;
    }

    public List<Avatar> findAll() {
        return avatarRepository.findAll();
    }

    public void delete(Long id) throws AvatarNotFoundException {
        if (id == 1L) {
            return;
        }

        Avatar avatar = avatarRepository.findById(id).orElseThrow(() -> new AvatarNotFoundException("Cannot find avatar to delete"));
        List<User> users = avatar.getUsers();
        for (User user : users) {
            user.setAvatar(getDefaultAvatar());
        }

        userRepository.saveAll(users);

        avatarRepository.deleteById(id);
    }

    public void save(Avatar avatar) {
        avatarRepository.save(avatar);
    }
}
