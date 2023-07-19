package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.EnumRole;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.LoginRequest;
import one.whr.simple.dto.request.SignupRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.UserInfoResponse;
import one.whr.simple.entity.Role;
import one.whr.simple.entity.User;
import one.whr.simple.repository.RoleRepository;
import one.whr.simple.repository.UserRepository;
import one.whr.simple.security.services.UserDetailsImpl;
import one.whr.simple.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@Slf4j
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
            ResponseCookie refreshJwtCookie = jwtUtils.generateRefreshJwtCookie(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshJwtCookie.toString())
                    .body(new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
        } catch (AuthenticationException e) {
            return ResponseEntity.ok()
                    .body(new MessageResponse(MessageCode.LOGIN_FAILED, "Failed to login. Please check username or password"));
        }

    }

    @PostMapping("signup")
    public ResponseEntity<?> userSignup(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            log.info("Username is already taken");
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.USER_EXIST, "Username is already taken"));
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            log.info("Email is already taken");
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.EMAIL_EXIST, "Email is already taken"));
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleRepository.findByName(EnumRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roleSet.add(userRole);
        user.setRoles(roleSet);

        userRepository.save(user);

        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SIGNUP_SUCCESSFUL, "Successfully signup user"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.generateEmptyCookie();
        ResponseCookie refreshCookie = jwtUtils.generateEmptyRefreshCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new MessageResponse(MessageCode.LOGGED_OUT, "You've been logged out!"));
    }

//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws UserNotFoundException {
//        String refreshToken = jwtUtils.getRefreshJwtTokenFromCookie(request);
//        if (refreshToken != null && refreshToken.length() > 0) {
//            if (jwtUtils.validateJwtRefreshToken(refreshToken)) {
//                String username = jwtUtils.getUsernameFromToken(refreshToken);
//                User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("user not found"));
//                ResponseCookie newAccessToken = jwtUtils.generateJwtCookie(user.getUsername());
//                return ResponseEntity.ok()
//                        .header(HttpHeaders.SET_COOKIE, newAccessToken.toString())
//                        .body(new MessageResponse(MessageCode.ACCESS_TOKEN_RENEWED, "Access token renewed"));
//            }
//        }
//
//        return ResponseEntity.ok().body(new MessageResponse(MessageCode.REFRESH_TOKEN_EXPIRED, "Refresh token expired"));
//    }
}
