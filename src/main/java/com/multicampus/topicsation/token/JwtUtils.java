package com.multicampus.topicsation.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //accessToken 만료시간 설정
    public final static long ACCESS_TOKEN_VALIDATION_SECOND = 1000L * 60 * 60; //1시간
    //RefreshToken 만료시간 설정
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 60 * 24; //1일

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public String createAccessToken(String roles, String userid) {

        System.out.println("createAccessToken");

        //토큰 만료 시간 설정(access token)
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALIDATION_SECOND);

        //accessToken 생성 - 나중에 바로 RETURN값에 넣어주기
        String accessToken = Jwts.builder()
                .setSubject(userid)
                .claim("roles", roles)
                .setIssuedAt(now) //토큰발행일자
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();

        System.out.println(accessToken);

        return accessToken;
    }

    public String createRefreshToken(String roles, String userid) {

        System.out.println("createRefreshToken");

        //토큰 만료 시간 설정(refresh token)
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_VALIDATION_SECOND);

        //refreshToken 생성
        String refreshToken = Jwts.builder()
                .setSubject(userid)
                .claim("roles", roles)
                .setIssuedAt(now) //토큰발행일자
                .setId(UUID.randomUUID().toString())
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();

        System.out.println(refreshToken);

        return refreshToken;
    }

    //토큰 유효성 검증 수행
    public boolean validateToken(String token) {

        System.out.println("validateToken");

        //토큰 파싱 후 발생하는 예외를 캐치하여 문제가 있으면 false, 정상이면 true 반환
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        }
        catch(io.jsonwebtoken.security.SignatureException e) {
            logger.info("잘못된 토큰 서명입니다.");
        }catch(ExpiredJwtException e) {
            logger.info("만료된 토큰입니다.");
        }catch(IllegalArgumentException | MalformedJwtException e) {
            logger.info("잘못된 토큰입니다.");
        }return false;
    }

    public String getRole(String token) {
        System.out.println("getRoles");

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJwt(token).getBody().get("roles").toString();
    }

    public String getId(String token) {
        System.out.println("getId");

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJwt(token).getBody().getSubject();
    }

    //request Header에서 access토큰 정보를 꺼내오기
    public String getAccessToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        System.out.println(bearerToken);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            System.out.println("token : "+bearerToken);

            return bearerToken.substring(7);
        }
        return null;
    }

    //reaquest Header에서 refresh토큰 정보 꺼내오기
    public String getRefreshToken(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // role에 따라서 페이지 이동을 다르게 하는 메서드
    public String authByRole(HttpServletRequest httpServletRequest ,String notLoginURI, String tuteeURI, String tutorURI, String adminURI){
        String token = getAccessToken(httpServletRequest);
        if (token == null){
            return notLoginURI;
        }else if(getRole(token).equals("tutee")){
            return tuteeURI;
        }else if(getRole(token).equals("tutor")){
            return tutorURI;
        }else if(getRole(token).equals("admin")){
            return adminURI;
        }
        return null;
    }

}
