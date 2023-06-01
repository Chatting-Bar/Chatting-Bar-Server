package com.chatbar.domain.user.presentation;

import com.chatbar.domain.user.application.FollowService;
import com.chatbar.domain.user.application.UserService;
import com.chatbar.global.config.security.token.CurrentUser;
import com.chatbar.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final FollowService followService;

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

}