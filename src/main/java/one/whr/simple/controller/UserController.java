package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.UpdateUserInfoRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.PostPageResponse;
import one.whr.simple.dto.response.UserInfoResponse;
import one.whr.simple.entity.Avatar;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.User;
import one.whr.simple.entity.projection.PostProjection;
import one.whr.simple.exceptions.AvatarNotFoundException;
import one.whr.simple.exceptions.PostNotFoundException;
import one.whr.simple.exceptions.UserNotFoundException;
import one.whr.simple.security.jwt.JwtUtils;
import one.whr.simple.security.services.UserDetailsImpl;
import one.whr.simple.service.AvatarService;
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
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    AvatarService avatarService;

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
    ResponseEntity<?> updateUserInfo(HttpServletRequest request, @RequestBody UpdateUserInfoRequest body) throws UserNotFoundException, AvatarNotFoundException {
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);

        User user = userService.findByUsername(username);
        user.setUsername(body.getUsername());
        user.setEmail(body.getEmail());
        Long avatarId = body.getAvatarId();
        Avatar avatar;
        try {
            avatar = avatarService.findById(avatarId);
        } catch (AvatarNotFoundException e) {
            avatar = avatarService.getDefaultAvatar();
        }
        user.setAvatar(avatar);

        userService.save(user);
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(body.getUsername());
        ResponseCookie refreshJwtCookie = jwtUtils.generateRefreshJwtCookie(body.getUsername());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshJwtCookie.toString()).body(new MessageResponse(MessageCode.SUCCESSFUL, "Change user info successfully"));
    }

    @GetMapping("/getinfo")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> getUserInfo(HttpServletRequest request) throws UserNotFoundException {
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);

        User user = userService.findByUsername(username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok().body(new UserInfoResponse(MessageCode.SUCCESSFUL, "get user info successfully", user, roles));
    }
}
