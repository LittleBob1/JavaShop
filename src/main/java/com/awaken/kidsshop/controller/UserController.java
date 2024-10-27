package com.awaken.kidsshop.controller;

import com.awaken.kidsshop.model.User;
import com.awaken.kidsshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    private ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }

    @GetMapping("/users/{userId}")
    private ResponseEntity<?> getUserById(@PathVariable Long userId) {
        User user = userService.findUserById(userId);
        if(user.getId() != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().body("Пользователь не найден");
    }

    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<?> setRolesToUser(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        try {
            User updatedUser = userService.addRolesToUser(userId, roleIds);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/users/{userId}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            boolean userIsDeleted = userService.deleteUser(userId);
            if(userIsDeleted) {
                return ResponseEntity.ok(true);
            }
            return ResponseEntity.badRequest().body(false);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/users/addNewUser")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        if (!userService.saveUser(user)) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        }
        return ResponseEntity.ok(user);
    }
}
