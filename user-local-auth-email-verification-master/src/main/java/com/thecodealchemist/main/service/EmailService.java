package com.thecodealchemist.main.service;

import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.factory.MailtrapClientFactory;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    public void sendEmail(String token, String email) {
        String subject = "Verify your account";
        String verificationUrl = "http://localhost:8080/api/auth/verify?token=" + token;

        String content = """
                Hi there,<br><br>
                Thanks for registering. Please verify your email by clicking the link below:<br><br>
                <a href="%s">Verify Email</a><br><br>
                This link will expire in 24 hours.<br><br>
                Cheers,<br>
                Your App Team
                """.formatted(verificationUrl);

        final MailtrapConfig config = new MailtrapConfig.Builder()
                .token("<pass_your_api_token>")
                .build();

        final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

        final MailtrapMail mail = MailtrapMail.builder()
                .from(new Address("hello@demomailtrap.co", "Mailtrap Test"))
                .to(List.of(new Address(email)))
                .subject(subject)
                .text(content)
                .category("Integration Test")
                .build();

        try {
            System.out.println(client.send(mail));
        } catch (Exception e) {
            System.out.println("Caught exception : " + e);
        }

    }

}
