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
import com.chatbar.global.error.DefaultException;
import com.chatbar.global.payload.ApiResponse;
import com.chatbar.global.payload.ErrorCode;
import com.chatbar.global.payload.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

        //오픈시간이 마감시간보다 늦는 경우 예외처리
        DefaultAssert.isTrue(!createRoomReq.getOpenTime().isAfter(createRoomReq.getCloseTime()),"시작시간이 마감시간 이후입니다.");

        //이미 지나간 시간에 방을 만들려고 하는 경우 예외처리
        DefaultAssert.isTrue(!LocalDateTime.now().isAfter(createRoomReq.getOpenTime()),"현재시간 이후부터 채팅방 오픈이 가능합니다.");

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
                .userRole(UserChatRoom.Role.valueOf("HOST")) // 방 생성 시 유저는 방장임
                .isFrozen(false)
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

        //영업시간이 아닌 경우 예외처리
        LocalDateTime currentTime = LocalDateTime.now();
        DefaultAssert.isTrue(!currentTime.isBefore(chatRoom.get().getOpenTime()),"채팅방 오픈시간 전입니다.");
        DefaultAssert.isTrue(!currentTime.isAfter(chatRoom.get().getCloseTime()),"채팅방 마감시간 후입니다.");

        UserChatRoom userChatRoom = UserChatRoom.builder()
                .user(user.get())
                .chatRoom(chatRoom.get())
                .userRole(UserChatRoom.Role.valueOf("GUEST"))  // 방 입장 시 유저는 손님임
                .isFrozen(false)
                .build();

        userChatRoomRepository.save(userChatRoom);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("방에 입장되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //방 퇴장 (본인이 직접 나가는 것)
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

    //방 강퇴 (방장만 가능)
    @Transactional
    public ResponseEntity<?> kickOutChatRoom(UserPrincipal userPrincipal, Long roomId, Long userId){

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
        DefaultAssert.isTrue(chatRoom.isPresent(),"채팅방이 올바르지 않습니다.");

        Optional<User> kickUser = userRepository.findById(userId);
        DefaultAssert.isTrue(kickUser.isPresent(), "강퇴할 유저가 올바르지 않습니다.");

        Optional<UserChatRoom> userChatRoom = userChatRoomRepository.findUserChatRoomByUserAndChatRoom(user.get(), chatRoom.get());
        DefaultAssert.isTrue(userChatRoom.isPresent(), "유저 채팅방이 올바르지 않습니다.");

        Optional<UserChatRoom> kickUserChatRoom = userChatRoomRepository.findUserChatRoomByUserAndChatRoom(kickUser.get(), chatRoom.get());
        DefaultAssert.isTrue(kickUserChatRoom.isPresent(), "강퇴 유저 채팅방이 올바르지 않습니다.");

        //유저가 방장인 경우에만 강퇴 허용
        if (userChatRoom.get().getUserRole() == UserChatRoom.Role.HOST) {
            userChatRoomRepository.delete(kickUserChatRoom.get());
        }else{
            throw new DefaultException(ErrorCode.INVALID_PARAMETER, "유저가 방장이 아닙니다.");
        }

        chatRoom.get().updateCurrentParticipant(chatRoom.get().getCurrentParticipant() - 1);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("유저를 강퇴했습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //유저 얼리기
    @Transactional
    public ResponseEntity<?> frozenUser(UserPrincipal userPrincipal, Long roomId, Long userId) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
        DefaultAssert.isTrue(chatRoom.isPresent(),"채팅방이 올바르지 않습니다.");

        Optional<User> kickUser = userRepository.findById(userId);
        DefaultAssert.isTrue(kickUser.isPresent(), "얼릴 유저가 올바르지 않습니다.");

        Optional<UserChatRoom> userChatRoom = userChatRoomRepository.findUserChatRoomByUserAndChatRoom(user.get(), chatRoom.get());
        DefaultAssert.isTrue(userChatRoom.isPresent(), "유저 채팅방이 올바르지 않습니다.");

        Optional<UserChatRoom> kickUserChatRoom = userChatRoomRepository.findUserChatRoomByUserAndChatRoom(kickUser.get(), chatRoom.get());
        DefaultAssert.isTrue(kickUserChatRoom.isPresent(), "얼릴 유저 채팅방이 올바르지 않습니다.");

        //유저가 방장인 경우에만 얼리기 허용
        if (userChatRoom.get().getUserRole() == UserChatRoom.Role.HOST) {
            kickUserChatRoom.get().updateIsFrozen(true);
        }else{
            throw new DefaultException(ErrorCode.INVALID_PARAMETER, "유저가 방장이 아닙니다.");
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("유저를 얼렸습니다.").build())
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
                        .categories(EnumSetToString(chatRoom.getCategories()))
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomListRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //방 조회 (최신 순)
    public ResponseEntity<?> findLatestChatRoom(UserPrincipal userPrincipal) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")); //만들어진 순 조회

        List<RoomListRes> roomListRes = chatRoomList.stream().map(
                chatRoom -> RoomListRes.builder()
                        .id(chatRoom.getId())
                        .name(chatRoom.getName())
                        .hostName(chatRoom.getHostName())
                        .open(chatRoom.getOpenTime())
                        .close(chatRoom.getCloseTime())
                        .current(chatRoom.getCurrentParticipant())
                        .max(chatRoom.getMaxParticipant())
                        .categories(EnumSetToString(chatRoom.getCategories()))
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomListRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //방 조회 (카테고리 추천순)
    public ResponseEntity<?> findRecommendedChatRoom(UserPrincipal userPrincipal) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();

        //user의 카테고리와 비교해 같은 카테고리가 많은 순  정렬
        chatRoomList.sort(Comparator.comparingInt(entity -> calculateSimilarity(user.get().getCategories(), entity.getCategories())));

        List<RoomListRes> roomListRes = chatRoomList.stream().map(
                chatRoom -> RoomListRes.builder()
                        .id(chatRoom.getId())
                        .name(chatRoom.getName())
                        .hostName(chatRoom.getHostName())
                        .open(chatRoom.getOpenTime())
                        .close(chatRoom.getCloseTime())
                        .current(chatRoom.getCurrentParticipant())
                        .max(chatRoom.getMaxParticipant())
                        .categories(EnumSetToString(chatRoom.getCategories()))
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(roomListRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //EunmSet을 String배열로 형변환
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

    //array1과 array2가 겹치는 개수 반환
    private static int calculateSimilarity(EnumSet<Category> user_categories, EnumSet<Category> room_categories) {

        String[] array1 = EnumSetToString(user_categories);
        String[] array2 = EnumSetToString(room_categories);

        int similarity = 0;
        int length = Math.min(array1.length, array2.length);

        for (int i = 0; i < length; i++) {
            if (array1[i].equals(array2[i])) {
                similarity++;
            }
        }

        return similarity;
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
                        .categories(EnumSetToString(chatRoom.getCategories()))
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