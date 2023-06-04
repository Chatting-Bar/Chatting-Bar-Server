package com.chatbar.domain.chatroom.presentation;

import com.chatbar.domain.chatroom.application.ChatRoomService;
import com.chatbar.domain.chatroom.dto.CloseRoomReq;
import com.chatbar.domain.chatroom.dto.CreateRoomReq;
import com.chatbar.domain.chatroom.dto.EnterRoomReq;
import com.chatbar.domain.chatroom.dto.UserListRes;
import com.chatbar.domain.user.domain.User;
import com.chatbar.global.config.security.token.CurrentUser;
import com.chatbar.global.config.security.token.UserPrincipal;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    ) throws JsonProcessingException {
        return chatRoomService.createChatRoom(userPrincipal, createRoomReq);
    }

    //방 닫기
    @PatchMapping("/close")
    public ResponseEntity<?> closeChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody CloseRoomReq closeRoomReq
            ) throws JsonProcessingException {
        return chatRoomService.closeChatRoom(userPrincipal, closeRoomReq);
    }

    //방 입장
    @PostMapping("/enter")
    public ResponseEntity<?> enterChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @Valid @RequestBody EnterRoomReq enterRoomReq
    ) {
        return chatRoomService.enterChatRoom(userPrincipal, enterRoomReq);
    }

    //방 퇴장
    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> exitChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "roomId") Long roomId
    ) {
        return chatRoomService.exitChatRoom(userPrincipal, roomId);
    }

    //방 강퇴
    @DeleteMapping("")
    public ResponseEntity<?> kickOutChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("roomId") Long roomId,
            @RequestParam("userId") Long userId
    ) {
        return chatRoomService.kickOutChatRoom(userPrincipal, roomId, userId);
    }

    //유저 얼리기
    @PatchMapping("")
    public ResponseEntity<?> frozenUser(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestParam("roomId") Long roomId,
            @RequestParam("userId") Long userId
    ) {
        return chatRoomService.frozenUser(userPrincipal, roomId, userId);
    }

    //방 하나 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<?> findOneChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "roomId") Long roomId
    ) {
        return chatRoomService.findOneChatRoom(userPrincipal, roomId);
    }

    //방 조회
    @GetMapping
    public ResponseEntity<?> findChatRoom(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return chatRoomService.findChatRoom(userPrincipal);
    }

    //방 조회(최신순)
    @GetMapping("/latest")
    public ResponseEntity<?> findLatestChatRoom(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return chatRoomService.findLatestChatRoom(userPrincipal);
    }

    //방 조회(추천 카테고리순)
    @GetMapping("/recommend")
    public ResponseEntity<?> findRecommendedChatRoom(
            @CurrentUser UserPrincipal userPrincipal
    ) {
        return chatRoomService.findRecommendedChatRoom(userPrincipal);
    }

    //방 검색
    @GetMapping("/search/{search}")
    public ResponseEntity<?> searchChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "search") String search
    ) {
        return chatRoomService.findChatRoomByMenuAndHost(userPrincipal, search);
    }

    //방에 있는 유저 조회
    @GetMapping("/{roomId}/users")
    public ResponseEntity<?> findUsersInChatRoom(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "roomId") Long roomId
    ) {
        return chatRoomService.findUsersInChatRoom(userPrincipal, roomId);
    }
}