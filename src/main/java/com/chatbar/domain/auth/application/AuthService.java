package com.chatbar.domain.auth.application;

import com.amazonaws.Response;
import com.chatbar.domain.auth.domain.Token;
import com.chatbar.domain.auth.domain.repository.TokenRepository;
import com.chatbar.domain.auth.dto.*;
import com.chatbar.domain.user.domain.Role;
import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.UserRepository;
import com.chatbar.global.DefaultAssert;
import com.chatbar.global.payload.ApiResponse;
import com.chatbar.global.payload.Message;
import lombok.RequiredArgsConstructor;
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
                .password(signUpReq.getPassword())
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

}
