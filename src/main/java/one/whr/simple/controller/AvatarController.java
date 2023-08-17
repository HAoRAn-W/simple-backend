package one.whr.simple.controller;

import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.AddAvatarRequest;
import one.whr.simple.dto.request.UpdateAvatarRequest;
import one.whr.simple.dto.response.AvatarListResponse;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.entity.Avatar;
import one.whr.simple.exceptions.AvatarNotFoundException;
import one.whr.simple.service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avatar")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class AvatarController {

    @Autowired
    AvatarService avatarService;
    @GetMapping("/all")
    ResponseEntity<?> getAllAvatars() {
        List<Avatar> avatars = avatarService.findAll();
        return ResponseEntity.ok().body(new AvatarListResponse(MessageCode.SUCCESSFUL, "Query avatars successful", avatars));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addAvatar(@RequestBody AddAvatarRequest request) {
        Avatar avatar = new Avatar(request.getName(), request.getAvatarUrl());
        avatarService.save(avatar);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "add avatar successful"));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> updateAvatar(@RequestBody UpdateAvatarRequest request) throws AvatarNotFoundException {
        Avatar avatar = avatarService.findById(request.getAvatarId());
        avatar.setName(request.getName());
        avatar.setUrl(request.getAvatarUrl());

        avatarService.save(avatar);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "update avatar successful"));
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addAvatar(@RequestParam Long avatarId) {
       avatarService.delete(avatarId);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "add avatar successful"));
    }
}
