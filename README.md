<p align="center">
  <img src="https://avatars.githubusercontent.com/u/129783133?s=200&v=4" width="300"/>
</p>
<h1 align="center"> 🏮 채팅마차 🏮 </h1>
</br>
<p align="middle">텍스트 기반 스트리밍 어플리케이션, 채팅마차입니다!</p>
</br>

<br>

## 팀원  👨‍👨‍👧‍👧👩‍👦‍
<table>
  <tbody>
    <tr>
      <td align="center"><a href=https://github.com/choeun7><img src="https://avatars.githubusercontent.com/u/95676587?v=4" width="150px;" alt=""/><br /><sub><b>BE 팀장 : 신초은</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/ZhongdanBae"><img src="https://avatars.githubusercontent.com/u/128568951?v=4" width="150px;" alt=""/><br /><sub><b>BE 팀원 : 배종찬</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/gyehwan24"><img src="https://avatars.githubusercontent.com/u/97265630?v=4" width="150px;" alt=""/><br /><sub><b>BE 팀원 : 백계환</b></sub></a><br /></td>
    </tr>
  </tbody>
</table>

<br>

<div align=center><h1>📚 STACKS</h1></div>

<div align=center> 
  <h2> Tech </h2>
  <img src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white"> 
  <img src="https://img.shields.io/badge/android-3DDC84?style=for-the-badge&logo=android&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white">
  <img src="https://img.shields.io/badge/Spring Data Jpa-0078D4?style=for-the-badge&logo=&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Security-6DB33F ?style=for-the-badge&logo=SpringSecurity&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens">


  <!--DB-->
  <img src="https://img.shields.io/badge/firebase-%23039BE5.svg?style=for-the-badge&logo=firebase">
  <img src="https://img.shields.io/badge/h2-2962FF?style=for-the-badge&logo=h2&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  
  <h2> Infra </h2>
  <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white">
  <img src="https://img.shields.io/badge/amazonec2-FF9900.svg?style=for-the-badge&logo=amazonec2&logoColor=white">
  <img src="https://img.shields.io/badge/amazonrds-527FFF.svg?style=for-the-badge&logo=amazonrds&logoColor=white">
  <img src="https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black">

  
  <h2> Tool </h2>
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/androidstudio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white">
  <img src="https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white">
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
  <img src="https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white">
  <img src="https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white">
  <img src="https://img.shields.io/badge/Discord-%235865F2.svg?style=for-the-badge&logo=discord&logoColor=white">
</div>
<br>
<br>

<h2>📀 ERD </h2>
<img align=center width="590" alt="ERD" src="https://github.com/Chatting-Bar/Chatting-Bar-Server/assets/97265630/31f2564d-def3-427f-ab3a-7661ff4b6a5c">
<br>

