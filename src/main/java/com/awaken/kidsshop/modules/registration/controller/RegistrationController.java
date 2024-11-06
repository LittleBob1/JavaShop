package com.awaken.kidsshop.modules.registration.controller;

import com.awaken.kidsshop.modules.registration.controller.dto.RegistrationRequest;
import com.awaken.kidsshop.modules.registration.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        if (registrationService.saveUser(registrationRequest)) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        }
        return ResponseEntity.ok("Вы успешно зарегистрированы");
    }

}
