package ru.itmentor.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmentor.spring.boot_security.demo.modules.User;
import ru.itmentor.spring.boot_security.demo.service.UserService;

import java.net.Authenticator;
import java.util.Optional;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String userInfo(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userService.findUserByEmail(auth.getName());
        model.addAttribute("user", user.get());
        return "user";
    }

}
