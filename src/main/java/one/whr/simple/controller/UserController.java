package one.whr.simple.controller;

import lombok.extern.slf4j.Slf4j;
import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.PostPageResponse;
import one.whr.simple.entity.Post;
import one.whr.simple.entity.User;
import one.whr.simple.entity.projection.PostProjection;
import one.whr.simple.exceptions.PostNotFoundException;
import one.whr.simple.exceptions.UserNotFoundException;
import one.whr.simple.security.jwt.JwtUtils;
import one.whr.simple.service.PostService;
import one.whr.simple.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("/favorite/remove/{postId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> removeFavorite(HttpServletRequest request, @PathVariable Long postId) {
        Post post;
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);
        try {
            post = postService.getPost(postId);
            User user = userService.findByUsername(username);
            user.getFavorites().remove(post);
            userService.save(user);
        } catch (PostNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.POST_NOT_FOUND, "Can't find post"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.USER_NOTFOUND, "Can't find user"));
        }
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Favorite post removed"));
    }

    @GetMapping("/favorite/{postId}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    ResponseEntity<?> queryFavorite(HttpServletRequest request, @PathVariable Long postId) {
        String token = jwtUtils.getJwtTokenFromCookie(request);
        String username = jwtUtils.getUsernameFromToken(token);
        try {
            Post post = postService.getPost(postId);
            User user = userService.findByUsername(username);
            if (!user.getFavorites().contains(post)) {
                return ResponseEntity.ok().body(new MessageResponse(MessageCode.NOT_IN_FAVORITE, "Not in favorite"));
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.USER_NOTFOUND, "Can't find user"));
        } catch (PostNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.POST_NOT_FOUND, "Can't find post"));
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
}
