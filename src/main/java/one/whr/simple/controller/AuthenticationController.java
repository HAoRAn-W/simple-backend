package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.EnumRole;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.LoginRequest;
import one.whr.simple.dto.request.RequestPasswordRequest;
import one.whr.simple.dto.request.SignupRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.UserInfoResponse;
import one.whr.simple.entity.Role;
import one.whr.simple.entity.User;
import one.whr.simple.exceptions.InfoMismatchException;
import one.whr.simple.exceptions.UserNotFoundException;
import one.whr.simple.repository.RoleRepository;
import one.whr.simple.security.jwt.JwtUtils;
import one.whr.simple.security.services.UserDetailsImpl;
import one.whr.simple.service.UserService;
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
import org.springframework.web.bind.annotation.*;

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
    AuthenticationManager authenticationManager;

    UserService userService;

    RoleRepository roleRepository;

    PasswordEncoder encoder;

    JwtUtils jwtUtils;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequest loginRequest) throws UserNotFoundException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
            ResponseCookie refreshJwtCookie = jwtUtils.generateRefreshJwtCookie(userDetails);


            User user = userService.findByUsername(userDetails.getUsername());

            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).header(HttpHeaders.SET_COOKIE, refreshJwtCookie.toString()).body(new UserInfoResponse(MessageCode.SUCCESSFUL, "Login successful", user, roles));
        } catch (AuthenticationException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.LOGIN_FAILED, "Login failed"));
        }

    }

    @PostMapping("signup")
    public ResponseEntity<?> userSignup(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.existsUsername(signupRequest.getUsername())) {
            log.info("Username is already taken");
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.USER_EXIST, "Username is already taken"));
        }
        if (userService.existsEmail(signupRequest.getEmail())) {
            log.info("Email is already taken");
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.EMAIL_EXIST, "Email is already taken"));
        }

        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(), encoder.encode(signupRequest.getPassword()));
        Set<Role> roleSet = new HashSet<>();
        Role userRole = roleRepository.findByName(EnumRole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roleSet.add(userRole);
        user.setRoles(roleSet);

        userService.save(user);

        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Successfully signup user"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.generateEmptyCookie();
        ResponseCookie refreshCookie = jwtUtils.generateEmptyRefreshCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).header(HttpHeaders.SET_COOKIE, refreshCookie.toString()).body(new MessageResponse(MessageCode.LOGGED_OUT, "You've been logged out!"));
    }

    @PostMapping("/resetpassword")
    ResponseEntity<?> resetPassword(@RequestBody RequestPasswordRequest request) throws UserNotFoundException, InfoMismatchException {
        String username = request.getUsername();
        User user = userService.findByUsername(username);

        String email = request.getEmail();
        if (!user.getEmail().equals(email)) {
            throw new InfoMismatchException(MessageCode.EMAIL_MISMATCH, "Email is not matched");
        }
        user.setPassword(encoder.encode(request.getPassword()));
        userService.save(user);

        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Reset password success"));
    }
}
