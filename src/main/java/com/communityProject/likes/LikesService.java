package com.communityProject.likes;

import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.post.PostRepository;
import com.communityProject.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesService {
    private final PostService postService;

    public void addLike(Account account, Long postId) {
        Post post = postService.getPostForUpdateLikes(account, postId);
        post.getWhoLikes().add(account);
        post.setLikesCount(post.getWhoLikes().size());
    }

    public void removeLike(Account account, Long postId) {
        Post post = postService.getPostForUpdateLikes(account, postId);
        post.getWhoLikes().remove(account);
        post.setLikesCount(post.getWhoLikes().size());
    }
}
