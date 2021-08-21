package com.communityProject.comment;

import com.communityProject.account.AccountPredicates;
import com.communityProject.account.AccountRepository;
import com.communityProject.domain.Account;
import com.communityProject.domain.Comment;
import com.communityProject.domain.Notification;
import com.communityProject.domain.Post;
import com.communityProject.notification.NotificationRepository;
import com.communityProject.notification.NotificationType;
import com.communityProject.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class CommentEventListener {
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleCommentCreatedEvent(CommentCreatedEvent commentCreatedEvent){
        Post post = commentCreatedEvent.getPost();
        Account createdBy = post.getCreatedBy();

        Notification notification = new Notification();
        notification.setTitle(post.getTitle());
        notification.setLink("/post/" + post.getId() + "/details");
        notification.setChecked(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setMessage(commentCreatedEvent.getCommentCreatedBy() + "님의 " +commentCreatedEvent.getMessage());

        notification.setAccount(createdBy);
        notification.setNotificationType(NotificationType.COMMENT_CREATED);
        notificationRepository.save(notification);
    }

    @EventListener
    public void handleCommentDeletedEvent(CommentDeletedEvent commentDeletedEvent){
        Post post = commentDeletedEvent.getPost();
        Account createdBy = post.getCreatedBy();

        Notification notification = new Notification();
        notification.setTitle(post.getTitle());
        notification.setLink("/post/" + post.getId() + "/details");
        notification.setChecked(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setMessage(commentDeletedEvent.getCommentDeletedBy() + "님의 " +commentDeletedEvent.getMessage());

        notification.setAccount(createdBy);
        notification.setNotificationType(NotificationType.COMMENT_CREATED);
        notificationRepository.save(notification);
    }
}
