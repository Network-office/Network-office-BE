package dev.office.networkoffice.feed.controller;

import dev.office.networkoffice.feed.controller.docs.CommentApiDocs;
import dev.office.networkoffice.feed.dto.request.CommentWrite;
import dev.office.networkoffice.feed.service.CommentService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/comments")
public class CommentController implements CommentApiDocs {

    private final CommentService commentService;

    @PostMapping
    public void writeComment(Principal principal, @RequestBody CommentWrite commentWrite) {
        Long userId = Long.parseLong(principal.getName());
        commentService.writeComment(userId, commentWrite);
    }

    @DeleteMapping("{commentId}")
    public void removeComment(Principal principal, @PathVariable Long commentId) {
        Long userId = Long.parseLong(principal.getName());
        commentService.removeComment(userId, commentId);
    }
}
