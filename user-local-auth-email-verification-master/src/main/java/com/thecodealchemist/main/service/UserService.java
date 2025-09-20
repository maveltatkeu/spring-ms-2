package com.thecodealchemist.main.service;

import com.thecodealchemist.main.db.UserRepository;
import com.thecodealchemist.main.dto.UserDTO;
import com.thecodealchemist.main.entity.Status;
import com.thecodealchemist.main.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void register(UserDTO userDTO) {
        User u = new User();
        u.setPassword(passwordEncoder.encode(userDTO.password()));
        u.setUsername(userDTO.username());
        u.setEmail(userDTO.email());
        u.setStatus(Status.INACTIVE);

        User createdUser = userRepository.save(u);

        if(createdUser.getId() != null) {
            tokenService.createToken(createdUser);
        }

    }
}
