package com.multicampus.topicsation.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String accessToken = jwtUtils.getAccessToken(request);
        String requestURI = request.getRequestURI();

        // 비회원일 때
        if(accessToken == null){
            logger.debug("비회원 유저입니다 URI: {}", requestURI);
            return true;
        }

        String refreshToken = jwtUtils.getRefreshToken(request);
        System.out.println(requestURI);

        if(jwtUtils.validateToken(accessToken)) {
            //accesstoke이 유효할 때
            logger.debug("유효한 access 토큰 정보입니다. URI: {}", requestURI);
        } else {
            //accesstoken이 유효하지 않을 때 refreshtoken 검증 수행
            logger.debug("유효하지 않은 access 토큰 정보입니다. refresh 토큰 검증이 필요합니다. URI: {}", requestURI);
            //accesstoken이 유효하지 않지만, Refreshtoken이 유효할 때 -> accesstoken 재발급 필요.
            if(StringUtils.hasText(refreshToken) && jwtUtils.validateToken(refreshToken)) {
                logger.debug("유효한 refresh 토큰 정보입니다. URI: {}", requestURI);

                String roles = jwtUtils.getRole(refreshToken);
                String userid = jwtUtils.getId(refreshToken);

                jwtUtils.createAccessToken(roles, userid);
            } else {
                //둘 다 유효하지 않을 때
                logger.debug("유효하지 않은 JWT 토큰입니다. uri: {}", requestURI);
                return false;
            }
        }

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);

        //Header에 accesstoken 정보 담아서 응답
        response.setHeader("Authorization", "Bearer " + accessToken);
        //Cookie에 refreshtoken 정보 담아서 응답
        response.addCookie(cookie);
        return true;
    }

}
