package com.communityProject.likes;

import com.communityProject.domain.Account;
import com.communityProject.domain.Post;
import com.communityProject.post.PostRepository;
import com.communityProject.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesService {
    private final PostService postService;
    private final ApplicationEventPublisher eventPublisher;

    public void addLike(Account account, Long postId) {
        Post post = postService.getPostForUpdateLikes(account, postId);
        post.addLikesToPost(account);
        eventPublisher.publishEvent(new LikesCreatedEvent(account.getNickname(), post, "추천을 눌렀습니다."));
        System.out.println("addLike called, " + postId);
        post.setLikesCount(post.getWhoLikes().size());
    }

    public void removeLike(Account account, Long postId) {
        Post post = postService.getPostForUpdateLikes(account, postId);
        post.deleteLikesToPost(account);
        eventPublisher.publishEvent(new LikesDeletedEvent(account.getNickname(), post, "추천을 취소하였습니다."));
        System.out.println("removeLike called, " + postId);
        post.setLikesCount(post.getWhoLikes().size());
    }
}
