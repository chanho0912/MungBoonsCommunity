package com.communityProject.post;

import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.domain.Tag;
import com.communityProject.post.form.PostForm;
import com.communityProject.post.form.PostUpdateForm;
import com.communityProject.tags.TagRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;

    public Post getPost(Long postId){
        Optional<Post> post = postRepository.findById(postId);
        return post.orElseThrow(()->new IllegalArgumentException("잘못된 요청입니다."));
    }

    public Post createNewPost(PostForm postForm, Account account) throws ParseException {
        Post newPost = modelMapper.map(postForm, Post.class);
        List<String> tagList = getTagsFromString(postForm.getTags());

        for (String s : tagList) {
            Tag tag = tagRepository.findByTitle(s).orElseGet(
                    ()->tagRepository.save(Tag.builder().title(s).build())
            );
            newPost.getTags().add(tag);
        }

        Post savedPost = postRepository.save(newPost);
        savedPost.setCreatedBy(account);
        savedPost.setCreatedAt(LocalDateTime.now());
        return savedPost;
    }

    private List<String> getTagsFromString(String tags) throws ParseException {
        List<String> retList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(tags);

        for (Object o : jsonArray) {
            JSONObject tempObj = (JSONObject) o;
            System.out.println(tempObj.get("value"));
            retList.add(tempObj.get("value").toString());
        }

        return retList;
    }

    public Post getPostForUpdate(Account account, Long id) {
        Post post = getPost(id);
        if(!account.isManagerOf(post)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
        return post;
    }

    public void updatePostContent(Post post, PostUpdateForm postUpdateForm) {
        modelMapper.map(postUpdateForm, post);
    }

    public Post getPostForUpdateTag(Account account, Long id) {
        Post post = postRepository.findPostWithTagsById(id);
        if(post == null) throw new IllegalArgumentException("해당 post를 찾을 수 없습니다.");

        if(!account.isManagerOf(post)) {
            throw new AccessDeniedException("해당 기능을 사용할 수 없습니다.");
        }
        return post;
    }

    public void addTag(Post post, Tag tag) {
        post.getTags().add(tag);
    }

    public void removeTag(Post post, Tag tag) {
        post.getTags().remove(tag);
    }

    public void remove(Post post) {
        postRepository.delete(post);
    }
}
