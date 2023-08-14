package one.whr.simple.controller;

import one.whr.simple.constant.MessageCode;
import one.whr.simple.dto.request.AddTagRequest;
import one.whr.simple.dto.request.UpdateTagRequest;
import one.whr.simple.dto.response.MessageResponse;
import one.whr.simple.dto.response.TagListResponse;
import one.whr.simple.entity.Tag;
import one.whr.simple.exceptions.TagNotFoundException;
import one.whr.simple.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    ResponseEntity<?> updateTag(@RequestBody UpdateTagRequest request) throws TagNotFoundException {
        Tag tag = tagService.findById(request.getId());
        tag.setName(request.getName());
        tagService.save(tag);

        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Update tag successfully"));
    }

    @GetMapping("/all")
    ResponseEntity<?> getTagList() {
        List<Tag> tags = tagService.getTagList();
        return ResponseEntity.ok().body(new TagListResponse(MessageCode.SUCCESSFUL, "Get Tag List Successful", tags));
    }

    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<?> deleteTag(@RequestParam Long tagId) throws TagNotFoundException {
        tagService.removeTagById(tagId);

        return ResponseEntity.ok().body(new MessageResponse(MessageCode.SUCCESSFUL, "Delete tag successfully"));
    }


}
