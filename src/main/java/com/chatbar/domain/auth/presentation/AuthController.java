package com.chatbar.domain.auth.presentation;

import com.chatbar.domain.auth.application.AuthService;
import com.chatbar.domain.auth.dto.RefreshTokenReq;
import com.chatbar.domain.auth.dto.SignInReq;
import com.chatbar.domain.auth.dto.SignUpReq;
import com.chatbar.domain.email.EmailService;
import com.chatbar.domain.user.application.UserService;
import com.chatbar.domain.auth.dto.ChangePasswordRes;
import com.chatbar.domain.auth.dto.EmailRes;
import com.chatbar.domain.user.dto.VerifyRes;
import com.chatbar.global.config.security.token.CurrentUser;
import com.chatbar.global.config.security.token.UserPrincipal;
import com.chatbar.global.payload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final EmailService emailService;

    private final UserService userService;

    //회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody SignUpReq signUpReq) {

        return authService.signUp(signUpReq);
    }

    //로그인
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody SignInReq signInReq) {

        return authService.signIn(signInReq);
    }

    //로그아웃
    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody RefreshTokenReq refreshTokenReq){

        return authService.signOut(refreshTokenReq);
    }

    //refresh
    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refresh(
             @Valid @RequestBody RefreshTokenReq tokenRefreshRequest){

        return authService.refresh(tokenRefreshRequest);
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
}
