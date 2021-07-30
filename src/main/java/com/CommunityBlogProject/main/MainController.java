package com.CommunityBlogProject.main;

import com.CommunityBlogProject.account.CurrentUser;
import com.CommunityBlogProject.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String goHome(@CurrentUser Account account, Model model) {
        if(account != null) {
            model.addAttribute(account);
        }
        return "index";
    }

    public String login() {
        return "login";
    }
}
