package com.multicampus.topicsation.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

//토큰 생성, 토큰 유효성 검증 등을 담당
@Component
public class TokenProvider implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

//    private static final String AUTORITIES_KEY = "auth";

    private final String secret;
//    private final long tokenValidity;

    //accessToken 만료시간 설정
    public final static long ACCESS_TOKEN_VALIDATION_SECOND = 1000L * 60;
    //RefreshToken 만료시간 설정
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 120;

    private Key key;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

//    @Autowired
//    private IMemberManageService iMemberManageService;

    //의존성 주입
    public TokenProvider(
            @Value("${jwt.secret}") String secret
    ){
        this.secret = secret;
    }


    @Override
    //Bean이 생성되고 주입을 받은 후에 secret값을 Base64로 Decode해서 변수 key에 할당
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        System.out.println(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(String username, String roles) {
        //roles이 꼭 List형식인가?

        System.out.println("createAccessToken");

        //authorities 설정
//        String authorities = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//
//        System.out.println("token authorities: "+authorities);


        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);

        System.out.println(claims.get("roles"));

        //토큰 만료 시간 설정(access token)
        Date now = new Date();
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_VALIDATION_SECOND);

        //accessToken 생성
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) //토큰발행일자
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        System.out.println(accessToken);

        return accessToken;
    }

    public String createRefreshToken() {

        //토큰 만료 시간 설정(refresh token)
        Date now = new Date();
        Date expiration = new Date(now.getTime() + REFRESH_TOKEN_VALIDATION_SECOND);

        //refreshToken 생성
        String refreshToken = Jwts.builder()
                .setIssuedAt(now) //토큰발행일자
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        System.out.println(refreshToken);

        // Redis에 refreshToken 저장 - 만료되면 유효하지 않게끔
        redisTemplate.opsForValue().set(refreshToken, "", REFRESH_TOKEN_VALIDATION_SECOND, TimeUnit.SECONDS);

//        return Jwts.builder()
//                .setSubject(authentication.getName())
//                .claim(AUTORITIES_KEY,authorities)
//                .signWith(key, SignatureAlgorithm.HS512) //jwt 알고리즘
//                .setExpiration(validity)
//                .compact();
        return refreshToken;
    }

    //토큰 정보를 이용해서 Authentication 객체 리턴
//    public Authentication getAuthentication(String token) {
//        System.out.println("getAuthentication");
//
//        //토큰을 이용해서 claim 생성
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        System.out.println(claims);
//
//        //claim을 이용해서 authorities 생성
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get(AUTORITIES_KEY).toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        //claim과 authorities 이용하여 User 객체 생성
//        User principal = new User(claims.getSubject(), "", authorities);
//        System.out.println(principal.getAuthorities());
//        System.out.println(authorities);
//
//        //최적 Authentication 객체 리턴
//        return new UsernamePasswordAuthenticationToken(principal,token,authorities);
//    }

    //refresh토큰 유효성 검사
    public boolean validateRefreshToken(String refreshToken) {
        Object value = redisTemplate.opsForValue().get(refreshToken); //redis에서 refreshToken 값 가져오기
        System.out.println("redis에서 가져온 토큰: "+(String) value);

        if(value == null) {
            //값이 없으면 유효하지 않은 것으로 판단
            System.out.println("redis에 refresh 값 없음");
            return false;
        }else {
            String redisRefreshToken = (String) value;
            if(!redisRefreshToken.equals(refreshToken)) {
                System.out.println("redis의 refreshToken 값과 요청한 refreshToken 값이 같지 않음");
                return false;
            }
            //값이 만료되지 않았는지 확인
            if(!isExpired(refreshToken)) {
                return true;
            }else{
                System.out.println("refreshToken이 만료됨");
                return false;
            }
        }
    }

    //Access토큰 유효성 검증 수행
    public boolean validateAccessToken(String Accesstoken) {

        System.out.println("validateAccessToken");

        //토큰 파싱 후 발생하는 예외를 캐치하여 문제가 있으면 false, 정상이면 true 반환
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(Accesstoken);
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

    public boolean isExpired(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();

        return expiration.before(new Date());
    }

}
