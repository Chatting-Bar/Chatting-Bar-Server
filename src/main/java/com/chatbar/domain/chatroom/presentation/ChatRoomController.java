package com.chatbar.domain.chatroom.presentation;

import com.chatbar.domain.chatroom.application.ChatRoomService;
import com.chatbar.domain.chatroom.dto.CreateRoomReq;
import com.chatbar.domain.chatroom.dto.EnterRoomReq;
import com.chatbar.domain.user.domain.User;
import com.chatbar.global.config.security.token.CurrentUser;
import com.chatbar.global.config.security.token.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //방 생성
    @PostMapping
    public ResponseEntity<?> createChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CreateRoomReq createRoomReq
    ){
        return chatRoomService.createChatRoom(userPrincipal, createRoomReq);
    }

    //방 입장
    @PostMapping("/enter")
    public ResponseEntity<?> enterChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody EnterRoomReq enterRoomReq
    ){
        return chatRoomService.enterChatRoom(userPrincipal, enterRoomReq);
    }

    //방 퇴장
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> exitChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "roomId") Long roomId
    ){
        return chatRoomService.exitChatRoom(userPrincipal, roomId);
    }

    //방 조회
    @GetMapping
    public ResponseEntity<?> findChatRoom(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return chatRoomService.findChatRoom(userPrincipal);
    }

    //방 검색
    @GetMapping("/{search}")
    public ResponseEntity<?> searchChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "search") String search
    ){
        return chatRoomService.findChatRoomByMenuAndHost(userPrincipal, search);
    }
}
