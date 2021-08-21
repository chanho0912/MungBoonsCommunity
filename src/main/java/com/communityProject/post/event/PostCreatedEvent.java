package com.communityProject.post.event;

import com.communityProject.domain.Post;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
@RequiredArgsConstructor
public class PostCreatedEvent {
    private final Post post;
}
