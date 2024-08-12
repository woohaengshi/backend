package com.woohaengshi.backend.service.auth;

import com.woohaengshi.backend.exception.WoohaengshiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static com.woohaengshi.backend.exception.ErrorCode.*;

@Component
public class JwtTokenProvider {

    private static final String MEMBER_ID = "memberId";
    private static final String ISSUER = "woohaengshi";
    private static final String TOKEN_TYPE = "Bearer";

    private SecretKey key;
    private Long accessExpiration;
    private Long refreshExpiration;

    @Autowired
    public JwtTokenProvider(@Value("${security.jwt.key}") String key,
                            @Value("${security.jwt.expiration.access}") Long accessExpiration,
                            @Value("${security.jwt.expiration.refresh}") Long refreshExpiration) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(key));
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    private String createToken(Long memberId, Long expiration) {
        return Jwts.builder()
                .claim(MEMBER_ID, memberId)
                .signWith(key)
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    public String createAccessToken(Long memberId) {
        return createToken(memberId, accessExpiration);
    }

    public String createRefreshToken(Long memberId) {
        return createToken(memberId, refreshExpiration);
    }

    public void validToken(String token) {
        try {
            getClaimsJwt(token);
        } catch (MissingClaimException e) {
            throw new WoohaengshiException(MISSING_ISSUER_TOKEN);
        } catch (IncorrectClaimException e) {
            throw new WoohaengshiException(NOT_WOOHAENGSHI_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new WoohaengshiException(EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new WoohaengshiException(UNSUPPORTED_TOKEN);
        } catch (SignatureException e) {
            throw new WoohaengshiException(FAILED_SIGNATURE_TOKEN);
        } catch (MalformedJwtException e) {
            throw new WoohaengshiException(INCORRECTLY_CONSTRUCTED_TOKEN);
        }
    }

    public Jws<Claims> getClaimsJwt(String token) {
        return Jwts.parser().verifyWith(key).requireIssuer(ISSUER).build().parseClaimsJws(token);
    }

    public Long getMemberId(String token) {
        try {
            return getClaimsJwt(token).getPayload().get(MEMBER_ID, Long.class);
        } catch (RequiredTypeException e) {
            throw new WoohaengshiException(INVALID_CLAIM_TYPE);
        }
    }

    public Date getExpiredAt(String accessToken) {
        return getClaimsJwt(accessToken).getBody().getExpiration();
    }

    public String extractAccessToken(String authorization) {
        String[] tokenFormat = authorization.split(" ");
        if (tokenFormat.length != 2 && !tokenFormat[0].equals(TOKEN_TYPE)) {
            throw new WoohaengshiException(INCORRECT_CONSTRUCT_HEADER);
        }
        return tokenFormat[1];
    }
}
