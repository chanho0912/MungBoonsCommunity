package com.communityProject.post;

import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.domain.Tag;
import com.communityProject.tags.TagRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
