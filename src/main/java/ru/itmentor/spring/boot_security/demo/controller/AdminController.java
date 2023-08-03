package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserServiceImp userServiceImp;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AdminController(UserServiceImp userServiceImp, PasswordEncoder passwordEncoder) {
        this.userServiceImp = userServiceImp;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping()
    public String listUsers(Model model){
        model.addAttribute("users", userServiceImp.findAllUsers());
        model.addAttribute("roles", userServiceImp.findAllRoles());
        return "admin";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userServiceImp.findAllRoles());
        return "/new";
    }
    @PostMapping("/new")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @RequestParam(value = "role", defaultValue = "ROLE_USER") String[] roleNames, Model model){

        Set<Role> rolesSet = Arrays.stream(roleNames)
                .map(roleName -> userServiceImp.findRoleByRoleName(roleName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", userServiceImp.findAllRoles());
            return "/new";
        }

        user.setRoles(rolesSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userServiceImp.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id){
        userServiceImp.delete(id);
        return "redirect:/admin";
    }
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        Optional<User> user = userServiceImp.findUser(id);

        model.addAttribute("user", user.get());
        model.addAttribute("allRoles", userServiceImp.findAllRoles());
        return "/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, @PathVariable("id") Long id,
                         @RequestParam("role")  String[] roleNames, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/edit"; // Возвращаем форму с ошибками валидации
        }
        Set<Role> rolesSet = Arrays.stream(roleNames)
                .map(roleName -> userServiceImp.findRoleByRoleName(roleName))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        user.setRoles(rolesSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userServiceImp.edit(id, user);
        return "redirect:/admin";
    }
}
