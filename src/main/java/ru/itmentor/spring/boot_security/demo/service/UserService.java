package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.dao.RoleRepository;
import ru.itmentor.spring.boot_security.demo.dao.UserRepository;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User> findUser(Long uid){
        return userRepository.findById(uid);
    }
    @Transactional
    public void saveUser(User user){
        userRepository.save(user);
    }
    @Transactional
    public void delete(Long uid){
        userRepository.deleteById(uid);
    }
    public void edit(Long uid, User user){
        userRepository.save(user);
    }
    public List<Role> findAllRoles(){
        return roleRepository.findAll();
    }
    public Optional<Role> findRoleByRoleName(String role){
        return roleRepository.findByRole(role);
    }
    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    @Transactional(readOnly = true)
    public Collection<Role> getUserRoles(Long uid) {
        Optional<User> userOptional = userRepository.findById(uid);
        return userOptional.map(User::getRoles).orElse(Collections.emptyList());
    }
}
