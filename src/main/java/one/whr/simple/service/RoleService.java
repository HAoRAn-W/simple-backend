package one.whr.simple.service;

import one.whr.simple.entity.Role;
import one.whr.simple.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public Optional<Role> findById(Long roleId) {
        return roleRepository.findById(roleId);
    }
}
