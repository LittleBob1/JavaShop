package com.awaken.kidsshop.modules.user.service;

import com.awaken.kidsshop.modules.role.dto.RoleResponse;
import com.awaken.kidsshop.modules.role.entity.Role;
import com.awaken.kidsshop.modules.user.controller.dto.request.UserRequest;
import com.awaken.kidsshop.modules.user.controller.dto.response.UserResponse;
import com.awaken.kidsshop.modules.user.entity.User;
import com.awaken.kidsshop.modules.role.repository.RoleRepository;
import com.awaken.kidsshop.modules.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public UserResponse findUserById(Long userId) {
        User userFromDb = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userFromDb.getId());
        userResponse.setUsername(userFromDb.getUsername());
        userResponse.setRoles(getRolesResponse(userFromDb));
        return userResponse;
    }

    public List<UserResponse> allUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setUsername(user.getUsername());
            userResponse.setRoles(getRolesResponse(user));
            return userResponse;
        }).toList();
    }

    public UserResponse saveUserByAdmin(UserRequest userRequest) {
        User userFromDB = userRepository.findByUsername(userRequest.getUsername());
        if (userFromDB != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        if (userRequest.getPassword() == null || userRequest.getPassword().length() < 6){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 6 characters");
        }

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        userRequest.getRoles().forEach(roleId -> roleRepository.findById(roleId).ifPresent(roles::add));

        if(roles.isEmpty()){
            user.setRoles(Collections.singleton(roleRepository.findByName("ROLE_USER")));
        }else{
            user.setRoles(roles);
        }

        userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setId(user.getId());
        userResponse.setRoles(getRolesResponse(user));

        return userResponse;
    }

    public UserResponse addRolesToUser(Long userId, List<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Role> roles = roleRepository.findAllById(roleIds);
        if (roles.isEmpty()) {
            throw new RuntimeException("No roles found for the provided IDs");
        }

        Set<Role> rolesToAssign = new HashSet<>(roleRepository.findAllById(roleIds));
        user.setRoles(rolesToAssign);

        userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setId(user.getId());
        userResponse.setRoles(getRolesResponse(user));
        return userResponse;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    private Set<RoleResponse> getRolesResponse(User user){
        return user.getRoles().stream().map(role -> new RoleResponse(role.getId(), role.getName())).collect(Collectors.toSet());
    }
}