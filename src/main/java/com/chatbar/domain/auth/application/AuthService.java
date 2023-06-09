package com.chatbar.domain.auth.application;

import com.chatbar.domain.auth.domain.Token;
import com.chatbar.domain.auth.domain.repository.TokenRepository;
import com.chatbar.domain.auth.dto.*;
import com.chatbar.domain.user.domain.Role;
import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.UserRepository;
import com.chatbar.global.DefaultAssert;
import com.chatbar.global.payload.ApiResponse;
import com.chatbar.global.payload.Message;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final CustomTokenProviderService customTokenProviderService;




    //회원가입
    @Transactional
    public ResponseEntity<?> signUp(SignUpReq signUpReq) {
        DefaultAssert.isTrue(!userRepository.existsByEmail(signUpReq.getEmail()), "해당 이메일이 존재합니다.");

        User user = User.builder()
                .nickname(signUpReq.getNickname())
                .email(signUpReq.getEmail())
                .password(passwordEncoder.encode(signUpReq.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users")
                .buildAndExpand(user.getId()).toUri();
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("회원가입에 성공했습니다").build())
                .build();

        return ResponseEntity.created(location).body(apiResponse);
    }

    //로그인
    @Transactional
    public ResponseEntity<?> signIn(SignInReq signInReq){
        Optional<User> user = userRepository.findByEmail(signInReq.getEmail());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        User findUser = user.get();  // get() => Optional로 받은 User객체 꺼내기
        boolean passwordCheck = passwordEncoder.matches(signInReq.getPassword(), findUser.getPassword());
        DefaultAssert.isTrue(passwordCheck, "비밀번호가 일치하지 않습니다.");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInReq.getEmail(),
                        signInReq.getPassword()
                )
        );

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                .refreshToken(tokenMapping.getRefreshToken())
                .userEmail(tokenMapping.getUserEmail())
                .build();

        tokenRepository.save(token);
        AuthRes authResponse = AuthRes.builder()
                .accessToken(tokenMapping.getAccessToken())
                .refreshToken(tokenMapping.getRefreshToken())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(authResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //로그아웃
    @Transactional
    public ResponseEntity<?> signOut(RefreshTokenReq refreshTokenReq){
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshTokenReq.getRefreshToken());
        DefaultAssert.isTrue(token.isPresent(), "이미 로그아웃 되었습니다");

        tokenRepository.delete(token.get());
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("로그아웃 되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> refresh(RefreshTokenReq tokenRefreshRequest) {

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isTrue(token.isPresent(), "다시 로그인 해주세요.");
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());

        TokenMapping tokenMapping;

        try {
            Long expirationTime = customTokenProviderService.getExpiration(tokenRefreshRequest.getRefreshToken());
            tokenMapping = customTokenProviderService.refreshToken(authentication, token.get().getRefreshToken());
        } catch (ExpiredJwtException ex) {
            tokenMapping = customTokenProviderService.createToken(authentication);
            token.get().updateRefreshToken(tokenMapping.getRefreshToken());
        }

        Token updateToken = token.get().updateRefreshToken(tokenMapping.getRefreshToken());

        AuthRes authResponse = AuthRes.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(updateToken.getRefreshToken()).build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(authResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    @Transactional
    public ResponseEntity<ApiResponse> updatePasswordByEmail(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .check(false)
                    .information("유효하지 않은 이메일입니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        User user = userOptional.get();
        user.setPlainPassword(newPassword);
        String encodedPassword = passwordEncoder.encode(user.getPlainPassword());
        user.updatePassword(encodedPassword);
        userRepository.save(user);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(email + " 계정의 비밀번호가 성공적으로 변경되었습니다.")
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
