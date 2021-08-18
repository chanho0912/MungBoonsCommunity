package com.communityProject.comment;

import com.communityProject.domain.Account;
import com.communityProject.domain.Comment;
import com.communityProject.domain.Post;
import com.communityProject.post.PostService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    public void addCommentToPost(Account account, Post post, CommentForm commentForm) {
        Comment comment = modelMapper.map(commentForm, Comment.class);
        comment.setCreatedBy(account);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        post.getComments().add(comment);
        post.setCommentCount(post.getComments().size());
    }

    public void deleteComment(Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new IllegalArgumentException("해당하는 댓글을 찾을 수 없습니다."));

        Post post = postService.getPost(postId);
        post.getComments().remove(comment);
        post.setCommentCount(post.getComments().size());

        commentRepository.delete(comment);
    }
}
