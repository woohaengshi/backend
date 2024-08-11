package com.woohaengshi.backend.service.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String MEMBER_ID = "memberId";
    private static final String ISSUER = "woohaengshi";

    private SecretKey key;
    @Value("${security.jwt.expiration.access}")
    private Long accessExpiration;
    @Value("${security.jwt.expiration.refresh}")
    private Long refreshExpiration;

    public JwtTokenProvider(@Value("${security.jwt.key") String key) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(key));
    }

    public String createToken(Long memberId, Long expiration){
        return Jwts.builder()
                .claim(MEMBER_ID, memberId)
                .signWith(key)
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

}

