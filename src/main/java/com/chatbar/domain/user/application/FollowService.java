package com.chatbar.domain.user.application;

import com.chatbar.domain.user.domain.Follow;
import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.FollowRepository;
import com.chatbar.domain.user.domain.repository.UserRepository;
import com.chatbar.domain.user.dto.FollowRes;
import com.chatbar.global.DefaultAssert;
import com.chatbar.global.config.security.token.UserPrincipal;
import com.chatbar.global.payload.ApiResponse;
import com.chatbar.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FollowService {

    private final FollowRepository followRepository;

    private final UserRepository userRepository;

    //둘 다 DB에 영향을 주므로 @Transactional을 사용
    @Transactional
    public ResponseEntity<ApiResponse> startFollowing(UserPrincipal userPrincipal, Long toUserId) {

        //유저 확인
        Optional<User> fromUser = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(fromUser.isPresent(), "현재 유저가 올바르지 않습니다.");

        Optional<User> toUser = userRepository.findById(toUserId);
        DefaultAssert.isTrue(toUser.isPresent(), "구독을 받을 유저가 올바르지 않습니다.");

        DefaultAssert.isTrue(!toUserId.equals(userPrincipal.getId()),"선택한 유저와 현재 유저가 같습니다.");

        //이미 구독한 경우 예외처리
        Optional<Follow> test = followRepository.findFollowByFromUserAndToUser(fromUser.get(), toUser.get());
        DefaultAssert.isTrue(test.isEmpty(), "이미 구독한 유저입니다.");

        Follow follow = Follow.builder()
                .fromUser(fromUser.get())
                .toUser(toUser.get())
                .build();

        followRepository.save(follow);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("구독을 시작합니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<ApiResponse> stopFollowing(UserPrincipal userPrincipal, Long toUserId) {

        //유저 확인
        Optional<User> fromUser = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(fromUser.isPresent(), "현재 유저가 올바르지 않습니다.");

        Optional<User> toUser = userRepository.findById(toUserId);
        DefaultAssert.isTrue(toUser.isPresent(), "구독을 받을 유저가 올바르지 않습니다.");

        DefaultAssert.isTrue(!toUserId.equals(userPrincipal.getId()),"선택한 유저와 현재 유저가 같습니다.");

        //삭제할 구독이 없는 경우
        Optional<Follow> follow = followRepository.findFollowByFromUserAndToUser(fromUser.get(), toUser.get());
        DefaultAssert.isTrue(follow.isPresent(), "구독되어있지 않습니다.");

        followRepository.delete(follow.get());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("구독을 종료합니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //내가 구독하는 유저 리스트
    public ResponseEntity<ApiResponse> IFollow(UserPrincipal userPrincipal) {

        //유저 확인
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        User fromUser = user.get(); //proxy 문제 해결 코드(먼저객체로 가져와야 지연로딩 가능)
        List<Follow> followList = followRepository.findAllByFromUser(fromUser);

        List<FollowRes> followingList = followList.stream().map(
                follow -> FollowRes.builder()
                        .id(follow.getToUser().getId())
                        .nickname(follow.getToUser().getNickname())
                        .email(follow.getToUser().getEmail())
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(followingList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //나를 구독하는 유저 리스트
    public ResponseEntity<ApiResponse> FollowMe(UserPrincipal userPrincipal) {

        //유저 확인
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        User toUser = user.get(); //proxy 문제 해결 코드(먼저객체로 가져와야 지연로딩 가능)
        List<Follow> followList = followRepository.findAllByToUser(toUser);

        List<FollowRes> followerList = followList.stream().map(
                follow -> FollowRes.builder()
                        .id(follow.getFromUser().getId())
                        .nickname(follow.getFromUser().getNickname())
                        .email(follow.getFromUser().getEmail())
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(followerList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
