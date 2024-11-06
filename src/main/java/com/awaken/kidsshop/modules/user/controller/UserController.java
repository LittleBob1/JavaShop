package com.awaken.kidsshop.modules.user.controller;

import com.awaken.kidsshop.modules.user.controller.dto.request.UserRequest;
import com.awaken.kidsshop.modules.user.controller.dto.response.UserResponse;
import com.awaken.kidsshop.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    private List<UserResponse> getAllUsers() {
        return userService.allUsers();
    }

    @GetMapping("/users/{userId}")
    private UserResponse getUserById(@PathVariable Long userId) {
        return userService.findUserById(userId);
    }

    @PostMapping("/users/{userId}/roles")
    public UserResponse setRolesToUser(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        try {
            return userService.addRolesToUser(userId, roleIds);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());        }
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
    public UserResponse addNewUser(@RequestBody UserRequest userRequest) {
        return userService.saveUserByAdmin(userRequest);
    }
}
