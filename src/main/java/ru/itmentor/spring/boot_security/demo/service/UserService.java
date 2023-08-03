package ru.itmentor.spring.boot_security.demo.service;

import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers();
    Optional<User> findUser(Long id);
    void saveUser(User user);
    void delete(Long uid);
    void edit(Long id, User user);
    List<Role> findAllRoles();
    Optional<Role> findRoleByRoleName(String role);
    Optional<User> findUserByEmail(String email);
    Collection<Role> getUserRoles(Long uid);
}
