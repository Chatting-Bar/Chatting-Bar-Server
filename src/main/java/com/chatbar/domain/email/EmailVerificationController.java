package com.chatbar.domain.email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
