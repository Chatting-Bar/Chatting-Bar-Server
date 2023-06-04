package com.chatbar.domain.email;

import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.UserRepository;
import com.chatbar.global.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    private final Map<String, VerificationCode> verificationCodes = new HashMap<>();


    public ResponseEntity<ApiResponse> sendVerificationCode(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .check(false)
                    .information("해당 이메일을 가진 유저가 존재하지 않습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }

        // 이메일이 존재하는 경우의 코드
        User user = userOptional.get();

        VerificationCode code = new VerificationCode(generateRandomCode(6), 5);
        verificationCodes.put(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your verification code");
        message.setText("Your verification code is: " + code.getCode() + "\nThis code will expire in 5 minutes.");

        mailSender.send(message);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("이메일이 성공적으로 발송되었습니다!")
                .build();
        return ResponseEntity.ok(apiResponse);
    }



    public ResponseEntity<ApiResponse> verifyCode(String email, String enteredCode) {
        VerificationCode correctCode = verificationCodes.get(email);
        if (correctCode == null || correctCode.isExpired()) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .check(false)
                    .information("인증 코드가 올바르지 않습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        boolean codeVerified = correctCode.getCode().equals(enteredCode);
        if (codeVerified) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .check(true)
                    .information("인증에 성공하였습니다!")
                    .build();
            return ResponseEntity.ok(apiResponse);
        } else {
            ApiResponse apiResponse = ApiResponse.builder()
                    .check(false)
                    .information("인증 코드가 올바르지 않습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
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
