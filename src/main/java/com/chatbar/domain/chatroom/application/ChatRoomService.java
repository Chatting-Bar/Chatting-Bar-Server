package com.chatbar.domain.chatroom.application;

import com.chatbar.domain.chatroom.domain.ChatRoom;
import com.chatbar.domain.chatroom.domain.UserChatRoom;
import com.chatbar.domain.chatroom.domain.repository.ChatRoomRepository;
import com.chatbar.domain.chatroom.domain.repository.UserChatRoomRepository;
import com.chatbar.domain.chatroom.dto.CreateRoomReq;
import com.chatbar.domain.chatroom.dto.RoomListRes;
import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.UserRepository;
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
@Transactional(readOnly = true)
@Service
public class ChatRoomService {

    private UserRepository userRepository;

    private ChatRoomRepository chatRoomRepository;

    private UserChatRoomRepository userChatRoomRepository;

    //방 만들기
    @Transactional
    public ResponseEntity<?> createChatRoom(UserPrincipal userPrincipal, CreateRoomReq createRoomReq) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "올바르지 않은 유저입니다.");

        ChatRoom chatRoom = ChatRoom.builder()
                .name(createRoomReq.getName())
                .desc(createRoomReq.getDesc())
                .categories(createRoomReq.getCategories())
                .open(createRoomReq.getOpenTime())
                .close(createRoomReq.getCloseTime())
                .max(createRoomReq.getMaxParticipant())
                .isPrivate(createRoomReq.getIsPrivate())
                .password(createRoomReq.getPassword())
                .build();

        UserChatRoom userChatRoom = UserChatRoom.builder()
                .user(user.get())
                .chatRoom(chatRoom)
                .build();

        chatRoomRepository.save(chatRoom);

        userChatRoomRepository.save(userChatRoom);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("방이 생성되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //방 조회 (조회 기준 - 방 id)
    public ResponseEntity<?> findChatRoom(UserPrincipal userPrincipal) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();

        List<RoomListRes> roomListRes = chatRoomList.stream().map(
                chatRoom -> RoomListRes.builder()
                        .id(chatRoom.getId())
                        .name(chatRoom.getName())
                        .host(chatRoom.getHost())
                        .open(chatRoom.getOpenTime())
                        .close(chatRoom.getCloseTime())
                        .current(chatRoom.getCurrentParticipant())
                        .max(chatRoom.getMaxParticipant())
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomListRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}
