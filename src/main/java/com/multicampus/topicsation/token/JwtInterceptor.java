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

        // 토큰 받기
        System.out.println("preHandle 실행!!!!!!!!!");
        String accessToken = jwtUtils.getAccessToken(request);
        String refreshToken = jwtUtils.getRefreshToken(request);
        System.out.println("Interceptor accessToken : " + accessToken);
        System.out.println("Interceptor refreshToken : " + refreshToken);
        // 로깅용 URI
        String requestURI = request.getRequestURI();

        // 비회원일 때
        if(accessToken == null && refreshToken == null){
            logger.debug("비회원 유저입니다 URI: {}", requestURI);
            return true;
        }else if(accessToken == null){ // 액세스 토큰 만료되었을 때
            logger.debug("액세스토큰만 만료되었습니다.");
            // 리프래쉬 토큰 이용해서 액세스 토큰 재발급
            if(StringUtils.hasText(refreshToken) && jwtUtils.validateToken(refreshToken)) {
                logger.debug("유효한 refresh 토큰입니다. URI: {}", requestURI);

                String roles = jwtUtils.getRole(refreshToken);
                String userid = jwtUtils.getId(refreshToken);

                String newAccessToken = jwtUtils.createAccessToken(roles, userid);

                //Header에 access token 정보 담아서 응답
                response.setHeader("Authorization", "Bearer " + newAccessToken);
            } else {
                // 리프래쉬 토큰 유효하지 않을 때
                logger.debug("유효하지 않은 refresh 토큰입니다. uri: {}", requestURI);
                return false;
            }
        }else{ // 액세스, 리프래쉬 토큰 둘 다 있을 때
            logger.debug("access, refresh 토큰 둘 다 존재합니다.");
            if(jwtUtils.validateToken(accessToken)) {
                //accesstoke이 유효할 때
                logger.debug("유효한 access 토큰 정보입니다. URI: {}", requestURI);
            } else {
                //refreshtoken 검증 수행
                logger.debug("유효하지 않은 access 토큰 정보입니다. refresh 토큰 검증이 필요합니다. URI: {}", requestURI);
                //accesstoken이 유효하지 않지만, Refreshtoken이 유효할 때 -> accesstoken 재발급 필요.
                if(StringUtils.hasText(refreshToken) && jwtUtils.validateToken(refreshToken)) {
                    logger.debug("유효한 refresh 토큰 정보입니다. URI: {}", requestURI);

                    String roles = jwtUtils.getRole(refreshToken);
                    String userid = jwtUtils.getId(refreshToken);

                    String newAccessToken = jwtUtils.createAccessToken(roles, userid);

                    //Header에 accesstoken 정보 담아서 응답
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                } else {
                    //둘 다 유효하지 않을 때
                    logger.debug("유효하지 않은 JWT 토큰입니다. uri: {}", requestURI);
                    return false;
                }
            }
        }
        return true;
    }

}
