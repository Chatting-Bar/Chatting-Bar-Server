package com.chatbar.domain.user.application;

import com.chatbar.domain.common.Category;
import com.chatbar.domain.email.EmailService;
import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.UserRepository;
import com.chatbar.domain.user.dto.UserRes;
import com.chatbar.global.DefaultAssert;
import com.chatbar.global.config.security.token.UserPrincipal;
import com.chatbar.global.payload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    //유저 조회 (조회 기준 - 유저 id)
    public ResponseEntity<?> findUser(UserPrincipal userPrincipal) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        UserRes userRes = UserRes.builder()
                .id(user.get().getId())
                .email(user.get().getEmail())
                .nickname(user.get().getNickname())
                .profileImg(user.get().getProfileImg())
                .categories(EnumSetToString(user.get().getCategories()))
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    private static String[] EnumSetToString(EnumSet<Category> enumSet) {
        if (enumSet == null) {
            return new String[0];
        }

        String[] result = new String[enumSet.size()];
        int index = 0;

        for (Enum<Category> enumValue : enumSet) {
            result[index++] = enumValue.name();
        }

        return result;
    }


    @Transactional
    public void updateCategories(UserPrincipal userPrincipal, EnumSet<Category> newCategories){
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + userPrincipal.getId() + " not found"));
        user.updateCategories(newCategories);
        userRepository.save(user);
    }



    public UserPrincipal getUserPrincipalByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found"));
        return UserPrincipal.create(user);
    }


    public void updatePassword(UserPrincipal userPrincipal, String newPassword) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        user.updatePassword(passwordEncoder.encode(newPassword));
    }

    
    public void updatePasswordByEmail(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found"));
        user.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    
}
