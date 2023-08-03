package ru.itmentor.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

@Service("userServiceImp")
public class UserServiceImp implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUser(Long uid){
        return userRepository.findById(uid);
    }
    @Override
    public void saveUser(User user){
        userRepository.save(user);
    }
    @Override
    public void delete(Long uid){
        userRepository.deleteById(uid);
    }
    @Override
    public void edit(Long uid, User user){
        userRepository.save(user);
    }
    @Override
    @Transactional(readOnly = true)
    public List<Role> findAllRoles(){
        return roleRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findRoleByRoleName(String role){
        return roleRepository.findByRole(role);
    }
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email){
        return userRepository.findByEmail(email);
    }
    @Override
    @Transactional(readOnly = true)
    public Collection<Role> getUserRoles(Long uid) {
        Optional<User> userOptional = userRepository.findById(uid);
        return userOptional.map(User::getRoles).orElse(Collections.emptyList());
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User '%s' not found", email)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
