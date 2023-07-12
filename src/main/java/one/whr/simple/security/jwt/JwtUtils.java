package one.whr.simple.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import one.whr.simple.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Util class for JWT related functions
 */
@Component
@Slf4j
public class JwtUtils {

    @Value("${whr.app.security.jwtSecret}")
    private String jwtSecret;

    @Value("${whr.app.security.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${whr.app.security.jwtRefreshExpirationMs}")
    private int jwtRefreshExpirationMs;

    @Value("${whr.app.security.jwtCookieName}")
    private String jwtCookieName;

    @Value("${whr.app.security.jwtRefreshCookieName}")
    private String jwtRefreshCookieName;


    /**
     * generate a cookie with the generated token
     *
     * @param userDetails user detail
     * @return cookie with token
     */
    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
        String token = generateTokenByUsername(userDetails.getUsername());
        return ResponseCookie.from(jwtCookieName, token).path("/api").maxAge(jwtExpirationMs / 1000).httpOnly(true).build();
    }

    public ResponseCookie generateJwtCookie(String username) {
        String token = generateTokenByUsername(username);
        return ResponseCookie.from(jwtCookieName, token).path("/api").maxAge(jwtExpirationMs / 1000).httpOnly(true).build();
    }

    public ResponseCookie generateRefreshJwtCookie(UserDetailsImpl userDetails) {
        String refreshToken = generateRefreshTokenByUsername(userDetails.getUsername());
        return ResponseCookie.from(jwtRefreshCookieName, refreshToken).path("/api/auth/refreshtoken").maxAge(jwtRefreshExpirationMs / 1000).httpOnly(true).build();
    }

    public ResponseCookie generateRefreshJwtCookie(String username) {
        String refreshToken = generateRefreshTokenByUsername(username);
        return ResponseCookie.from(jwtRefreshCookieName, refreshToken).path("/api/auth/refreshtoken").maxAge(jwtRefreshExpirationMs / 1000).httpOnly(true).build();
    }

    /**
     * generate an empty cookie
     *
     * @return cookie
     */
    public ResponseCookie generateEmptyCookie() {
        return ResponseCookie.from(jwtCookieName, null).path("/api").build();
    }

    public ResponseCookie generateEmptyRefreshCookie() {
        return ResponseCookie.from(jwtRefreshCookieName, null).path("/api/auth/refreshtoken").build();
    }

    /**
     * retrieve token from http requests
     *
     * @param request http requests
     * @return token
     */
    public String getJwtTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            log.warn("Empty access token from cookie");
            return null;
        }
    }

    public String getRefreshJwtTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtRefreshCookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            log.warn("Empty refresh token from cookie");
            return null;
        }
    }

    /**
     * verify the token's cryptographic signature
     *
     * @param token JWT token
     * @return if the token is valid
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token already expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is illegal: {}", e.getMessage());
        }
        return false;
    }

    public boolean validateJwtRefreshToken(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(refreshToken);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT refreshToken already expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT refreshToken is unsupported: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT refreshToken: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is illegal: {}", e.getMessage());
        }
        return false;
    }

    /**
     * retrieve username from a token
     *
     * @param token token from the request
     * @return username
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * generate a JWT token using username
     *
     * @param username user's name (unique)
     * @return JWT token
     */
    private String generateTokenByUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(getKey()).compact();
    }

    private String generateRefreshTokenByUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtRefreshExpirationMs)).signWith(getKey()).compact();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

}
