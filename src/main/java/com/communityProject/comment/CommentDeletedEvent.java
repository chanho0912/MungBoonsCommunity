package com.communityProject.comment;

import com.communityProject.domain.Comment;
import com.communityProject.domain.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentDeletedEvent {
    private final String commentDeletedBy;
    private final Post post;
    private final String message;
}
