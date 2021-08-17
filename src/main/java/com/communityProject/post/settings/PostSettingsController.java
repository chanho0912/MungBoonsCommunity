package com.communityProject.post.settings;

import com.communityProject.account.CurrentUser;
import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.domain.Tag;
import com.communityProject.post.PostService;
import com.communityProject.post.form.PostUpdateForm;
import com.communityProject.tags.TagForm;
import com.communityProject.tags.TagRepository;
import com.communityProject.tags.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/post/{id}/settings")
@RequiredArgsConstructor
public class PostSettingsController {
    private final PostService postService;

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    private final TagRepository tagRepository;
    private final TagService tagService;

    @GetMapping("/update")
    public String viewPostSettings(@CurrentUser Account account, @PathVariable Long id, Model model) {
        Post post = postService.getPostForUpdate(account, id);
        model.addAttribute(account);
        model.addAttribute(post);
        model.addAttribute(modelMapper.map(post, PostUpdateForm.class));
        return "post/settings/description";
    }

    @PostMapping("/update")
    public String updatePostInfo(@CurrentUser Account account, @PathVariable Long id, Model model,
                                 PostUpdateForm postUpdateForm, Errors errors,
                                 RedirectAttributes redirectAttributes) {
        Post post = postService.getPostForUpdate(account, id);
        if(errors.hasErrors()) {
            model.addAttribute(account);
            model.addAttribute(post);
            return "post/settings/update";
        }
        postService.updatePostContent(post, postUpdateForm);
        redirectAttributes.addFlashAttribute("message", "게시글 내용을 수정했습니다.");
        return "redirect:/post/" + URLEncoder.encode(String.valueOf(id), StandardCharsets.UTF_8) + "/settings/update";
    }

    @GetMapping("/tags")
    public String viewUpdateTagsForm(@CurrentUser Account account, @PathVariable Long id, Model model) throws JsonProcessingException {
        Post post = postService.getPostForUpdate(account, id);
        model.addAttribute(account);
        model.addAttribute(post);

        model.addAttribute("tags", post.getTags().stream()
                            .map(Tag::getTitle).collect(Collectors.toList()));

        List<String> allTagTitles = tagRepository.findAll().stream()
                                .map(Tag::getTitle).collect(Collectors.toList());
        model.addAttribute("whitelist", objectMapper.writeValueAsString(allTagTitles));
        return "post/settings/tags";
    }

    @PostMapping("/tags/add")
    @ResponseBody
    public ResponseEntity addTag(@CurrentUser Account account, @PathVariable Long id, @RequestBody TagForm tagForm) {
        Post post = postService.getPostForUpdateTag(account, id);
        Tag tag = tagService.findOrCreateNew(tagForm.getTagTitle());
        postService.addTag(post, tag);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tags/remove")
    @ResponseBody
    public ResponseEntity removeTag(@CurrentUser Account account, @PathVariable Long id, @RequestBody TagForm tagForm) {
        Post post = postService.getPostForUpdateTag(account, id);
        Tag tag = tagRepository.findByTitle(tagForm.getTagTitle()).orElseThrow(()->new IllegalArgumentException("해당 태그가 없습니다."));
        postService.removeTag(post, tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post-delete")
    public String viewUpdatePostDelete(@CurrentUser Account account, @PathVariable Long id, Model model) {
        Post post = postService.getPostForUpdate(account, id);
        model.addAttribute(account);
        model.addAttribute(post);
        return "post/settings/post-delete";
    }

    @PostMapping("/post/remove")
    public String deletePost(@CurrentUser Account account, @PathVariable Long id, Model model) {
        Post post = postService.getPostForUpdate(account, id);
        postService.remove(post);
        return "redirect:/";
    }
}
