package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.response.FavoriteListResponse;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.User;
import one.whr.simple.entity.projection.PostProjection;
import one.whr.simple.entity.projection.UserProjection;
import one.whr.simple.exceptions.PostNotFoundException;
import one.whr.simple.exceptions.UserNotFoundException;
import one.whr.simple.security.jwt.JwtUtils;
import one.whr.simple.service.PostService;
import one.whr.simple.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

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

    @GetMapping("/favorite/{postId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addFavorite(HttpServletRequest request, @PathVariable Long postId) {
        Post post;
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);
        try {
            post = postService.getPost(postId);
            User user = userService.findByUsername(username);
            user.getFavorites().add(post);
            userService.save(user);
        } catch (PostNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.POST_NOT_FOUND, "Can't find post"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.USER_NOTFOUND, "Can't find user"));
        }
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Favorite post saved"));
    }

    @GetMapping("/favorite/all")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> getFavoriteList(HttpServletRequest request) {
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);
        Set<PostProjection> favorites;
        UserProjection user = userService.findBy(username);
        if (user != null) {
            favorites = user.getFavorites();
        } else {
            return ResponseEntity.ok().body(new FavoriteListResponse(MessageCode.USER_NOTFOUND, "Can't find user", null));
        }

        return ResponseEntity.ok().body(new FavoriteListResponse(MessageCode.SUCCESSFUL, "Get favorite list successfully", favorites));
    }
}
