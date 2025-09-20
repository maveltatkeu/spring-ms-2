package com.thecodealchemist.main.service;

import com.thecodealchemist.main.db.TokenRepository;
import com.thecodealchemist.main.db.UserRepository;
import com.thecodealchemist.main.entity.Status;
import com.thecodealchemist.main.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenVerificationService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private UserRepository userRepository;

    public boolean verify(String token) {
        UserToken userToken = tokenRepository.findByToken(token);
        if(userToken != null) {
            userToken.setStatus(Status.INACTIVE);
            tokenRepository.save(userToken);

            userRepository.findById(userToken.getUserId()).ifPresent(user -> {
                user.setStatus(Status.ACTIVE);
                userRepository.save(user);
            });

            return true;
        }
        return false;
    }
}
