package com.communityProject.post;

import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.domain.Tag;
import com.communityProject.post.event.PostCreatedEvent;
import com.communityProject.post.form.PostForm;
import com.communityProject.post.form.PostUpdateForm;
import com.communityProject.tags.TagRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

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

        newPost.setCreatedBy(account);
        newPost.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepository.save(newPost);

        eventPublisher.publishEvent(new PostCreatedEvent(savedPost));
        return savedPost;
    }

    private List<String> getTagsFromString(String tags) throws ParseException {
        List<String> retList = new ArrayList<>();
        if(tags.length() == 0) return retList;

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
        Post post = postRepository.findWithTagsById(id);
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

    public Post getPostForUpdateLikes(Account account, Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        return post.orElseThrow(()->new IllegalArgumentException("잘못된 요청입니다."));
    }

    public Post getPostForViews(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) throw new IllegalArgumentException("잘못된 요청입니다.");
        Post retPost = post.get();
        retPost.setCountOfViews(retPost.getCountOfViews() + 1);
        return retPost;
    }

    public void generateTestPost(Account account) throws ParseException {
        for(int i = 0; i < 30; i++) {
            String randomValue = RandomString.make(5);
            PostForm postForm = new PostForm("테스트 스터디 " + randomValue, "테스트용 post 입니다.", "");
            Post post = this.createNewPost(postForm, account);
            Tag Spring = tagRepository.findByTitle("Spring").orElseGet(()->
                    tagRepository.save(Tag.builder().title("Spring").build()));
            post.getTags().add(Spring);
        }
    }
}
