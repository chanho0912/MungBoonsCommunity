package com.communityProject.notification;

import com.communityProject.account.CurrentUser;
import com.communityProject.domain.Account;
import com.communityProject.domain.Notification;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String getNotificationsView(@CurrentUser Account account, Model model) {
        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedAtDesc(account, false);
        long numberOfChecked = notificationRepository.countByAccountAndChecked(account, true);
        putCategorizedNotifications(model, notifications, numberOfChecked, notifications.size());

        model.addAttribute(account);
        model.addAttribute("isNew", true);
        notificationService.markAsRead(notifications);
        return "notification/list";
    }

    @GetMapping("/notifications/old")
    public String getOldNotifications(@CurrentUser Account account, Model model) {
        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedAtDesc(account, true);
        long numberOfNotChecked = notificationRepository.countByAccountAndChecked(account, false);
        putCategorizedNotifications(model, notifications, notifications.size(), numberOfNotChecked);

        model.addAttribute(account);
        model.addAttribute("isNew", false);
        return "notification/list";
    }

    @DeleteMapping("notifications")
    public String deleteNotifications(@CurrentUser Account account) {
        notificationRepository.deleteByAccountAndChecked(account, true);
        return "redirect:/notifications";
    }

    private void putCategorizedNotifications(Model model, List<Notification> notifications,
                                             long numberOfChecked, long numberOfNotChecked) {

        List<Notification> newPostNotifications = new ArrayList<>();
        List<Notification> newCommentNotifications = new ArrayList<>();
        List<Notification> newLikesNotifications = new ArrayList<>();

        for(var notification : notifications) {
            switch (notification.getNotificationType()) {
                case POST_CREATED: newPostNotifications.add(notification); break;
                case COMMENT_CREATED: newCommentNotifications.add(notification); break;
                case LIKES_CREATED: newLikesNotifications.add(notification); break;
            }
        }

        model.addAttribute("numberOfChecked", numberOfChecked);
        model.addAttribute("numberOfNotChecked", numberOfNotChecked);
        model.addAttribute("notifications", notifications);
        model.addAttribute("newPostNotifications", newPostNotifications);
        model.addAttribute("newCommentNotifications", newCommentNotifications);
        model.addAttribute("newLikesNotifications", newLikesNotifications);
    }
}
