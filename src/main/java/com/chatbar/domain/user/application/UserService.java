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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public ResponseEntity<ApiResponse> updateCategories(UserPrincipal userPrincipal, EnumSet<Category> newCategories) {
        Optional<User> userOptional = userRepository.findById(userPrincipal.getId());

        if (userOptional.isEmpty()) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .check(false)
                    .information("아이디를 찾을 수 없습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }

        User user = userOptional.get();
        user.updateCategories(newCategories);
        userRepository.save(user);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information("아이디 " + userPrincipal.getEmail() + "의 카테고리를 성공적으로 변경했습니다!")
                .build();

        return ResponseEntity.ok(apiResponse);
    }






}
