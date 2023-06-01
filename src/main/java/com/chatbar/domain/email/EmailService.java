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

<<<<<<< Updated upstream
    private final Map<String, String> verificationCodes = new HashMap<>();

    public void sendVerificationCode(String email) {
        String code = generateRandomCode(6);
=======
    private final Map<String, VerificationCode> verificationCodes = new HashMap<>();

    public void sendVerificationCode(String email) {
        VerificationCode code = new VerificationCode(generateRandomCode(6), 5);
>>>>>>> Stashed changes
        verificationCodes.put(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your verification code");
<<<<<<< Updated upstream
        message.setText("Your verification code is: " + code);
=======
        message.setText("Your verification code is: " + code.getCode() + "\nThis code will expire in 5 minutes.");
>>>>>>> Stashed changes

        mailSender.send(message);
    }

    public boolean verifyCode(String email, String enteredCode) {
<<<<<<< Updated upstream
        String correctCode = verificationCodes.get(email);
        return correctCode != null && correctCode.equals(enteredCode);
=======
        VerificationCode correctCode = verificationCodes.get(email);
        if (correctCode == null || correctCode.isExpired()) {
            return false;
        }

        return correctCode.getCode().equals(enteredCode);
>>>>>>> Stashed changes
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
