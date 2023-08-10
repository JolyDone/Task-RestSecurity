package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itmentor.spring.boot_security.demo.model.Role;
import ru.itmentor.spring.boot_security.demo.model.User;
import ru.itmentor.spring.boot_security.demo.service.UserServiceImp;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserServiceImp userServiceImp;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public AdminController(UserServiceImp userServiceImp, PasswordEncoder passwordEncoder) {
        this.userServiceImp = userServiceImp;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public List<User> listUsers(){
        return userServiceImp.findAllUsers();
    }

    @PostMapping("/new")
    public ResponseEntity<?> create(@Valid User user,
                                 BindingResult bindingResult,
                                 @RequestParam(value = "role", defaultValue = "ROLE_USER") String[] roleNames){
        if(bindingResult.hasErrors())
        {
            System.out.println(user + " Has Error");
            return new ResponseEntity<>(bindingResult.getFieldError(), HttpStatus.BAD_REQUEST);
        }
        if(userServiceImp.findUserByEmail(user.getEmail()).isPresent()){
            return new ResponseEntity<>("User exist", HttpStatus.BAD_REQUEST);
        }

        Set<Role> rolesSet = Arrays.stream(roleNames)
                .map(roleName -> userServiceImp.findRoleByRoleName(roleName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        user.setRoles(rolesSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userServiceImp.saveUser(user);
        System.out.println(user + " :Was created");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/delete")
    public void delete(@PathVariable("id") Long id){
        userServiceImp.delete(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid User user,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id, @RequestParam(value = "role", defaultValue = "ROLE_USER")  String[] roleNames) {
        if(bindingResult.hasErrors())
        {
            System.out.println(user + " Has Error in update");
            return new ResponseEntity<>(bindingResult.getFieldError(), HttpStatus.BAD_REQUEST);
        }


        Set<Role> rolesSet = Arrays.stream(roleNames)
                .map(roleName -> userServiceImp.findRoleByRoleName(roleName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());


        user.setRoles(rolesSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userServiceImp.edit(id, user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
