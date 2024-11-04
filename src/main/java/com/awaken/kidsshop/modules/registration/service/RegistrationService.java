package com.awaken.kidsshop.modules.registration.service;

import com.awaken.kidsshop.modules.registration.controller.dto.RegistrationRequest;
import com.awaken.kidsshop.modules.role.repository.RoleRepository;
import com.awaken.kidsshop.modules.user.entity.User;
import com.awaken.kidsshop.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean saveUser(RegistrationRequest user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return true;
        }

        if (user.getPassword() == null || user.getPassword().length() < 6){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters");
        }

        User userToSave = new User();
        userToSave.setUsername(user.getUsername());
        userToSave.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userToSave.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER")));

        userRepository.save(userToSave);

        return false;
    }
}
