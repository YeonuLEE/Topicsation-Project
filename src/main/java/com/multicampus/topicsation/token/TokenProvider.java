package com.multicampus.topicsation.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.slf4j.Logger;

//토큰 생성, 토큰 유효성 검증 등을 담당
@Component
public class TokenProvider implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTORITIES_KEY = "auth";

    private final String secret;
    private final long tokenValidity;
    private Key key;

    //의존성 주입
    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidity
    ){
        this.secret = secret;
        this.tokenValidity = tokenValidity*1000;
    }


    @Override
    //Bean이 생성되고 주입을 받은 후에 secret값을 Base64로 Decode해서 변수 key에 할당
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        System.out.println(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //권한정보를 이용해서 token생성
    public String createToken(Authentication authentication) {

        System.out.println("createToken");

        //authorities 설정
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //토큰 만료 시간 설정
        long now = (new Date()).getTime();
        Date validity = new Date(now + this.tokenValidity);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTORITIES_KEY,authorities)
                .signWith(key, SignatureAlgorithm.HS512) //jwt 알고리즘
                .setExpiration(validity)
                .compact();
    }

    //토큰 정보를 이용해서 Authentication 객체 리턴
    public Authentication getAuthentication(String token) {
        System.out.println("getAuthentication");

        //토큰을 이용해서 claim 생성
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        //claim을 이용해서 authorities 생성
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //claim과 authorities 이용하여 User 객체 생성
        User pricipal = new User(claims.getSubject(), "", authorities);

        //최적 Authentication 객체 리턴
        return new UsernamePasswordAuthenticationToken(pricipal,token,authorities);
    }

    //토큰 유효성 검증 수행
    public boolean validateToken(String token) {

        System.out.println("validateToken");

        //토큰 파싱 후 발생하는 예외를 캐치하여 문제가 있으면 flase, 정상이면 true 반환
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.info("잘못된 토큰 서명입니다.");
        }catch(ExpiredJwtException e) {
            logger.info("만료된 토큰입니다.");
        }catch(UnsupportedJwtException e) {
            logger.info("지원되지 않는 토큰입니다.");
        }catch(IllegalArgumentException e) {
            logger.info("잘못된 토큰입니다.");
        }return false;
    }


}
