package one.whr.simple.controller;

import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.entity.User;
import one.whr.simple.exceptions.UserNotFoundException;
import one.whr.simple.repository.UserRepository;
import one.whr.simple.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class RefreshController {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/api/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws UserNotFoundException {
        String refreshToken = jwtUtils.getRefreshJwtTokenFromCookie(request);
        if (refreshToken != null && refreshToken.length() > 0) {
            if (jwtUtils.validateJwtRefreshToken(refreshToken)) {
                String username = jwtUtils.getUsernameFromToken(refreshToken);
                User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("user not found"));
                ResponseCookie newAccessToken = jwtUtils.generateJwtCookie(user.getUsername());
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, newAccessToken.toString())
                        .body(new MessageResponse(MessageCode.ACCESS_TOKEN_RENEWED, "Access token renewed"));
            }
        }

        return ResponseEntity.ok().body(new MessageResponse(MessageCode.REFRESH_TOKEN_EXPIRED, "Refresh token expired"));
    }
}
