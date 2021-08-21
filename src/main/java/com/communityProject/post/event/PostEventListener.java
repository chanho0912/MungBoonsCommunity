package com.communityProject.post.event;

import com.communityProject.account.AccountPredicates;
import com.communityProject.account.AccountRepository;
import com.communityProject.domain.Account;
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
import java.util.Iterator;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class PostEventListener {
    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handlePostCreatedEvent(PostCreatedEvent postCreatedEvent) {

        Post post = postRepository.findWithTagsById(postCreatedEvent.getPost().getId());
        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTags(post.getTags()));

        accounts.forEach(account -> {
//            if(account.isPostCreatedByEmail()) {
//                TODO
//                이메일 전송 구현
//            }
            if(account.isPostCreatedByWeb()) {
                Notification notification = new Notification();
                notification.setTitle(post.getTitle());
                notification.setLink("/post/" + post.getId() + "/details");
                notification.setChecked(false);
                notification.setCreatedAt(LocalDateTime.now());
                // message?
                notification.setAccount(account);
                notification.setNotificationType(NotificationType.POST_CREATED);
                notificationRepository.save(notification);
            }
        });
    }
}
