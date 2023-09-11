package one.whr.simple.service;

import one.whr.simple.entity.Avatar;
import one.whr.simple.exceptions.AvatarNotFoundException;
import one.whr.simple.repository.AvatarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvatarService {

    AvatarRepository avatarRepository;

    @Autowired
    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
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

    public void delete(Long id) {
        avatarRepository.deleteById(id);
    }

    public void save(Avatar avatar) {
        avatarRepository.save(avatar);
    }
}
