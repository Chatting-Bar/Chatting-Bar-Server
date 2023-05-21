package com.chatbar.domain.chatroom.application;

import com.chatbar.domain.chatroom.domain.ChatRoom;
import com.chatbar.domain.chatroom.domain.UserChatRoom;
import com.chatbar.domain.chatroom.domain.repository.ChatRoomRepository;
import com.chatbar.domain.chatroom.domain.repository.UserChatRoomRepository;
import com.chatbar.domain.chatroom.dto.CreateRoomReq;
import com.chatbar.domain.chatroom.dto.EnterRoomReq;
import com.chatbar.domain.chatroom.dto.ResultRoomListRes;
import com.chatbar.domain.chatroom.dto.RoomListRes;
import com.chatbar.domain.common.Category;
import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.UserRepository;
import com.chatbar.global.DefaultAssert;
import com.chatbar.global.config.security.token.UserPrincipal;
import com.chatbar.global.payload.ApiResponse;
import com.chatbar.global.payload.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomService {

    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;

    private final UserChatRoomRepository userChatRoomRepository;

    //방 만들기
    @Transactional
    public ResponseEntity<?> createChatRoom(UserPrincipal userPrincipal, CreateRoomReq createRoomReq) throws JsonProcessingException {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "올바르지 않은 유저입니다.");

        ChatRoom chatRoom = ChatRoom.builder()
                .name(createRoomReq.getName())
                .host(user.get())
                .desc(createRoomReq.getDesc())
                .categories(jsonToEnumSet(createRoomReq.getCategories()))
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
        chatRoom.updateCurrentParticipant(1);

        // 방 생성 자체가 본인이 방장으로 참여한 것이기 때문에 userChatRoomRepository에도 추가해준다.
        userChatRoomRepository.save(userChatRoom);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("방이 생성되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //방 입장
    @Transactional
    public ResponseEntity<?> enterChatRoom(UserPrincipal userPrincipal, EnterRoomReq enterRoomReq) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "올바르지 않은 유저입니다.");

        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(enterRoomReq.getId());
        DefaultAssert.isTrue(chatRoom.isPresent(), "올바르지 않은 채팅방입니다.");

        chatRoom.get().updateCurrentParticipant(chatRoom.get().getCurrentParticipant() + 1);
        DefaultAssert.isTrue(!chatRoom.get().isFull(), "채팅방 정원이 가득 찼습니다.");

        //이미 입장해있는 경우 예외처리
        Optional<UserChatRoom> test = userChatRoomRepository.findUserChatRoomByUserAndChatRoom(user.get(), chatRoom.get());
        DefaultAssert.isTrue(test.isEmpty(), "이미 입장해있는 채팅방입니다.");

        UserChatRoom userChatRoom = UserChatRoom.builder()
                .user(user.get())
                .chatRoom(chatRoom.get())
                .build();

        userChatRoomRepository.save(userChatRoom);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("방에 입장되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //방 퇴장
    @Transactional
    public ResponseEntity<?> exitChatRoom(UserPrincipal userPrincipal, Long roomId) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
        DefaultAssert.isTrue(chatRoom.isPresent(), "올바르지 않은 채팅방 ID입니다.");

        Optional<UserChatRoom> userChatRoom = userChatRoomRepository.findUserChatRoomByUserAndChatRoom(user.get(), chatRoom.get());
        DefaultAssert.isTrue(userChatRoom.isPresent(), "유저채팅방이 올바르지 않습니다.");

        userChatRoomRepository.delete(userChatRoom.get());

        chatRoom.get().updateCurrentParticipant(chatRoom.get().getCurrentParticipant() - 1);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("방을 퇴장했습니다.").build())
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
                        .hostName(chatRoom.getHostName())
                        .open(chatRoom.getOpenTime())
                        .close(chatRoom.getCloseTime())
                        .current(chatRoom.getCurrentParticipant())
                        .max(chatRoom.getMaxParticipant())
                        .categories(chatRoom.getCategories())
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomListRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //방 검색 -> 호스트 이름과 메뉴 이름으로 검색할 수 있음.
    public ResponseEntity<?> findChatRoomByMenuAndHost(UserPrincipal userPrincipal, String search) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        List<ChatRoom> listByhost = chatRoomRepository.findAllByHostName(search);

        List<ChatRoom> listByName = chatRoomRepository.findAllByName(search);

        List<ChatRoom> chatRoomList = Stream.of(listByhost, listByName)
                .flatMap(Collection::stream)
                .toList();

        List<RoomListRes> roomListRes = chatRoomList.stream().map(
                chatRoom -> RoomListRes.builder()
                        .id(chatRoom.getId())
                        .name(chatRoom.getName())
                        .hostName(chatRoom.getHostName())
                        .open(chatRoom.getOpenTime())
                        .close(chatRoom.getCloseTime())
                        .current(chatRoom.getCurrentParticipant())
                        .max(chatRoom.getMaxParticipant())
                        .categories(chatRoom.getCategories())
                        .build()
        ).toList();

        ResultRoomListRes resultRoomListRes = ResultRoomListRes.builder()
                .searchWord(search)
                .data(roomListRes)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(resultRoomListRes)
                .build();

        return ResponseEntity.ok(apiResponse);

    }

    // JSON 문자열 -> EnumSet 직렬화 메소드
    public EnumSet<Category> jsonToEnumSet(String[] categories){

        EnumSet<Category> temp = EnumSet.noneOf(Category.class); // 빈 EnumSet 생성

        for (String category : categories) {
            try {
                Category enumValue = Category.valueOf(category); // 문자열을 해당 Enum 값으로 변환
                temp.add(enumValue); // EnumSet에 추가
            } catch (IllegalArgumentException e) {
                // 잘못된 값이라면 예외 처리 또는 무시할 수 있습니다.
            }
        }
        return temp;
    }
}
