package com.communityProject.comment;

import com.communityProject.domain.Comment;
import com.communityProject.domain.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
@RequiredArgsConstructor
public class CommentCreatedEvent {
    private final String commentCreatedBy;
    private final Post post;
    private final String message;
}
