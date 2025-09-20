package com.thecodealchemist.main.service;

import com.thecodealchemist.main.db.TokenRepository;
import com.thecodealchemist.main.entity.Status;
import com.thecodealchemist.main.entity.User;
import com.thecodealchemist.main.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    public void createToken(User createdUser) {
        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setUserId(createdUser.getId());
        userToken.setStatus(Status.ACTIVE);
        userToken.setToken(token);

        UserToken createdToken = tokenRepository.save(userToken);

        if(createdUser.getId() != null) {
            System.out.println("Sending email to the user");
            emailService.sendEmail(token, createdUser.getEmail());
        }

    }
}
