package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.UpdateUserInfoRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.PostPageResponse;
import one.whr.simple.dto.response.UserInfoResponse;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.User;
import one.whr.simple.entity.projection.PostProjection;
import one.whr.simple.exceptions.PostNotFoundException;
import one.whr.simple.exceptions.UserNotFoundException;
import one.whr.simple.security.jwt.JwtUtils;
import one.whr.simple.security.services.UserDetailsImpl;
import one.whr.simple.service.PostService;
import one.whr.simple.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@Slf4j
public class UserController {
    @Autowired
    PostService postService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @GetMapping("/favorite/add/{postId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addFavorite(HttpServletRequest request, @PathVariable Long postId) throws PostNotFoundException, UserNotFoundException {
        Post post;
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);

        post = postService.getPost(postId);
        User user = userService.findByUsername(username);
        user.getFavorites().add(post);
        userService.save(user);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Favorite post saved"));
    }

    @GetMapping("/favorite/remove/{postId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> removeFavorite(HttpServletRequest request, @PathVariable Long postId) throws PostNotFoundException, UserNotFoundException {
        Post post;
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);

        post = postService.getPost(postId);
        User user = userService.findByUsername(username);
        user.getFavorites().remove(post);
        userService.save(user);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Favorite post removed"));
    }

    @GetMapping("/favorite/{postId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> queryFavorite(HttpServletRequest request, @PathVariable Long postId) throws PostNotFoundException, UserNotFoundException {
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);
        Post post = postService.getPost(postId);
        User user = userService.findByUsername(username);
        if (!user.getFavorites().contains(post)) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.NOT_IN_FAVORITE, "Not in favorite"));
        }
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.IS_IN_FAVORITE, "In favorite"));
    }

    @GetMapping("/favorite")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> getFavoriteList(HttpServletRequest request, @RequestParam int pageNo, @RequestParam(defaultValue = "6") int pageSize) {
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);
        Page<PostProjection> favoritePage = postService.getPaginatedPostsByUserFavorite(username, pageNo, pageSize);

        return ResponseEntity.ok().body(new PostPageResponse(MessageCode.SUCCESSFUL, "Page query successful", favoritePage.toList(), favoritePage.getTotalPages()));
    }

    @PostMapping("/updateinfo")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> updateUserInfo(HttpServletRequest request, @RequestBody UpdateUserInfoRequest body) throws UserNotFoundException {
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);

        User user = userService.findByUsername(username);
        user.setUsername(body.getUsername());
        user.setEmail(body.getEmail());
        user.setAvatarId(body.getAvatarId());
        userService.save(user);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(body.getUsername());
        ResponseCookie refreshJwtCookie = jwtUtils.generateRefreshJwtCookie(body.getUsername());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshJwtCookie.toString()).body(new MessageResponse(MessageCode.SUCCESSFUL, "Change user avatar successfully"));
    }

    @GetMapping("/getinfo")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> getUserInfo(HttpServletRequest request) throws UserNotFoundException {
        String token = jwtUtils.getJwtTokenFromCookie(request);  // not working
        String username = jwtUtils.getUsernameFromToken(token);

        User user = userService.findByUsername(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok().body(new UserInfoResponse(MessageCode.SUCCESSFUL, "get user info successfully", user, roles));
    }
}
