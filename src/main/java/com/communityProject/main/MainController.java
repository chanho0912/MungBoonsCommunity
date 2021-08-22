package com.communityProject.main;

import com.communityProject.account.AccountRepository;
import com.communityProject.account.CurrentUser;
import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.post.PostRepository;
import com.communityProject.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final PostService postService;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @GetMapping("/")
    public String goHome(@CurrentUser Account account, Model model) {
        model.addAttribute("postList", postRepository.findFirst9ByOrderByCreatedAtDesc());
        if(account != null) {
            Account accountLoaded = accountRepository.findAccountWithTagsById(account.getId());
            model.addAttribute(accountLoaded);
            model.addAttribute("postManagerOf", postRepository.findFirst5ByCreatedByOrderByCreatedAtDesc(accountLoaded));
            model.addAttribute("postByFavoriteTag", postRepository.findByTags(accountLoaded.getTags()));
            return "index-after-login";
        }

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/search/post")
    public String searchPost(@PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                             @CurrentUser Account account, String keyword, Model model) {
        if(account != null) {
            model.addAttribute(account);
        }
        Page<Post> postList = postRepository.findByKeyword(keyword, pageable);
        model.addAttribute("postPage", postList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortProperty", pageable.getSort().toString().contains("createdAt") ? "createdAt" : "likesCount");
        return "search";
    }

    @GetMapping("search/tag/{keyword}")
    public String searchTag(@PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                            @PathVariable String keyword, @CurrentUser Account account, Model model) {
        if(account != null) {
            model.addAttribute(account);
        }
        Page<Post> postList = postRepository.findByKeyword(keyword, pageable);
        model.addAttribute("postPage", postList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortProperty", pageable.getSort().toString().contains("createdAt") ? "createdAt" : "likesCount");

        return "search";
    }
}
