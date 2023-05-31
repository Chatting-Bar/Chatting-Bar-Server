package com.chatbar.domain.user.presentation;

import com.chatbar.domain.user.application.SubscribeService;
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
    private final SubscribeService subscribeService;

    //유저 조회
    @GetMapping
    public ResponseEntity<?> findUser(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.findUser(userPrincipal);
    }

    //구독 시작
    @PostMapping("/subscribe/{toUserId}")
    public ResponseEntity<?> subscribe(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "toUserId") Long toUserId
    ) {
        return subscribeService.startSubsribe(userPrincipal, toUserId);
    }

    //구독 종료
    @DeleteMapping("/subscribe/{toUserId}")
    public ResponseEntity<?> unsubscribe(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "toUserId") Long toUserId
    ) {
        return subscribeService.stopSubsribe(userPrincipal, toUserId);
    }

}