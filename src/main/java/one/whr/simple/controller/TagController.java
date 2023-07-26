package one.whr.simple.controller;

import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.AddTagRequest;
import one.whr.simple.dto.request.UpdateTagRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.entity.Tag;
import one.whr.simple.exceptions.TagNotFoundException;
import one.whr.simple.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/tag")
public class TagController {
    @Autowired
    TagService tagService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> addTag(@RequestBody AddTagRequest request) {
        Tag tag = new Tag(request.getName());
        tagService.save(tag);
        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Add tag successfully"));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> updateTag(@RequestBody UpdateTagRequest request) {
        try {
            Tag tag = tagService.findById(request.getId());
            tag.setName(request.getName());
            tagService.save(tag);

        } catch (TagNotFoundException e) {
            return ResponseEntity.ok().body(new MessageResponse(MessageCode.TGA_NOT_FOUND, e.getMessage()));
        }

        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Update tag successfully"));
    }
}
