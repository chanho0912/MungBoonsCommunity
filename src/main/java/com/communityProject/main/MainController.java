package com.communityProject.main;

import com.communityProject.account.AccountRepository;
import com.communityProject.account.CurrentUser;
import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.post.PostRepository;
import com.communityProject.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final PostService postService;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @GetMapping("/")
    public String goHome(@CurrentUser Account account, Model model) {
        if(account != null) {
            Account accountLoaded = accountRepository.findAccountWithTagsById(account.getId());
            model.addAttribute(accountLoaded);
            model.addAttribute("postList", postRepository.findAll());
            return "index-after-login";
        }

        model.addAttribute("postList", postRepository.findAll());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
