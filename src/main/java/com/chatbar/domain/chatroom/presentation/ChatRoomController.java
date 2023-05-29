package com.chatbar.domain.chatroom.presentation;

import com.chatbar.domain.chatroom.application.ChatRoomService;
import com.chatbar.domain.chatroom.dto.CreateRoomReq;
import com.chatbar.global.config.security.token.CurrentUser;
import com.chatbar.global.config.security.token.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<?> createChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CreateRoomReq createRoomReq){

        return chatRoomService.createChatRoom(userPrincipal, createRoomReq);
    }

    @GetMapping
    public ResponseEntity<?> findChatRoom(
            @CurrentUser UserPrincipal userPrincipal){

        return chatRoomService.findChatRoom(userPrincipal);
    }

}
