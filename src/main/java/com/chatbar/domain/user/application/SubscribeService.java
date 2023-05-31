package com.chatbar.domain.user.application;

import com.chatbar.domain.user.domain.Subscribe;
import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.SubscribeRepository;
import com.chatbar.domain.user.domain.repository.UserRepository;
import com.chatbar.global.DefaultAssert;
import com.chatbar.global.config.security.token.UserPrincipal;
import com.chatbar.global.payload.ApiResponse;
import com.chatbar.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final SubscribeRepository subscribeRepository;

    private final UserRepository userRepository;

    //둘 다 DB에 영향을 주므로 @Transactional을 사용
    @Transactional
    public ResponseEntity<ApiResponse> startSubsribe(UserPrincipal userPrincipal, Long toUserId) {

        //유저 확인
        Optional<User> fromUser = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(fromUser.isPresent(), "현재 유저가 올바르지 않습니다.");

        Optional<User> toUser = userRepository.findById(toUserId);
        DefaultAssert.isTrue(toUser.isPresent(), "구독을 받을 유저가 올바르지 않습니다.");

        DefaultAssert.isTrue(toUserId.equals(userPrincipal.getId()),"선택한 유저와 현재 유저가 같습니다.");

        //이미 구독한 경우 예외처리
        Optional<Subscribe> test = subscribeRepository.findSubscribeByFromUserAndToUser(fromUser.get(), toUser.get());
        DefaultAssert.isTrue(test.isEmpty(), "이미 구독한 유저입니다.");

        Subscribe subscribe = Subscribe.builder()
                .fromUser(fromUser.get())
                .toUser(toUser.get())
                .build();

        subscribeRepository.save(subscribe);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("구독을 시작합니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<ApiResponse> stopSubsribe(UserPrincipal userPrincipal, Long toUserId) {

        //유저 확인
        Optional<User> fromUser = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(fromUser.isPresent(), "현재 유저가 올바르지 않습니다.");

        Optional<User> toUser = userRepository.findById(toUserId);
        DefaultAssert.isTrue(toUser.isPresent(), "구독을 받을 유저가 올바르지 않습니다.");

        DefaultAssert.isTrue(toUserId.equals(userPrincipal.getId()),"선택한 유저와 현재 유저가 같습니다.");

        //삭제할 구독이 없는 경우
        Optional<Subscribe> subscribe = subscribeRepository.findSubscribeByFromUserAndToUser(fromUser.get(), toUser.get());
        DefaultAssert.isTrue(subscribe.isPresent(), "구독되어있지 않습니다.");

        subscribeRepository.delete(subscribe.get());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("구독을 종료합니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
