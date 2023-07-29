package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.DAO.RoleRepository;
import ru.itmentor.spring.boot_security.demo.DAO.UserRepository;
import ru.itmentor.spring.boot_security.demo.modules.Role;
import ru.itmentor.spring.boot_security.demo.modules.User;

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
        Optional<User> updateUser = userRepository.findById(uid);
        if(updateUser.isEmpty()){
            throw new UsernameNotFoundException("No user");
        } else {
            userRepository.save(user);
        }
    }
    public List<Role> findAllRoles(){
        return roleRepository.findAll();
    }
    public Optional<Role> findRoleByRoleName(String role){
        return roleRepository.findByRole(role);
    }
}