<h2>⭐️ API </h2>
[API 명세서](https://www.notion.so/API-5b5b7308681b4c1d8f43137d7be66ae7?pvs=4)
<br>
<br>

## Project Structure
```bash
.
├── java
│   └── com
│       └── chatbar
│           ├── ChatbarApplication.java
│           ├── domain
│           │   ├── auth
│           │   │   ├── application
│           │   │   │   ├── AuthService.java
│           │   │   │   ├── CustomDefaultOAuth2UserService.java
│           │   │   │   ├── CustomTokenProviderService.java
│           │   │   │   └── CustomUserDetailsService.java
│           │   │   ├── domain
│           │   │   │   ├── Token.java
│           │   │   │   └── repository
│           │   │   │       ├── CustomAuthorizationRequestRepository.java
│           │   │   │       └── TokenRepository.java
│           │   │   ├── dto
│           │   │   │   ├── AuthRes.java
│           │   │   │   ├── RefreshTokenReq.java
│           │   │   │   ├── SignInReq.java
│           │   │   │   ├── SignUpReq.java
│           │   │   │   └── TokenMapping.java
│           │   │   └── presentation
│           │   │       └── AuthController.java
│           │   ├── chatroom
│           │   │   ├── application
│           │   │   │   └── ChatRoomService.java
│           │   │   ├── domain
│           │   │   │   ├── ChatRoom.java
│           │   │   │   ├── UserChatRoom.java
│           │   │   │   └── repository
│           │   │   │       ├── ChatRoomRepository.java
│           │   │   │       └── UserChatRoomRepository.java
│           │   │   ├── dto
│           │   │   │   ├── CloseRoomReq.java
│           │   │   │   ├── CreateRoomReq.java
│           │   │   │   ├── CreateRoomRes.java
│           │   │   │   ├── EnterRoomReq.java
│           │   │   │   ├── ResultRoomListRes.java
│           │   │   │   ├── RoomListRes.java
│           │   │   │   └── UserListRes.java
│           │   │   └── presentation
│           │   │       └── ChatRoomController.java
│           │   ├── common
│           │   │   ├── BaseEntity.java
│           │   │   ├── Category.java
│           │   │   ├── CategorySetConverter.java
│           │   │   └── Status.java
│           │   ├── email
│           │   │   ├── EmailService.java
│           │   │   └── VerificationCode.java
│           │   ├── message
│           │   │   ├── application
│           │   │   │   └── MessageService.java
│           │   │   ├── domain
│           │   │   │   ├── Message.java
│           │   │   │   └── repository
│           │   │   │       └── MessageRepository.java
│           │   │   ├── dto
│           │   │   │   └── MessageDto.java
│           │   │   └── presentation
│           │   │       └── MessageController.java
│           │   └── user
│           │       ├── application
│           │       │   ├── FollowService.java
│           │       │   └── UserService.java
│           │       ├── domain
│           │       │   ├── Follow.java
│           │       │   ├── Provider.java
│           │       │   ├── Role.java
│           │       │   ├── User.java
│           │       │   └── repository
│           │       │       ├── FollowRepository.java
│           │       │       └── UserRepository.java
│           │       ├── dto
│           │       │   ├── ChangePasswordRes.java
│           │       │   ├── EmailRes.java
│           │       │   ├── FollowRes.java
│           │       │   ├── UserRes.java
│           │       │   └── VerifyRes.java
│           │       └── presentation
│           │           └── UserController.java
│           └── global
│               ├── DefaultAssert.java
│               ├── config
│               │   ├── JpaConfig.java
│               │   ├── S3Config.java
│               │   ├── TomcatWebCustomConfig.java
│               │   ├── YamlPropertySourceFactory.java
│               │   └── security
│               │       ├── OAuth2Config.java
│               │       ├── SecurityConfig.java
│               │       ├── WebMvcConfig.java
│               │       ├── handler
│               │       │   ├── CustomSimpleUrlAuthenticationFailureHandler.java
│               │       │   └── CustomSimpleUrlAuthenticationSuccessHandler.java
│               │       ├── oauth
│               │       │   ├── OAuth2UserInfo.java
│               │       │   ├── OAuth2UserInfoFactory.java
│               │       │   └── company
│               │       │       ├── Google.java
│               │       │       └── Naver.java
│               │       ├── token
│               │       │   ├── CurrentUser.java
│               │       │   ├── CustomAuthenticationEntryPoint.java
│               │       │   ├── CustomOncePerRequestFilter.java
│               │       │   └── UserPrincipal.java
│               │       └── util
│               │           └── CustomCookie.java
│               ├── error
│               │   ├── ApiControllerAdvice.java
│               │   ├── DefaultAuthenticationException.java
│               │   ├── DefaultException.java
│               │   ├── DefaultNullPointerException.java
│               │   └── InvalidParameterException.java
│               ├── infrastructure
│               │   └── S3Uploader.java
│               └── payload
│                   ├── ApiResponse.java
│                   ├── ErrorCode.java
│                   ├── ErrorResponse.java
│                   └── Message.java
└── resources
    ├── application.yml
    ├── database
    │   └── application-database.yml
    └── oauth
        └── application-oauth.yml
```

## 🌱 Pull Requests Rule 
**Title**: ex) [Feat]: 채팅 기능 추가 , [FIX]: 오류 수정

**Reviewers(PR을 리뷰해 줄 팀원)**: 파트 내 본인 외 1명

**Assignees(PR 담당자)**: 본인

**Labels**: Commit Message Convention에 따름

<br>

## ✅ Commit Message Convention

[FEAT]: 새로운 기능 추가

[FIX]: 버그, 오류 수정

[DOCS]: README 등의 문서 수정

[REFACTOR]: 전면 수정(코드 리펙토링)

[TEST]: 테스트 코드 추가 및 수정

<br>

## 🍃 Branch Strategy
- main
    - 배포 이력 관리 목적
- develop
    - feature 병합용 브랜치
    - 배포 전 병합 브랜치
- feature
    - develop 브랜치를 베이스로 기능별로 feature 브랜치 생성해 개발
- fix
    - 수정용 브랜치
- test
    - 테스트가 필요한 코드용 브랜치
- hotfix
    - 배포 후 버그 발생 시 버그 수정 
<br>

