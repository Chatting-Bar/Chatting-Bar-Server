package com.chatbar.domain.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, String> verificationCodes = new HashMap<>();

    public void sendVerificationCode(String email) {
        String code = generateRandomCode(6);
        verificationCodes.put(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your verification code");
        message.setText("Your verification code is: " + code);

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String enteredCode) {
        String correctCode = verificationCodes.get(email);
        return correctCode != null && correctCode.equals(enteredCode);
    }

    private String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = new Random().nextInt(characters.length());
            code.append(characters.charAt(randomIndex));
        }

        return code.toString();
    }
}
