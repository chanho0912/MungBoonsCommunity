package com.communityProject.comment;

import com.communityProject.account.CurrentUser;
import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/comments/{postId}/add")
    public String addComment(@CurrentUser Account account, @PathVariable Long postId,
                             Model model, @Valid CommentForm commentForm, Errors errors) {
        if(errors.hasErrors()) {
            return "post/details";
        }
        Post post = postService.getPost(postId);
        commentService.addCommentToPost(account, post, commentForm);

        return "redirect:/post/{postId}/details";
    }

    @GetMapping("/comments/delete/{postId}/{commentId}")
    public String deleteComment(@CurrentUser Account account, @PathVariable Long postId, @PathVariable Long commentId,
                                        Model model) {

        commentService.deleteComment(postId, commentId);
        return "redirect:/post/{postId}/details";
    }
}
