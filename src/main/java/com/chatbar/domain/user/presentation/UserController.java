package com.chatbar.domain.user.presentation;

import com.chatbar.domain.common.Category;
import com.chatbar.domain.email.EmailService;
<<<<<<< Updated upstream
=======
import com.chatbar.domain.user.application.FollowService;
>>>>>>> Stashed changes
import com.chatbar.domain.user.application.UserService;
import com.chatbar.global.config.security.token.CurrentUser;
import com.chatbar.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
<<<<<<< Updated upstream
import org.springframework.beans.factory.annotation.Autowired;
=======
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
    //Set Category of User
    @PatchMapping("/categories")
    public ResponseEntity<?> updateCategories(@CurrentUser UserPrincipal userPrincipal, @RequestBody EnumSet<Category> newCategories){
        try {
            userService.updateCategories(userPrincipal, newCategories);
            return ResponseEntity.ok().build(); // return 200 OK without body
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build(); // return 404 Not Found
        }
=======
    private final EmailService emailService;

    //유저 조회
    @GetMapping
    public ResponseEntity<?> findUser(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.findUser(userPrincipal);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
}
=======

    //나를 구독하는 유저 조회
    @GetMapping("/follower")
    public ResponseEntity<?> findFollower(@CurrentUser UserPrincipal userPrincipal) {
        return followService.FollowMe(userPrincipal);
    }

    //내가 구독하는 유저 조회
    @GetMapping("/following")
    public ResponseEntity<?> findFollowing(@CurrentUser UserPrincipal userPrincipal) {
        return followService.IFollow(userPrincipal);
    }

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

    @PostMapping("/requestVeri")
    public ResponseEntity<?> requestVerificationCode(@RequestBody Map<String, String> emailPayload){
        String email = emailPayload.get("email");
        try {
            emailService.sendVerificationCode(email);
            return ResponseEntity.ok().build(); // return 200 OK without body
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody Map<String, String> payload){
        String email = payload.get("email");
        String code = payload.get("code");
        if(emailService.verifyCode(email, code)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> payload){
        String email = payload.get("email");
        String newPassword = payload.get("newPassword");
        try {
            userService.updatePasswordByEmail(email, newPassword);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
>>>>>>> Stashed changes
