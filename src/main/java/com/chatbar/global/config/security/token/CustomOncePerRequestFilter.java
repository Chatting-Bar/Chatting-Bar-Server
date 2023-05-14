package com.chatbar.global.config.security.token;

import com.chatbar.domain.auth.application.CustomTokenProviderService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//설명: Spring이 지원하는 filter 중 OncePerRequestFilter 라는 필터가 있는데
//말 그대로 한번의 요청마다 필터링을 수행하는 클래스입니다.
//아래의 코드는 이 클래스를 상속받아 Custom 한 클래스입니다.
//Custom 목적은 유저가 요청을 보낼 때 마다 유저의 http 헤더에서 accesstoken 을 검증하기 번거롭기 때문에 아래 클래스에서 알아서 헤더에 달려있는 Authorization 의 토큰값을 추출해 검증하고
//만약 사용 가능한 accesstoken 이면 유저의 ID값을 추출해 userRepository에서 유저 정보를 가져와
//SpringSecurity의 SecurityContextHolder에 등록합니다.

@Slf4j
public class CustomOncePerRequestFilter extends OncePerRequestFilter{

    @Autowired
    private CustomTokenProviderService customTokenProviderService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 JWT를 받아온다.
        String jwt = getJwtFromRequest(request);
        //유효한 토큰인지 확인한다.
        if (StringUtils.hasText(jwt) && customTokenProviderService.validateToken(jwt)) {
            //토큰이 유효하면 토큰으로부터 유저 정보를 받아온다.
            UsernamePasswordAuthenticationToken authentication = customTokenProviderService.getAuthenticationById(jwt);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //SecurityContext에 Authentication 객체를 저장한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            log.info("bearerToken = {}", bearerToken.substring(7, bearerToken.length()));
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
    
}
