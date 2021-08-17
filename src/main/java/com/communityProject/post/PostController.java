package com.communityProject.post;

import com.communityProject.account.CurrentUser;
import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.domain.Tag;
import com.communityProject.tags.TagForm;
import com.communityProject.tags.TagRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;


    @GetMapping("/new-post")
    public String newPostForm(@CurrentUser Account account, Model model) throws JsonProcessingException {
        model.addAttribute(account);
        model.addAttribute(new PostForm());

        List<String> allTags = tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTags));
        return "post/form";
    }

    @PostMapping("/new-post")
    public String newPostSubmit(@CurrentUser Account account, PostForm postForm, Model model) throws ParseException {
        Post newPost = postService.createNewPost(postForm, account);
        return "redirect:/post/" + URLEncoder.encode(newPost.getId().toString(), StandardCharsets.UTF_8);
    }

    @GetMapping("/post/{postId}")
    public String viewPost(@CurrentUser Account account, @PathVariable Long postId, Model model) {
        Post post = postService.getPost(postId);

        model.addAttribute(account);
        model.addAttribute(post);
        return "post/view";
    }

    @PostMapping("/post/tags/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Tag tag = tagRepository.findByTitle(title).orElseGet(
                ()->tagRepository.save(Tag.builder().title(title).build())
        );
        System.out.println("tag repository add");
        System.out.println(tag.getTitle());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post/tags/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentUser Account account, @RequestBody TagForm tagForm) {
        String title = tagForm.getTagTitle();
        Optional<Tag> tag = tagRepository.findByTitle(title);
        if(tag.isEmpty()) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }
}
