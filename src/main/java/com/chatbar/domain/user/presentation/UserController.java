package com.chatbar.domain.user.presentation;

import com.chatbar.domain.common.Category;
import com.chatbar.domain.email.EmailService;
import com.chatbar.domain.user.application.UserService;
import com.chatbar.global.config.security.token.CurrentUser;
import com.chatbar.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    //Set Category of User
    @PatchMapping("/categories")
    public ResponseEntity<?> updateCategories(@CurrentUser UserPrincipal userPrincipal, @RequestBody EnumSet<Category> newCategories){
        try {
            userService.updateCategories(userPrincipal, newCategories);
            return ResponseEntity.ok().build(); // return 200 OK without body
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // return 404 Not Found
        }
    }

    @PostMapping("/requestVerificationCode")
    public ResponseEntity<?> requestVerificationCode(@RequestBody Map<String, String> emailPayload){
        String email = emailPayload.get("email");
        try {
            emailService.sendVerificationCode(email);
            return ResponseEntity.ok().build(); // return 200 OK without body
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verifyCodeAndChangePassword")
    public ResponseEntity<?> verifyCodeAndChangePassword(@RequestBody Map<String, String> payload){
        String email = payload.get("email");
        String code = payload.get("code");
        String newPassword = payload.get("newPassword");
        if(emailService.verifyCode(email, code)) {
            try {
                userService.updatePasswordByEmail(email, newPassword);
                return ResponseEntity.ok().build();
            } catch (NoSuchElementException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
