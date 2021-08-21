package com.communityProject.likes;

import com.communityProject.comment.CommentCreatedEvent;
import com.communityProject.comment.CommentDeletedEvent;
import com.communityProject.domain.Account;
import com.communityProject.domain.Notification;
import com.communityProject.domain.Post;
import com.communityProject.notification.NotificationRepository;
import com.communityProject.notification.NotificationType;
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
public class LikesEventListener {
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleLikeCreatedEvent(LikesCreatedEvent likesCreatedEvent){
        Post post = likesCreatedEvent.getPost();
        Account createdBy = post.getCreatedBy();

        Notification notification = new Notification();
        notification.setTitle(post.getTitle());
        notification.setLink("/post/" + post.getId() + "/details");
        notification.setChecked(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setMessage(likesCreatedEvent.getLikesBy() + "님이 " +likesCreatedEvent.getMessage());

        notification.setAccount(createdBy);
        notification.setNotificationType(NotificationType.LIKES_CREATED);
        notificationRepository.save(notification);
    }

    @EventListener
    public void handleLikeDeletedEvent(LikesDeletedEvent likesDeletedEvent){
        Post post = likesDeletedEvent.getPost();
        Account createdBy = post.getCreatedBy();

        Notification notification = new Notification();
        notification.setTitle(post.getTitle());
        notification.setLink("/post/" + post.getId() + "/details");
        notification.setChecked(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setMessage(likesDeletedEvent.getLikesBy() + "님이 " +likesDeletedEvent.getMessage());

        notification.setAccount(createdBy);
        notification.setNotificationType(NotificationType.LIKES_CREATED);
        notificationRepository.save(notification);
    }

}
