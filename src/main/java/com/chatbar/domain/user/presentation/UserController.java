package com.chatbar.domain.user.presentation;

import com.chatbar.domain.common.Category;
import com.chatbar.domain.email.EmailService;
import com.chatbar.domain.user.application.FollowService;
import com.chatbar.domain.user.application.UserService;
import com.chatbar.domain.user.dto.ChangePasswordRes;
import com.chatbar.domain.user.dto.EmailRes;
import com.chatbar.domain.user.dto.VerifyRes;
import com.chatbar.global.config.security.token.CurrentUser;
import com.chatbar.global.config.security.token.UserPrincipal;
import com.chatbar.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
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
    private final FollowService followService;

    private final EmailService emailService;

    //유저 조회
    @GetMapping
    public ResponseEntity<?> findUser(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.findUser(userPrincipal);
    }

    //구독 시작
    @PostMapping("/follow/{toUserId}")
    public ResponseEntity<?> follow(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "toUserId") Long toUserId
    ) {
        return followService.startFollowing(userPrincipal, toUserId);
    }

    //구독 종료
    @DeleteMapping("/follow/{toUserId}")
    public ResponseEntity<?> unfollow(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "toUserId") Long toUserId
    ) {
        return followService.stopFollowing(userPrincipal, toUserId);
    }

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
    public ResponseEntity<?> updateCategories(@CurrentUser UserPrincipal userPrincipal, @RequestBody EnumSet<Category> newCategories) {
        try {
            ApiResponse apiResponse = userService.updateCategories(userPrincipal, newCategories);
            return ResponseEntity.ok(apiResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .check(false)
                    .information(e.getMessage())
                    .build()); // return 400 Bad Request
        }
    }


    @PostMapping("/requestVeri")
    public ResponseEntity<ApiResponse> requestVerificationCode(@RequestBody EmailRes emailRes) {
        return emailService.sendVerificationCode(emailRes.getEmail());
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<ApiResponse> verifyCode(@RequestBody VerifyRes verifyRes) {
        return emailService.verifyCode(verifyRes.getEmail(), verifyRes.getCode());
    }



    @PostMapping("/changePassword")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody ChangePasswordRes changePasswordRes) {
        String email = changePasswordRes.getEmail();
        String newPassword = changePasswordRes.getNewPassword();

        return userService.updatePasswordByEmail(email, newPassword);

    }



    //ID로 구독자 조회
    @GetMapping("/following/{toUserId}")
    public ResponseEntity<?> findFollowingById(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "toUserId") Long toUserId
    ) {
        return followService.Follow(userPrincipal, toUserId);
    }


}
