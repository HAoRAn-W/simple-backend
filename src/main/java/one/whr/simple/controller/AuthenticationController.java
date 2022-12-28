package one.whr.simple.controller;

import one.whr.simple.dto.Request.LoginRequest;
import one.whr.simple.dto.Request.SignupRequest;
import one.whr.simple.dto.Response.MessageResponse;
import one.whr.simple.dto.Response.UserInfoResponse;
import one.whr.simple.entity.Role;
import one.whr.simple.entity.User;
import one.whr.simple.repository.RoleRepository;
import one.whr.simple.repository.UserRepository;
import one.whr.simple.security.UserDetailsImpl;
import one.whr.simple.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));

    }

    @PostMapping("/signup")
    public ResponseEntity<?> userSignup(@Valid @RequestBody SignupRequest signupRequest) {
        // check username exists or not
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return  ResponseEntity.badRequest().body(new MessageResponse("USER_EXIST", "user exists!"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return  ResponseEntity.badRequest().body(new MessageResponse("EMAIL_EXIST", "email exists!"));
        }

        User user = new User(signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getEmail(), signupRequest.getWebsite(), signupRequest.getDescription());

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new RuntimeException("ERROR: Cannot find role"));
        roles.add(role);

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("SIGNUP_SUCCESSFUL", "registered successfully!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> userLogout() {
        ResponseCookie cookie = jwtUtils.generateEmptyCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(new MessageResponse("LOGGED_OUT", "logged out"));
    }

}
