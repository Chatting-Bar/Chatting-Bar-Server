package com.chatbar.domain.auth.application;

import com.chatbar.domain.user.domain.Provider;
import com.chatbar.domain.user.domain.Role;
import com.chatbar.domain.user.domain.User;
import com.chatbar.domain.user.domain.repository.UserRepository;
import com.chatbar.global.DefaultAssert;
import com.chatbar.global.config.security.oauth.OAuth2UserInfo;
import com.chatbar.global.config.security.oauth.OAuth2UserInfoFactory;
import com.chatbar.global.config.security.token.UserPrincipal;
import com.chatbar.global.error.DefaultAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomDefaultOAuth2UserService extends DefaultOAuth2UserService{

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (Exception e) {
            try {
                DefaultAssert.isAuthentication(e.getMessage());
            } catch (DefaultAuthenticationException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws DefaultAuthenticationException {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        DefaultAssert.isAuthentication(!oAuth2UserInfo.getEmail().isEmpty());

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            DefaultAssert.isAuthentication(user.getProvider().equals(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId())));
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .provider(Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .providerId(oAuth2UserInfo.getId())
                .nickname(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .profileImg(oAuth2UserInfo.getImageUrl())
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    private User updateExistingUser(User user, OAuth2UserInfo oAuth2UserInfo) {

        user.updateNickname(oAuth2UserInfo.getName());
        user.updateProfileImg(oAuth2UserInfo.getImageUrl());

        return userRepository.save(user);
    }
}
