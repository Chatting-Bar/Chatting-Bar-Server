package com.chatbar.domain.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api")
public class EmailVerificationController {
    private final EmailService emailService;

    public EmailVerificationController(EmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping("/requestVerificationCode")
    public ResponseEntity<?> requestVerificationCode(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, Object> payload) {
        String email = (String) payload.get("email");
        String enteredCode = (String) payload.get("enteredCode");

        if (emailService.verifyCode(email, enteredCode)) {
            return ResponseEntity.ok().body("true");
        } else {
            return ResponseEntity.ok().body("false");
        }
    }
}

