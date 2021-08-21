package com.communityProject.comment;

import com.communityProject.domain.Account;
import com.communityProject.domain.Comment;
import com.communityProject.domain.Post;
import com.communityProject.post.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final PostService postService;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public void addCommentToPost(Account account, Post post, CommentForm commentForm) {
        Comment comment = modelMapper.map(commentForm, Comment.class);
        comment.setCreatedBy(account);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        eventPublisher.publishEvent(new CommentCreatedEvent(comment.getCreatedBy().getNickname(), post, "댓글이 추가되었습니다."));

        post.getComments().add(comment);
        post.setCommentCount(post.getComments().size());
    }

    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new IllegalArgumentException("해당하는 댓글을 찾을 수 없습니다."));

        Post post = postService.getPost(postId);
        post.getComments().remove(comment);
        post.setCommentCount(post.getComments().size());

        eventPublisher.publishEvent(new CommentDeletedEvent(comment.getCreatedBy().getNickname(), post, "댓글이 삭제되었습니다."));

//        eventPublisher.publishEvent(new CommentCreatedEvent(comment, "댓글이 삭제되었습니다."));
        commentRepository.delete(comment);
    }
}
