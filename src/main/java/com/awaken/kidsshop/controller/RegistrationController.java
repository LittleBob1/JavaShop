package com.awaken.kidsshop.controller;

import com.awaken.kidsshop.model.User;
import com.awaken.kidsshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (!userService.saveUser(user)) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        }
        return ResponseEntity.ok("Вы успешно зарегистрированы");
    }
}
