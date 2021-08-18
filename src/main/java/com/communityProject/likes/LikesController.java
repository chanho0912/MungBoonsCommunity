package com.communityProject.likes;

import com.communityProject.account.AccountRepository;
import com.communityProject.account.CurrentUser;
import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.post.PostRepository;
import com.communityProject.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PostMapping("/likes/add/{postId}")
    public String addLikes(@CurrentUser Account account, @PathVariable Long postId) {
        likesService.addLike(account, postId);

        return "redirect:/post/{postId}/details";
    }

    @PostMapping("/likes/remove/{postId}")
    public String removeLikes(@CurrentUser Account account, @PathVariable Long postId) {
        likesService.removeLike(account, postId);

        return "redirect:/post/{postId}/details";
    }
}
