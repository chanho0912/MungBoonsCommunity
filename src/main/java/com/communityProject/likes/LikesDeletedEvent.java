package com.communityProject.likes;

import com.communityProject.domain.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LikesDeletedEvent {
    private final String likesBy;
    private final Post post;
    private final String message;
}
